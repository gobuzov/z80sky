package ru.z80sky.fragment;

import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import ru.z80sky.R;

public class BaseFragment extends Fragment {

    public void update(String s) {} // overload in children if need
    /**
     * @return String id for top editText
     */
    public int getTip() { return R.string.hint_command;} // overload in children if need
    public void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
    public void setUri(Uri uri){

    } // overload in children if need
}
