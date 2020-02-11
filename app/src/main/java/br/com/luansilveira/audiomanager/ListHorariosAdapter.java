package br.com.luansilveira.audiomanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import br.com.luansilveira.audiomanager.db.Model.Horario;

public class ListHorariosAdapter extends ArrayAdapter<Horario> {

    private boolean[] selection;
    private boolean actionMode;
    private List<Horario> listSelect;

    public ListHorariosAdapter(@NonNull Context context, @NonNull List<Horario> lista) {
        super(context, 0, lista);
    }

    public void resetSelection() {
        this.selection = new boolean[getCount()];
        this.listSelect.clear();
    }

    public void setSelection(int position, boolean selected) {
        this.selection[position] = selected;
        Horario h = getItem(position);
        if (selected) listSelect.add(h);
        else listSelect.remove(h);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Context context = getContext();
        if (convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.layout_list_horarios, parent, false);

        TextView txtHorario = convertView.findViewById(R.id.txtHorario);
        TextView txtModo = convertView.findViewById(R.id.txtModo);

        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setVisibility(this.actionMode ? View.VISIBLE : View.GONE);
        if (actionMode) checkBox.setChecked(selection[position]);

        Horario horario = getItem(position);

        if (horario != null){
            txtHorario.setText(String.format(context.getString(R.string.txt_horario), horario.getHoraInicial(), horario.getHoraFinal()));
            txtModo.setText(String.format(context.getString(R.string.txt_modo), horario.getModo() == Horario.MODO_VIBRAR ? "Vibrar" : "Silencioso"));
        }

        return convertView;
    }

    public ListHorariosAdapter setActionMode(boolean actionMode){
        this.actionMode = actionMode;
        if (!actionMode) resetSelection();
        return this;
    }

    public int getCountSelected() {
        return this.listSelect.size();
    }

    public List<Horario> getSelectedItems() {
        return this.listSelect;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!this.actionMode) this.selection = new boolean[getCount()];
    }
}
