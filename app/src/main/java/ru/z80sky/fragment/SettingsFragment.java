package ru.z80sky.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import ru.z80sky.App;
import ru.z80sky.R;
import ru.z80sky.Tools;
import ru.z80sky.c;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener ,RadioGroup.OnCheckedChangeListener{
    Switch nextSwitch, hexSwitch, hexUpSwitch;

    public SettingsFragment() {}// Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_settings, container, false);
        nextSwitch = v.findViewById(R.id.spectrum_next);
        nextSwitch.setOnCheckedChangeListener(this);
        nextSwitch.setChecked(1==App.getInstance().getInt(c.USE_NEXT, 1));
        hexSwitch = v.findViewById(R.id.hex_mode);
        hexSwitch.setOnCheckedChangeListener(this);
        hexSwitch.setChecked(1==App.getInstance().getInt(c.HEX_MODE, 1));
        hexUpSwitch = v.findViewById(R.id.hex_upper);
        hexUpSwitch.setOnCheckedChangeListener(this);
        hexUpSwitch.setChecked(1==App.getInstance().getInt(c.HEX_UPPER, 1));
        initRadioGroup(v.findViewById(R.id.dis_adr), c.DIS_ADR);
        initRadioGroup(v.findViewById(R.id.dis_8), c.DIS_8);
        initRadioGroup(v.findViewById(R.id.dis_16), c.DIS_16);
        return v;
    }
    private void initRadioGroup(RadioGroup rg, String key){
        int id = App.getInstance().getInt(key, 0);
        RadioButton rb =(RadioButton) rg.getChildAt(id);
        rb.setChecked(true);
        rg.setOnCheckedChangeListener(this);
    }
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (R.id.spectrum_next==id) {
            App.getInstance().putInt(c.USE_NEXT, isChecked ? 1 : 0);
        }else if (R.id.hex_mode==id){
            App.getInstance().putInt(c.HEX_MODE, isChecked ? 1 : 0);
        }else if (R.id.hex_upper==id){
            App.getInstance().putInt(c.HEX_UPPER, isChecked ? 1 : 0);
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup rg, int i) {
        int rbId = rg.getCheckedRadioButtonId();
        View radioButton = rg.findViewById(rbId);
        i = rg.indexOfChild(radioButton);
        if (R.id.dis_adr==rg.getId()){
            App.getInstance().putInt(c.DIS_ADR, i);
        }else if (R.id.dis_16==rg.getId()){
            App.getInstance().putInt(c.DIS_16, i);
        }else if (R.id.dis_8==rg.getId()){
            App.getInstance().putInt(c.DIS_8, i);
        }
    }
}