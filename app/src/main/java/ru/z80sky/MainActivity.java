package ru.z80sky;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import ru.z80sky.fragment.*;
// todo: Programs, Pin Descriptor(?), ROM soubrutines, Tape Explorer, Z80 Explorer, Quiz, ... Pp_079272434535
/* todo
OK I downloaded your app. For me, the Z80 flags affected are one of the most important things
to know about in an instruction, especially CP. The text "Affects flags according to the result" is
quite vague - I would prefer to have more information available,
like here: http://z80-heaven.wikidot.com/instructions-set:cp
        Especially the "uses" section:
        Unsigned
        If A == N, then Z flag is set.
        If A != N, then Z flag is reset.
        If A < N, then C flag is set.
        If A >= N, then C flag is reset.
        Signed
        If A == N, then Z flag is set.
        If A != N, then Z flag is reset.
        If A < N, then S and P/V are different.//*/

// kill android:visible="false" in activity_main_drawer, add to manifest:
//<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    EditText edit;
    ImageView clear;
    View edit_container;
    public static int orientation = 1;
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        edit = findViewById(R.id.edit);
        clear = findViewById(R.id.clear);
        edit_container = findViewById(R.id.edit_container);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setText("");
            }
        });
        edit.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable e) {
                String s = e.toString().trim().toLowerCase();
                clear.setVisibility("".equals(s)?View.GONE : View.VISIBLE);
                if (null!=currFragment)
                    currFragment.update(s);
            }
        });
        orientation = getResources().getConfiguration().orientation;
        setFragment(fragmentId);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        App.getInstance().putInt(c.SCR_WIDTH, displayMetrics.widthPixels);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
 /*       String[] list = null;
        try{
            list = getAssets().list("asm"); // "" for root assets
            System.out.println("*** "+list.length);
        }catch (Exception ex){ex.printStackTrace();}//*/
    }
    public void setTimeDelay(int i) {
        new Handler().postDelayed(new Runnable() {
            public void run() {}
        }, (long) i);
    }
    public void closeDrawerLayout() {
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
    }
    static int fragmentId = c.FRAG_COMMAND_SET;
    BaseFragment currFragment;
    public void setFragment(int id) {
        fragmentId = id;

        BaseFragment fragment = c.FRAG_COMMAND_SET==id ?  new CommandSetFragment():
                c.FRAG_SETTINGS ==id? new SettingsFragment() :
                c.FRAG_ABOUT== id? new AboutFragment() : new ExplorerFragment();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        edit_container.setVisibility(c.FRAG_COMMAND_SET==id?View.VISIBLE : View.GONE);
        EditText et = edit_container.findViewById(R.id.edit);
        et.setHint(fragment.getTip());
        beginTransaction.replace(R.id.main_content, fragment);
        beginTransaction.commit();
        currFragment = fragment;
        if (c.FRAG_COMMAND_SET==id) {
            String str = null;// get
            edit.requestFocus();
        }
    }
    public void share(String text){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", text);
        startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
    }
    public boolean onNavigationItemSelected(MenuItem item){
        closeDrawerLayout();
        int fid = -1;
        int id = item.getItemId();
        if (R.id.nav_home == id) {
            fid = c.FRAG_COMMAND_SET;
        } else if (R.id.nav_settings==id) {
            fid = c.FRAG_SETTINGS;
        } else if (R.id.nav_about==id) {
            fid = c.FRAG_ABOUT;
        }else if (R.id.nav_explo==id) {
            fid = c.FRAG_EXPLO;
        }else {
            if (R.id.menu_share==id) {
                share(getString(R.string.share_text));
            } else if (R.id.menu_rate_the_app==id) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(getString(R.string.rate_app)));
                startActivity(intent);
            } else if (R.id.menu_telegram==id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/uectFMxYqXVhOWMy"));
                try {
                    startActivity(intent);
                }catch (android.content.ActivityNotFoundException anfe){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(c.TELEGRAM_LINK)));
                }
            }
        }
        if (-1!=fid) {
            closeDrawerLayout();
            setTimeDelay(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            if (c.FRAG_COMMAND_SET==fid){
                edit.setText("");
            }
            setFragment(fid);
        }
        return true;
    }
/// File management
    /**
     * Запуск выбора фото из файловой системы
     */
    public void chooseFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    }, c.REQUEST_EXTERNAL_STORAGE_PERMISSIONS_CONTENT);
        } else {
            mGetContent.launch("*/*");
        }
    }
    /**
     * Обработчик запроса разрешений
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

                //DialogBuilder.showNoRightsDialog(this);
               // break;
        }
    }

    /// new way
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    currFragment.setUri(uri);
                }
            });
}
