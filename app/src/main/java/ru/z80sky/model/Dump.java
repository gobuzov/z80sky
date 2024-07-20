package ru.z80sky.model;

import ru.z80sky.Tools;

public class Dump implements MyContentProvider {
    public final static int SZ = 8;
    byte[] data;
    boolean[] checked;
    int start;
    int cnt;
    public Dump(byte[] data, int start){
        this.data = data;
        this.start = start;
        cnt = data.length/SZ;
        if (0!=data.length%SZ)
            cnt++;
        checked = new boolean[cnt];
    }
    public void changeStart(int start){this.start = start;}
    public int getLinkTo(int pos){return -1;}// not used
    public String getString(int pos){
        StringBuffer sb = new StringBuffer();
        int begin = pos * SZ;
        int max = begin + SZ;
        if (max>data.length)
            max = data.length;
        sb.append(Tools.getHex16(start+begin)).append("  ");
        for (int i=begin; i<max; i++){
            if (i<data.length){
                int b = data[i]&255;
                String hx = Integer.toHexString(b);
                if (b<16)
                    sb.append('0');
                sb.append(hx).append(' ');
            }else sb.append("  ");
        }
        sb.append(" ");
        for (int i=begin; i<max; i++){
            if (i<data.length){
                int b = data[i]&255;
                char ch = '.';
                if (b>=32 && b<128)
                    ch = (char)b;
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    public int getBeginSelect(int pos){return -1;}// mean no selection
    public int getEndSelect(int pos) {return -1;}
    public boolean isChecked(int pos){return checked[pos];}
    public void setCheck(int pos, boolean b){
        checked[pos]=b;
    }
    public String getChecked(){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<cnt; i++)
            if (isChecked(i))
                sb.append(getString(i)).append('\n');
        return sb.toString();
    }
    public int getCnt(){
        return cnt;
    }
}