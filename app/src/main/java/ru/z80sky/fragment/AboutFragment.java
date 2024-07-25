package ru.z80sky.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.z80sky.MainActivity;
import ru.z80sky.R;
import ru.z80sky.Tools;

import java.util.Hashtable;

public class AboutFragment extends BaseFragment implements View.OnClickListener{
    Hashtable<Integer, Integer> act = new Hashtable();
    public AboutFragment() {}// Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(MainActivity.orientation == Configuration.ORIENTATION_PORTRAIT ?
                R.layout.fragment_about : R.layout.fragment_about_land,
                container, false);
        v.findViewById(R.id.link1).setOnClickListener(this);act.put(R.id.link1, R.string.abt_link1);
        v.findViewById(R.id.link2).setOnClickListener(this);act.put(R.id.link2, R.string.abt_link2);
        v.findViewById(R.id.link3).setOnClickListener(this);act.put(R.id.link3, R.string.abt_link3);
        v.findViewById(R.id.link4).setOnClickListener(this);act.put(R.id.link4, R.string.abt_link4);
        v.findViewById(R.id.grup1).setOnClickListener(this);act.put(R.id.grup1, R.string.grup_link1);
        v.findViewById(R.id.grup2).setOnClickListener(this);act.put(R.id.grup2, R.string.grup_link2);
        v.findViewById(R.id.grup3).setOnClickListener(this);act.put(R.id.grup3, R.string.grup_link3);
        return v;
    }
    public void onClick(View view) {
        Tools.onlyClick(view);
        int id = view.getId();
        Integer act_id = act.get(id);
        if (null!=act_id) {
            String url = getString(act_id);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }
}
