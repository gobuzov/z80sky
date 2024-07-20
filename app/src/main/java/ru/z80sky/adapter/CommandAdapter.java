package ru.z80sky.adapter;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import ru.z80sky.App;
import ru.z80sky.Command;
import ru.z80sky.MainActivity;
import ru.z80sky.R;
import ru.z80sky.c;

public class CommandAdapter extends RecyclerView.Adapter<CommandViewHolder>{
    ArrayList<Command> list;
    int openId = -1;

    public CommandAdapter(ArrayList<Command> Data) {
        list = Data;
    }
    public void setOpen(int pos) {
        openId = pos;
    }
    public void setList(ArrayList<Command> Data) {
        list = Data;
        notifyDataSetChanged();
    }
    @Override
    public CommandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_command, parent, false);
        CommandViewHolder holder = new CommandViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final CommandViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Command cc = list.get(position);
        holder.command.setText(cc.mnem);
/// hack for long commands : decrease textsize
        int textsize = 24;
        if (MainActivity.orientation == Configuration.ORIENTATION_PORTRAIT &&
            1!=App.getInstance().getInt(c.HEX_MODE, 1) &&
            cc.mnem.length()>12)
            textsize = 20;
        holder.command.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textsize);
//\
        holder.desc.setText(cc.desc);
        holder.header.setBackgroundColor(cc.isNextCommand()? c.NEXT_BG :
                cc.isUndocCommand() ? c.UNOFF_BG : c.NORMAL_BG);
        holder.tacts.setText(cc.tacts);
        holder.fc.setText(Character.toString(cc.flags.charAt(0))); // opti ???
        holder.fn.setText(Character.toString(cc.flags.charAt(1)));
        holder.fpv.setText(Character.toString(cc.flags.charAt(2)));
        holder.fh.setText(Character.toString(cc.flags.charAt(3)));
        holder.fz.setText(Character.toString(cc.flags.charAt(4)));
        holder.fs.setText(Character.toString(cc.flags.charAt(5)));

        holder.bytes.setText(cc.getBytes());
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
        if (-1!=cc.picId){
            holder.pic.setImageDrawable(AppCompatResources.getDrawable(App.getInstance(), cc.picId));
            holder.pic.setVisibility(View.VISIBLE);
        }else
            holder.pic.setVisibility(View.GONE);
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