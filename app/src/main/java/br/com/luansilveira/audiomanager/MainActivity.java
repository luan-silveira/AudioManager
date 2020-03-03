package br.com.luansilveira.audiomanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.luansilveira.audiomanager.db.DB;
import br.com.luansilveira.audiomanager.db.Model.Horario;

public class MainActivity extends AppCompatActivity implements AbsListView.MultiChoiceModeListener {

    private static final int REQUEST_HORARIO = 6;

    TextView txtSemHorario;
    Switch swAtivado;
    View layoutPrincipal;
    ListView listViewHorarios;
    FloatingActionButton btAdicionar;

    private Dao<Horario, ?> daoHorario;
    private List<Horario> listHorarios = new ArrayList<>();
    private ListHorariosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutPrincipal = findViewById(R.id.layoutPrincipal);
        txtSemHorario = findViewById(R.id.txtSemHorario);
        swAtivado = findViewById(R.id.swAtivado);
        listViewHorarios = findViewById(R.id.listViewHorarios);
        listViewHorarios.setMultiChoiceModeListener(this);

        btAdicionar = findViewById(R.id.btAdicionar);
        btAdicionar.setOnClickListener(view -> startActivityForResult(new Intent(this, HorarioActivity.class), REQUEST_HORARIO));

        try {
            this.daoHorario = DB.get(this).getDao(Horario.class);
            this.listHorarios = daoHorario.queryForAll();

            this.adapter = new ListHorariosAdapter(this, listHorarios);
            this.listViewHorarios.setAdapter(this.adapter);

            listViewHorarios.setOnItemClickListener((parent, view, position, id) -> {
                Horario horario = (Horario) parent.getItemAtPosition(position);
                startActivityForResult(new Intent(this, HorarioActivity.class).putExtra(HorarioActivity.EXTRA_HORARIO, horario), REQUEST_HORARIO);
            });

            mostrarLista();

            this.ativarHorarios(this.isManagerAtivado());
            swAtivado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                this.ativarHorarios(isChecked);
                this.setPrefsAtivarManager(isChecked);
                HorarioManager.agendarOuCancelarHorarios(this, this.listHorarios, !isChecked);
            });

            swAtivado.setChecked(this.isManagerAtivado());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SharedPreferences getPrefs() {
        return this.getSharedPreferences("prefs", MODE_PRIVATE);
    }

    private void setPrefsAtivarManager(boolean ativar) {
        SharedPreferences.Editor editor = this.getPrefs().edit();
        editor.putBoolean("ativar", ativar).apply();
    }

    private boolean isManagerAtivado() {
        return this.getPrefs().getBoolean("ativar", false);
    }

    private void ativarHorarios(boolean ativar) {
        boolean listaVazia = this.listHorarios.size() == 0;
        listViewHorarios.setEnabled(ativar);
        btAdicionar.setVisibility((ativar || listaVazia) ? View.VISIBLE : View.GONE);
        btAdicionar.setEnabled(ativar || listaVazia);
    }

    private void excluirHorarios(ActionMode mode, List<Horario> horarios) {
        new AlertDialog.Builder(this).setTitle("Excluir")
                .setMessage("Deseja excluir os horários selecionados?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    try {
                        HorarioManager.cancelarHorarios(MainActivity.this, horarios);
                        if (this.daoHorario.delete(horarios) > 0) {
                            mode.finish();
                            atualizarLista();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }).setNegativeButton("Não", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_HORARIO) return;
        if (resultCode == RESULT_OK) {
            if (! swAtivado.isChecked()) swAtivado.setChecked(true);
            atualizarLista();
        }
    }

    private void mostrarLista() {
        boolean mostrar = this.listHorarios.size() > 0;
        this.layoutPrincipal.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        this.txtSemHorario.setVisibility(!mostrar ? View.VISIBLE : View.GONE);
    }

    private void atualizarLista() {
        try {
            List<Horario> list = daoHorario.queryForAll();
            this.listHorarios.clear();
            this.listHorarios.addAll(list);
            adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mostrarLista();
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getMenuInflater().inflate(R.menu.list_select_menu, menu);
        adapter.setActionMode(true);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.menuDelete) {
            this.excluirHorarios(mode, adapter.getSelectedItems());
        }

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        adapter.setActionMode(false);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        adapter.setSelection(position, checked);
        int count = adapter.getCountSelected();
        mode.setTitle(count + " selecionado" + (count > 1 ? "s" : ""));
    }


}
