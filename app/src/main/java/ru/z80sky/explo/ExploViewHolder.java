package ru.z80sky.explo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.z80sky.App;
import ru.z80sky.R;

public class ExploViewHolder extends RecyclerView.ViewHolder {
    public TextView start, size, name, desc, descBig, desc2;
    public View header, notes;
    androidx.recyclerview.widget.RecyclerView rv;

    public int position;

    public ExploViewHolder(View v) {
        super(v);
        start = (TextView) v.findViewById(R.id.start);
        size = (TextView) v.findViewById(R.id.size);
        name = (TextView) v.findViewById(R.id.name);
        desc = (TextView) v.findViewById(R.id.desc);
        descBig = (TextView) v.findViewById(R.id.descBig);
        desc2 = (TextView) v.findViewById(R.id.desc2);
        header = v.findViewById(R.id.header_explo);
        notes = v.findViewById(R.id.notes);
        rv = v.findViewById(R.id.rview2);
        CustomLinearLayoutManager lm = new CustomLinearLayoutManager(App.getInstance().getBaseContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lm);
    }
}
