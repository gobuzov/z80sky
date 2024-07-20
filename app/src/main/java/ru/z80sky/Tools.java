package ru.z80sky;

import ru.z80sky.listeners.waitNumber;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import static android.content.Context.MODE_PRIVATE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

/**
 * Класс с вспомогательными утилитами
 * Created by Аркадий on 18.12.2016.
 */
public class Tools {
    /**
     * Получить из assets файл как InputStream
     *
     * @param fname имя файла
     * @return InputStream
     */
    public static InputStream loadAssetStream(String fname){
        try {
            InputStream is = App.getInstance().getAssets().open(fname);
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Получить из assets файл как строку
     *
     * @param fname имя файла
     * @return string
     */
    public static String loadAssetString(String fname){
        byte[] array = getFile(fname);
        try {
            String result = new String(array, "UTF-8");
            return result;
        }catch (Exception exc){exc.printStackTrace();}
        return null;
    }

    /**
     * получить из assets файл как массив байтов
     *
     * @param fname имя файла
     * @return byte [ ]
     */
    public static byte[] getFile(String fname){
        byte[] result  = null;
        try {
            InputStream in = App.getInstance().getAssets().open(fname);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[16384];
            while (true){
                int read = in.read(buffer);
                if (-1==read)
                    break;
                baos.write(buffer, 0, read);
            }
            result = baos.toByteArray();
            baos.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * получить файл из assets как входной поток
     *
     * @param fname the fname
     * @return input stream
     */
    public static InputStream getFileStream(String fname){
        InputStream in = null;
        try {
            in = App.getInstance().getAssets().open(fname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
    /**
     * Разбить строку на подстроки с заданным разделителем
     *
     * @param str   the str
     * @param delim the delim
     * @return string [ ]
     */
    public static String[] getSubstringsWithBreaks(String str, char delim) {
        if (-1 == str.indexOf(delim))
            return new String[]{str};
        int i = 0;
        int start = 0;
        int skobki = 0;
        Vector<String> vct = new Vector<String>(4);
        int len = str.length();
        while (i != len) {
            char ch = str.charAt(i++);
            if ('(' == ch)
                skobki++;
            else if (')' == ch)
                skobki--;
            else if (delim == ch && 0 == skobki) {
                String news = (str.substring(start, i - 1)).trim();
                if (0 != news.length())
                    vct.addElement(news);
                start = i;
            }
        }
        if (start < len) {
            str = str.substring(start).trim();
            if (false == "".equals(str))
                vct.addElement(str);
        }
        String[] mas = new String[vct.size()];
        vct.copyInto(mas);
        return mas;
    }
    /**
     * Разбить строку на подстроки с заданным разделителем
     *
     * @param str   the str
     * @param delim the delim
     * @return string [ ]
     */
    public static String[] getSubstrings(String str, char delim) {
        if (-1 == str.indexOf(delim))
            return new String[]{str};
        int i = 0;
        int start = 0;
        Vector<String> vct = new Vector<String>(4);
        int len = str.length();
        while (i != len) {
            char ch = str.charAt(i++);
            if (delim == ch) {
                String news = (str.substring(start, i - 1)).trim();
                if (0 != news.length())
                    vct.addElement(news);
                start = i;
            }
        }
        if (start < len) {
            str = str.substring(start).trim();
            if (false == "".equals(str))
                vct.addElement(str);
        }
        String[] mas = new String[vct.size()];
        vct.copyInto(mas);
        return mas;
    }

    /**
     * Разбить строку на подстроки с ЗАДАННЫМИ РАЗДЕЛИТЕЛЯМИ, made 22.05.2019
     *
     * @param str    the str
     * @param delims the delims
     * @return string [ ]
     */
    public static String[] getSubstrings(String str, String delims) {
        boolean found = false;
        for (int i=0; i<delims.length(); i++){
            if (-1!=str.indexOf(delims.charAt(i)))
                found = true;
        }
        if (false==found)
            return new String[]{str};

        int i = 0;
        int start = 0;

        Vector<String> vct = new Vector<String>(4);
        int len = str.length();
        while (i != len) {
            char ch = str.charAt(i++);
            if (-1!=delims.indexOf(ch)) {
                String news = (str.substring(start, i - 1)).trim();
                if (0 != news.length())
                    vct.addElement(news);
                start = i;
            }
        }
        if (start < len) {
            str = str.substring(start).trim();
            if (false == "".equals(str))
                vct.addElement(str);
        }
        String[] mas = new String[vct.size()];
        vct.copyInto(mas);
        return mas;
    }

    /**
     * получить файл из assets как массив строк
     *
     * @param fname the fname
     * @return string [ ]
     */
    public static String[] loadAssetStrings(String fname){
        String str = loadAssetString(fname);
        return getSubstrings(str, '\n');
    }

    /**
     * проверим является ли последовательность символов допустимым E-mail
     *
     * @param target the target
     * @return boolean
     */
    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Check whether the given feature name is one of the available
     * features as returned by getSystemAvailableFeatures().
     *
     * @param ctx         the context of the single, global Application object            of the parent process. Usually returned by            {@code getApplicationContext()}
     * @param featureName feature name {@code PackageManager.XXX}
     * @return {@code true} if device is supporting the given feature
     */
    public static boolean hasFeature(Context ctx, String featureName) {
        boolean hasFeature = ctx.getPackageManager().hasSystemFeature(featureName);
        return hasFeature;
    }

    /**
     * вычислять Возраст между NOW и датой рождения
     *
     * @param bdate the bdate
     * @return int
     */
    public static int getAge(String bdate){
        if (null==bdate)
            return 0;
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            cal.setTime(sdf.parse(bdate));// all done
        }catch (Exception exc){
            exc.printStackTrace();
        }
        int age = Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }

    /**
     * переименовать внутренний файл
     *
     * @param oldname старое имя
     * @param newname новое
     */
    public static void renameInt(String oldname, String newname){
        File file =  new File(App.getInstance().getFilesDir(), oldname);
        if (file.exists()) {
            file.renameTo(new File(App.getInstance().getFilesDir(),newname));
        }
        /*file =  new File(App.getInstance().getFilesDir(),oldname);
        boolean old = file.exists();
        file =  new File(App.getInstance().getFilesDir(),newname);
        boolean newf = file.exists(); //*/
    }

    /**
     * масштабировать ВНУТРЕННЮЮ картинку до заданной ширины
     *
     * @param targetW ширина
     * @param path    путь к картинке
     * @return bitmap
     */
    public static Bitmap scalePic(int targetW, String path) {
        try {
            FileInputStream fis = App.getInstance().openFileInput(path);
            Bitmap oldBitmap = BitmapFactory.decodeStream(fis);// BitmapFactory.decodeFile(path);//
            //oldBitmap.getPixel()
            int targetH = targetW * oldBitmap.getHeight() / oldBitmap.getWidth();
            Bitmap newBitmap = Bitmap.createScaledBitmap(oldBitmap, targetW, targetH, true);
            //newBitmap = blackAndWhite(newBitmap); // comment this if no black and white
            return newBitmap;//saveBitmap(newBitmap, path);
        }catch (Exception exc){
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * масштабировать ВНЕШНЮЮ картинку до заданной ширины
     *
     * @param targetW ширина
     * @param path    путь к картинке
     * @return bitmap
     */
    public static Bitmap scalePicExt(int targetW, String path) {
        File file = new File(Environment.getExternalStorageDirectory(), path);
        Uri uri = Uri.fromFile(file);
        if (file.exists())
            return scalePic(targetW, uri);
        return null;
    }

    /**
     * Масштабировать картинку до заданной ширины по URI
     *
     * @param targetW the target w
     * @param uri     the uri
     * @return bitmap
     */
    public static Bitmap scalePic(int targetW, Uri uri) {
        try {
            InputStream imageStream = App.getInstance().getContentResolver().openInputStream(uri);
            Bitmap oldBitmap = BitmapFactory.decodeStream(imageStream);
            int targetH = targetW * oldBitmap.getHeight() / oldBitmap.getWidth();
            Bitmap newBitmap = Bitmap.createScaledBitmap(oldBitmap, targetW, targetH, true);
            //newBitmap = blackAndWhite(newBitmap); // comment this if no black and white
            return newBitmap;//saveBitmap(newBitmap, path);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * изменить размер картинки (и сохранить ее по тому же пути)
     *
     * @param targetW the target w
     * @param path    the path
     */
    public static void resizePicFile(int targetW, String path){//
        try {
            Bitmap bitmap = scalePicExt(targetW, path);
            saveBitmap(bitmap, path);
        }catch (Exception exc){exc.printStackTrace();}
    }
    /**
     * сделать картинку чернобелой
     * @param oldBitmap
     * @return
     */
    private static Bitmap blackAndWhite(Bitmap oldBitmap){
        Bitmap bmpMonochrome = Bitmap.createBitmap(oldBitmap.getWidth(), oldBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(oldBitmap, 0, 0, paint);
        return bmpMonochrome;
    }

    /**
     * сохранить картинку по заданному пути
     *
     * @param bitmap the bitmap
     * @param path   the path
     */
    public static void saveBitmap(Bitmap bitmap, String path){
        try {
            File file = new File(Environment.getExternalStorageDirectory(), path);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);

            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * только что сфотографированную картинку упакуем как Jpeg и сохраним во внутреннем хранилище
     *
     * @param uri the uri
     * @return string
     */
    public static String compressFoto(Uri uri){
        String filename = null;
        try {
            //File root = App.getInstance().getFilesDir();
            filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
            InputStream imageStream = App.getInstance().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            FileOutputStream fileOutputStream = App.getInstance().openFileOutput(filename, MODE_PRIVATE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            byte[] image = stream.toByteArray();
//            Log.d("aspix", String.valueOf(image.length));
            fileOutputStream.write(image);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    /**
     * переисать файл (картинку) во внутреннее хранилище
     *
     * @param uri      источник
     * @param filePath приемник
     */
    public static void saveFile(Uri uri, String filePath){
        try {
            InputStream imageStream = App.getInstance().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageStream.close();
            saveBitmap(bitmap, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * загрузить битмап из внутреннего хранилища
     *
     * @param filename the filename
     * @return bitmap
     */
    public static Bitmap loadIntBitmap(String filename){
        try {
            FileInputStream fis = App.getInstance().openFileInput(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            return bitmap;
        }catch (Exception exc){
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * вернуть ширину экрана
     *
     * @return int
     */
    public static int getScreenWidth(){
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * что то там нужно было, когда ListView внутри фрагмента неправильно высчитывал свою высоту
     *
     * @param listView the list view
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        setListViewHeightBasedOnChildren(listView, 0);
    }

    /**
     * что то там нужно было, когда ListView внутри фрагмента неправильно высчитывал свою высоту
     *
     * @param listView the list view
     * @param addon    the addon
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, int addon) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) +addon;
        listView.setLayoutParams(params);
    }

    /**
     * вернуть язык приложения, сейчас только русский
     *
     * @return string
     */
    public static String getLang(){//// TODO: 05.04.2017 другие языки
        return "ru";
    }
    /**
     * проверим, что баркод содержит только цифры
     *
     * @param barcode строка с баркодом
     * @return true если содержит только цифры
     */
    public static boolean checkBarcodeDigits(String barcode){
        if (null!=barcode){
            for (int i=0; i<barcode.length(); i++){
                char ch = barcode.charAt(i);
                if (ch<'0' || ch>'9')
                    return false;
            }
            return true;
        }else return false;
    }
    ////
    private static final String ALGORITHM = "SHA-1";

    /**
     * посчитать контрольную сумму файла
     *
     * @param filename the filename
     * @return string
     */
    public static String getFileSum(String filename){
        String result = "";
        try {
            // Получаем контрольную сумму для файла в виде массива байт
            final MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            final InputStream fis = App.getInstance().openFileInput(filename);
            byte[] dataBytes = new byte[1024];
            int bytesRead;
            while((bytesRead = fis.read(dataBytes)) > 0) {
                md.update(dataBytes, 0, bytesRead);
            }
            byte[] mdBytes = md.digest();
            // Переводим контрольную сумму в виде массива байт в
            // шестнадцатеричное представление
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < mdBytes.length; i++) {
                sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            result = sb.toString();
        } catch (FileNotFoundException | NoSuchAlgorithmException ex) {
            Log.e("ago", "CheckSumExample no file or algoritm");
        } catch (IOException ex) {
            Logger.getLogger("ago", "CheckSumExample io");
        }
        System.out.println("Check summa: " + result);
        return result;
    }

    /**
     * из пути вычленяем имя файла
     *
     * @param string путь
     * @return имя файла
     */
    public static String prepareFilename(String string) {
        String newFilename = string;
        String subString = "/";
        String[] array = newFilename.split(subString);
        if (array.length > 0) {
            newFilename = array[array.length - 1];
            newFilename = newFilename.replace("\"", "");
        }
        return newFilename;
    }

    /**
     * Метод для устранения двойных нажатий на элементах UI
     *
     * @param view the view
     */
    public static void onlyClick(final View view){
        view.setClickable(false);// .setEnabled(false);
        view.postDelayed(new Runnable() {
            public void run() {
                view.setClickable(true);
            }
        }, 500);
    }

    /**
     * Gets screen resolution.
     *
     * @param context the context
     * @return the screen resolution
     */
//
    public static String getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return "{" + width + "," + height + "}";
    }

    /**
     * Gets screen width.
     *
     * @param context the context
     * @return the screen width
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }
    public static Integer getNumber(String s){
        int len = s.length();
        if (0 == len)
            return null;

        char a = s.charAt(0);
        char b = len > 1 ? s.charAt(1) : ' ';
        if ('%'==a){ // binary number
            s = s.substring(1);
            return isBinString(s) ? getBinValue(s) : null; // to
        }
        if (('$' == a || '#' == a) || '0' == a && 'x' == b) { // #, $ or 0x - sign of hex number
            s = s.substring('0' == a ? 2 : 1);
            return isHexString(s) ? 0==s.length()? 0 : Integer.parseInt(s, 16) : null;
        }
        return isDecString(s) ? Integer.parseInt(s, 10) : null;
    }
    public static boolean isDecString(String s){
        boolean decimal = true;
        for (int i=s.length()-1; i>=0; i--){
            char ch = s.charAt(i);
            if (ch<'0' || ch>'9')
                decimal = false;
        }
        return decimal;
    }
    public static boolean isHexString(String s){
        for (int i=s.length()-1; i>=0; i--){
            char ch = s.charAt(i);
            if (ch<'0' || ch>'f')
                return false;
            if (ch>'9' && ch <'a')
                return false;
        }
        return true;
    }
    public static boolean isBinString(String s){
        for (int i=s.length()-1; i>=0; i--){
            char ch = s.charAt(i);
            if (ch!='0' && ch!='1')
                return false;
        }
        return true;
    }
    public static int getBinValue(String s) {
        int k = 1;
        int v = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char ch = s.charAt(i);
            if ('1' == ch)
                v += k;
            else if ('0'!=ch)
                return -1;
            k <<= 1;
        }
        return v;
    }
    public static String replace(String s, String old, String news){
        int i = s.indexOf(old);
        if (-1==i)
            return s;
        StringBuilder sb = new StringBuilder(s.substring(0, i));
        sb.append(news);
        sb.append(s.substring(i+old.length()));
        return sb.toString();
    }
    public static String replace(String s, int begin, int len, String news){
        StringBuilder sb = new StringBuilder(s.substring(0, begin));
        sb.append(news);
        sb.append(s.substring(begin+len));
        return sb.toString();
    }
    public static String getHex16(int i){
        String s = Integer.toHexString(i);
        if (i>=0x1000)
            return s;
        if (i<0x10)
            return "000".concat(s);
        if (i<0x100)
            return "00".concat(s);
        return "0".concat(s);
    }
    public static String getNumber16(int i, String key){
        int val = App.getInstance().getInt(key, 0);
        if (0==val)
            return Integer.toString(i);
        String hex = getHex16(i);
        if (1==val)
            return hex;
        if (2==val)
            return "0x".concat(hex);
        if (3==val)
            return "$".concat(hex);
        return hex.concat("h");
    }
    public static String getNumber8(int i, String key){
        int val = App.getInstance().getInt(key, 0);
        if (0==val)
            return Integer.toString(i);
        String hex = Integer.toHexString(i);
        if (1==val)
            return hex;
        if (2==val)
            return "0x".concat(hex);
        if (3==val)
            return "$".concat(hex);
        return hex.concat("h");
    }
    public static void showSnackbar(View view, String string){
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT).show();
    }
    public static void openDialog(Context ctx, String title, String right, waitNumber wm){
        View view = LayoutInflater.from(ctx).inflate(R.layout.dlg_edit_start, null);
        final EditText edit = view.findViewById(R.id.start);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ctx, R.style.AppTheme));
        builder.setView(view);
        if (null != title)
            builder.setTitle(title);
        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s = edit.getText().toString();
                try{
                    int n = Integer.parseInt(s);
                    wm.waitNumber(n);
                }catch (Exception exc){
                    exc.printStackTrace();
                }
            }
        };
        builder.setPositiveButton(right, positive);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog ad = builder.create();
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ad.hide();
            }});
        ad.show();
    }

}
