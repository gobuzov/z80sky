package ru.z80sky;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Build;
/**
 * Главный класс приложения
 * 21.08.2020. Russia, Sharapova Okhota
 * @author arkady_gobuzov
 * https://question-it.com/questions/840231/kljuch-razvertyvanija-i-zagruzki-dlja-google-play-kak-ispolzovat-ih-s-cordova
 * https://question-it.com/questions/7527280/podpisanie-apk-s-pomoschju-kljucha-zagruzki-predostavlennogo-google-play
 * https://prishanmaduka.medium.com/generate-hash-keys-properly-for-android-sms-retriever-api-c26b9be42ddc
 * https://support.google.com/googleplay/android-developer/answer/9842756
 */
public class App extends Application {
    //todo push **, nextreg errors
    private static App instance;
    /**
     * текущая активити, та что сейчас на экране
     */
    private static Activity currentActivity;
    /**
     * для хранения переменных приложения
     */
    private SharedPreferences mPrefs;
    /**
     * для хранения переменных приложения
     */
    private SharedPreferences.Editor ed;
    public String mnfc;
    //
    public App() {
        super();
        instance = this;
        System.out.println("*** App start");
        System.out.println("*** todo remove MANUFACTURER"); // LGE, Google
        mnfc = Build.MANUFACTURER;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*if (LeakCanary.isInAnalyzerProcess(this)) {return;}
        LeakCanary.install(this);*/

        System.out.println("App.onCreate*");
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ed = mPrefs.edit();
//        MyBaze.getInstance(this);
    }
    /**
     * сохранить строку под ключом в хранилище
     * @param key ключ
     * @param string значение
     */
    public void putString(String key, String string) {
        if (null==string)
            ed.remove(key);
        else
            ed.putString(key, string);
        ed.commit();
    }
    /**
     * Взять строку из хранилища
     * @param key название ключа
     * @param def значение по умолчанию
     * @return
     */
    public String getString(String key, String def){
        return mPrefs.getString(key, def);
    }
    /**
     * сохранить число под ключом в хранилище
     * @param key ключ
     * @param i значение
     */
    public void putInt(String key, int i) {
        if (-1==i)
            ed.remove(key);
        else
            ed.putInt(key, i);
        ed.commit();
    }
    /**
     * Взять число int из хранилища
     * @param key название ключа
     * @param def значение по умолчанию
     * @return
     */
    public int getInt(String key, int def){
        return mPrefs.getInt(key, def);
    }
    /**
     * установить Активити текущим
     * @param activity
     */
    public void setCurrentActivity(Activity activity){currentActivity = activity;}
    /**
     * получить текущую Активность
     * @return
     */
    public Activity getCurrentActivity(){return currentActivity;}
    /**
     * Проверить - это текущая Активность?
     * @param activity параметр
     * @return
     */
    public boolean isCurrentActivity(Activity activity){
        return (currentActivity!=null & currentActivity.equals(activity));
    }
}