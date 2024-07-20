package ru.z80sky.model;

import java.util.ArrayList;

public class BasicDump implements MyContentProvider {
    public final static String[] tokens = {"RND","INKEY$","PI","FN","POINT","SCREEN$","ATTR","AT","TAB","VAL$","CODE","VAL",
    "LEN","SIN","COS","TAN","ASN","ACS","ATN","LN","EXP","INT","SQR","SGN","ABS","PEEK","IN","USR","STR$","CHR$","NOT","BIN",
    "OR","AND","<=",">=","<>","LINE","THEN","TO","STEP","DEF FN","CAT","FORMAT","MOVE","ERASE","OPEN #","CLOSE #","MERGE",
    "VERIFY","BEEP","CIRCLE","INK","PAPER","FLASH","BRIGHT","INVERSE","OVER","OUT","LPRINT","LLIST","STOP","READ","DATA",
    "RESTORE","NEW","BORDER","CONTINUE","DIM","REM","FOR","GO TO","GO SUB","INPUT","LOAD","LIST","LET","PAUSE","NEXT","POKE",
    "PRINT","PLOT","RUN","SAVE","RANDOMIZE","IF","CLS","DRAW","CLEAR","RETURN","COPY"};
    ArrayList<String> lines = new ArrayList<>();
    public BasicDump(byte[] data, int start){
        getLines(data, start);
    }
    boolean[] checked;
    void getLines(byte[] data, int start){// todo show startline by '>'
        StringBuffer sb = new StringBuffer();
        int i=0;
        try{
            while(i<data.length) {
                int n = ((data[i++] & 255) << 8) | (data[i++] & 255);
                if (n>9999) // max basic line number
                    break;
                sb.append(n).append(' ');
                int size = ((data[i++] & 255) | ((data[i++] & 255) << 8));
                int begin = i;
                while (true) {
                    int b = data[i++] & 255;
                    if (0xe == b){/// basic number
                        int j = i-2;
                        i+=2; // skip 0,0
                        n = ((data[i++] & 255) | ((data[i++] & 255) << 8));
                        i++; // skip 0
                        int m = 1, pr = 0;
                        while (true){ // roll back to calc visible number
                            int dig = data[j--] & 255;
                            if (dig<'0'|dig>'9')
                                break;
                            pr +=(dig-'0')*m;
                            m*=10;
                        }
                        if (pr!=n)
                            sb.append('<').append(n).append("!>");

                    }else if (0xd == b) {
                        if (size!=i-begin)
                            sb.append(" Invalid length! Real: ").append((i-begin)).append(" Fake: ").append(size);
                        lines.add(sb.toString());
                        sb = new StringBuffer();
                        break;
                    }else {
                        if (b>=32 && b<128){ // ascii char
                            sb.append((char)b);
                        }else if (b>164){ // token
                            if (204==b)
                                sb.append(' '); // space before TO
                            sb.append(tokens[b-165]).append(' ');
                        }else { // [0..31] | [128..164]
                            sb.append('<').append(b).append(">");
                        }
                    }
                }
            }
        }catch (Exception exc){
            exc.printStackTrace();
        }
        String s = sb.toString(); /// case invalid basic file -> still
        if (0!=s.length())
            lines.add(s);
        checked = new boolean[lines.size()];
    }
    public boolean isChecked(int pos){return checked[pos];}
    public void setCheck(int pos, boolean b){
        checked[pos]=b;
    }
    public String getChecked(){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<checked.length; i++)
            if (isChecked(i))
                sb.append(getString(i)).append('\n');
        return sb.toString();
    }
    public int getLinkTo(int pos){return -1;}// not used
    public String getString(int pos){
        return lines.get(pos);
    }
    public int getBeginSelect(int pos){return -1;}// mean no selection
    public int getEndSelect(int pos) {return -1;}
    public int getCnt(){
        return lines.size();
    }
    public void changeStart(int start){}//nothing
}
