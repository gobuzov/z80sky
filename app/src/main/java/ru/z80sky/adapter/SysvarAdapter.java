package ru.z80sky.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import ru.z80sky.App;
import ru.z80sky.R;
import ru.z80sky.Sysvar;
import ru.z80sky.c;

public class SysvarAdapter extends RecyclerView.Adapter<SysvarViewHolder>{
    ArrayList<Sysvar> list;
    int openId = -1;
    boolean hex = true;

    public SysvarAdapter(ArrayList<Sysvar> Data) {
        list = Data;
        hex = 1==App.getInstance().getInt(c.HEX_MODE, 1);
    }
    public void setOpen(int pos) {
        openId = pos;
    }
    public void setList(ArrayList<Sysvar> Data) {
        list = Data;
        notifyDataSetChanged();
    }
    @Override
    public SysvarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sysvar, parent, false);
        SysvarViewHolder holder = new SysvarViewHolder(view);
        return holder;
    } //     public TextView address, size, name, desc, desc2;
    @Override
    public void onBindViewHolder(final SysvarViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Sysvar sv = list.get(position);
        String adr = hex ? Integer.toHexString(sv.address) : Integer.toString(sv.address);
        holder.address.setText(adr);
        holder.size.setText(Integer.toString(sv.len));
        holder.name.setText(sv.name);
        //holder.desc.setText(sv.desc);
        holder.descBig.setText(sv.desc);
        if (0==sv.effect)
            holder.desc2.setVisibility(View.GONE);
        else{
            holder.desc2.setVisibility(View.VISIBLE);
            holder.desc2.setText('X'==sv.effect ? R.string.sysvar_x : R.string.sysvar_n);
        }
//        holder.header.setBackgroundColor(cc.isNextCommand()? c.NEXT_BG : cc.isUndocCommand() ? c.UNOFF_BG : c.NORMAL_BG);
        holder.position = position;
        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openId==holder.position) {
                    holder.notes.setVisibility(View.GONE);
                    openId = -1;
                }else{
                    holder.notes.setVisibility(View.VISIBLE);
                    openId = holder.position;
                }
            }
        });
        holder.notes.setVisibility(openId==holder.position?View.VISIBLE:View.GONE);
    }
    /**
     * количество элементов
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

}
