package ru.z80sky.fragment;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import ru.z80sky.App;
import ru.z80sky.MainActivity;
import ru.z80sky.R;
import ru.z80sky.Tools;
import ru.z80sky.adapter.ExplorerAdapter;
import ru.z80sky.files.BaseFile;
import ru.z80sky.files.Scl;
import ru.z80sky.files.Sna;
import ru.z80sky.files.Tap;
import ru.z80sky.listeners.waitNumber;
import ru.z80sky.model.BasicDump;
import ru.z80sky.model.Dizasm;
import ru.z80sky.model.Dump;
import ru.z80sky.model.MyContentProvider;
import ru.z80sky.c;

public class ExplorerFragment extends BaseFragment implements View.OnClickListener, waitNumber {
    BaseFile currFile = null;
    int currId = 0;
    RecyclerView rList;
    public ExplorerFragment() {}// Required empty public constructor
    MyContentProvider myContentProvider;
    ImageButton load, type, basic, dump, code, share, about;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(MainActivity.orientation == Configuration.ORIENTATION_PORTRAIT ?
                R.layout.fragment_explo : R.layout.fragment_explo, container, false);
        load = v.findViewById(R.id.load); load.setOnClickListener(this);
        type = v.findViewById(R.id.type); type.setOnClickListener(this);
        basic = v.findViewById(R.id.basic); basic.setOnClickListener(this);
        dump = v.findViewById(R.id.dump); dump.setOnClickListener(this);
        code = v.findViewById(R.id.code); code.setOnClickListener(this);
        share = v.findViewById(R.id.share); share.setOnClickListener(this);
        about = v.findViewById(R.id.about); about.setOnClickListener(this);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        //CustomLinearLayoutManager lm = new CustomLinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rList = v.findViewById(R.id.rview);
        rList.setLayoutManager(lm);
        return v;
    }
    public int getTip() { return R.string.hint_explo;}
    public void setUri(Uri uri) {
        try {
            String path = uri.getPath();
            loadFile(path, App.getInstance().getContentResolver().openInputStream(uri));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public void loadFile(String fname, InputStream is){ // type, basic, dump, code, share
        int i = fname.lastIndexOf('.');
        if (-1 != i) {
            String ext = fname.substring(i + 1).toLowerCase();
            try {
                BaseFile bf = null;
                int resid = 0;
                if ("tap".equals(ext)) {
                    bf = new Tap();
                    resid = R.drawable.zxtape;
                } else if ("scl".equals(ext)) {
                    bf = new Scl();
                    resid = R.drawable.zxdisk;
                } else if ("sna".equals(ext)) {
                    bf = new Sna();
                    resid = R.drawable.m3snap;
                }
                if (null != bf) {
                    bf.read(is);
                    currFile = bf;
                    if (bf.isMultiParts()) {
                        showFileDialog();
                    }else {// (for sna, z80 files)
                        initData(new Dump(currFile.getData(currId), currFile.getStart(currId)));
                    }
                    type.setImageResource(resid);
                    type.setVisibility(View.VISIBLE);
                    type.setEnabled(bf.isMultiParts());
                    basic.setVisibility(View.VISIBLE);
                    dump.setVisibility(View.VISIBLE);
                    code.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                    showToast(fname);
                    int id = fname.lastIndexOf('/');
                    if (-1!=id)
                        fname = fname.substring(id+1);
                    getActivity().setTitle(fname);
                    return;
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        showToast("Unsupport file: ".concat(fname));
    }
    private void initData(MyContentProvider mcp){
        myContentProvider = mcp;
        ExplorerAdapter ea = new ExplorerAdapter(mcp);
        rList.setAdapter(ea);
    }
    void showFileDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a file");
        builder.setItems(currFile.getNames(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                currId = i;
                MyContentProvider mcp = null;
                byte[] arr = currFile.getData(i);
                int start = currFile.getStart(i);
                if ('B'==currFile.getType(i)) // todo add here scr, etc
                    mcp = new BasicDump(arr, start);
                else
                    mcp = new Dump(arr, start);
                initData(mcp);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void test(){
        String fname = "reb2.sna";
        loadFile(fname, Tools.loadAssetStream(fname));
    }
    public void showAlert(int titleId, int msgId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(titleId));  // header
        builder.setMessage(getString(msgId)); // сообщение
        builder.setPositiveButton(getString(R.string.explo_share_ok), null);
        final AlertDialog ad = builder.create();
        ad.setCancelable(false);
        ad.show();
    }
    public void onClick(View view) {
        Tools.onlyClick(view);
        int id = view.getId();
        if (R.id.load==id){
            /*if ("Google".equals(App.getInstance().mnfc))
                test();
            else*/
            ((ru.z80sky.MainActivity)getActivity()).chooseFile();
        }else if (R.id.type==id){
            if (currFile.isMultiParts())
                showFileDialog();
        }else if (R.id.basic==id){
            initData(new BasicDump(currFile.getData(currId), currFile.getStart(currId)));
        }else if (R.id.dump==id){
            initData(new Dump(currFile.getData(currId), currFile.getStart(currId)));
        }else if (R.id.code==id){
            initData(new Dizasm(currFile.getData(currId), currFile.getStart(currId)));
        }else if (R.id.share==id){
            String s = myContentProvider.getChecked();
            if (null!=s && s.length()>0)
                ((ru.z80sky.MainActivity)getActivity()).share(s);
            else{
                showAlert(R.string.explo_share_alert, R.string.explo_share_text);
            }
        }else if (R.id.about==id){
            //showAlert(R.string.explo_share_alert, R.string.explo_about);
            Tools.openDialog(getActivity(), "Start address", "Ok", this);
        }
    }
    public void waitNumber(int i){ // 24686
        if (null!=myContentProvider)
            myContentProvider.changeStart(i);
    }
}
