package ru.z80sky.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import ru.z80sky.R;

public class CommandViewHolder extends RecyclerView.ViewHolder {
    public TextView command, bytes, tacts, fc, fz, fpv, fs, fn, fh, desc;
    public ImageView pic;
    public View header, notes;

    public int position;

    public CommandViewHolder(View v) {
        super(v);
        command = (TextView) v.findViewById(R.id.command);
        bytes = (TextView) v.findViewById(R.id.bytes);
        tacts = (TextView) v.findViewById(R.id.tacts);
        fc = (TextView) v.findViewById(R.id.fc);
        fz = (TextView) v.findViewById(R.id.fz);
        fpv = (TextView) v.findViewById(R.id.fpv);
        fs = (TextView) v.findViewById(R.id.fs);
        fn = (TextView) v.findViewById(R.id.fn);
        fh = (TextView) v.findViewById(R.id.fh);
        desc = (TextView) v.findViewById(R.id.desc);
        pic = (ImageView) v.findViewById(R.id.pic);
        header = v.findViewById(R.id.header_command);
        notes = v.findViewById(R.id.notes);
    }
}
