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

public class AboutFragment extends BaseFragment implements View.OnClickListener{
    public AboutFragment() {}// Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(MainActivity.orientation == Configuration.ORIENTATION_PORTRAIT ?
                R.layout.fragment_about : R.layout.fragment_about_land,
                container, false);
        v.findViewById(R.id.link1).setOnClickListener(this);
        v.findViewById(R.id.link2).setOnClickListener(this);
        v.findViewById(R.id.link3).setOnClickListener(this);
        v.findViewById(R.id.link4).setOnClickListener(this);
        return v;
    }
    public void onClick(View view) {
        Tools.onlyClick(view);
        int id = view.getId();
        String url = getString(R.id.link1==id ? R.string.abt_link1 :
                R.id.link2==id ? R.string.abt_link2:
                R.id.link3==id ? R.string.abt_link3 : R.string.abt_link4);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}