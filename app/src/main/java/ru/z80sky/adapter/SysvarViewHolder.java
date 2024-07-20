package ru.z80sky.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import ru.z80sky.R;

public class SysvarViewHolder extends RecyclerView.ViewHolder {
    public TextView address, size, name, desc, descBig, desc2;
    public View header, notes;

    public int position;

    public SysvarViewHolder(View v) {
        super(v);
        address = (TextView) v.findViewById(R.id.address);
        size = (TextView) v.findViewById(R.id.size);
        name = (TextView) v.findViewById(R.id.name);
        desc = (TextView) v.findViewById(R.id.desc);
        descBig = (TextView) v.findViewById(R.id.descBig);
        desc2 = (TextView) v.findViewById(R.id.desc2);
        header = v.findViewById(R.id.header_sysvar);
        notes = v.findViewById(R.id.notes);
    }
}
