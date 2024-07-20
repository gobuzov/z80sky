package ru.z80sky.explo;

import ru.z80sky.App;
import ru.z80sky.c;
import ru.z80sky.z80.AbstractSpectrum;

public class Item {
    public final static int TYPE_DB  = 0;
    public final static int TYPE_ASM = 1;
    public final static int TYPE_STR = 2;
    public final static int TYPE_DEF  = TYPE_DB;

    public Item (int start, int len, int type){
        this.start = start;
        this.len   = len;
        this.type  = type;
    }
    public String getMem(AbstractSpectrum as, boolean hex){
        StringBuffer sb = new StringBuffer();
        int MAX = start + len;
        for (int i=start; i<MAX; i++){
            int b = as.getMem(i);
            String hx = Integer.toHexString(b);
            if (b<16)
                sb.append('0');
            sb.append(hx).append(' ');
        }
        return sb.toString();
    }
    public String toString() {
        boolean hex = 1 == App.getInstance().getInt(c.HEX_MODE, 1);
        return hex ? "[" + Integer.toHexString(start) + " ," + Integer.toHexString(len) + "]" :
                "[" + Integer.toString(start) + " ," + Integer.toString(len) + "]";
    }

    public int address;
    public char effect; // 0 - no, N - Poking the variable will have no lasting effect
    // X - The variables should not be poked because the system might crash.
    public String name;
    public String desc; // description
    //
    public int start;
    public int len; // length in bytes
    public int type;
    public float textSize = 14f;
}
