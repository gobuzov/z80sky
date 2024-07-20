package ru.z80sky.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import ru.z80sky.Command;
import ru.z80sky.Sysvar;
import ru.z80sky.Tools;
import ru.z80sky.adapter.CommandAdapter;
import ru.z80sky.R;
import ru.z80sky.adapter.SysvarAdapter;

public class CommandSetFragment extends BaseFragment {
    RecyclerView rList;
    View header_command;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_command_set, container, false);
        header_command = fragmentView.findViewById(R.id.header_command);
        header_command.setVisibility(View.GONE);
        TextView command = header_command.findViewById(R.id.command);
        command.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rList = fragmentView.findViewById(R.id.rview);
        rList.setLayoutManager(lm);
        //Tools.showActivityKeyboard(getActivity());
        return fragmentView;
    }
//
    public void update(String s) {
        ArrayList<Command> clist = Command.find(s);
        header_command.setVisibility(clist.isEmpty() ? View.GONE : View.VISIBLE);
        if (false==clist.isEmpty()){
            CommandAdapter ca = new CommandAdapter(clist);
            rList.setAdapter(ca);
            if (false==clist.isEmpty())
                ca.setList(clist);
            if (1 == clist.size() || s.equals(clist.get(0).mnem))
                ca.setOpen(0);
            return;
        }
        ArrayList<Sysvar> slist = Sysvar.find(s);
        SysvarAdapter sa = new SysvarAdapter(slist);
        rList.setAdapter(sa);
        if (false==slist.isEmpty() && (1 == slist.size() || s.equals(slist.get(0).name)))
                sa.setOpen(0);
    }
}
