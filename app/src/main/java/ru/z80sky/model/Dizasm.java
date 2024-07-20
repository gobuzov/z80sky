package ru.z80sky.model;

import java.util.ArrayList;
import java.util.Collections;

import ru.z80sky.Command;
import ru.z80sky.Tools;

public class Dizasm implements MyContentProvider{
    ArrayList<DizCommand> lines = new ArrayList<>();
    byte[] data;
    boolean[] checked;
    int start;
    public Dizasm(byte[] data, int start){
        this.data = data;
        this.start = start;
        getLines();
    }
    public void changeStart(int start){
        this.start = start;
        getLines();
    }
    void getLines(){
        int i=0;
        ArrayList<DizCommand> newLines = new ArrayList<>();
        try{
            while(i<data.length) {
                DizCommand dz = new DizCommand(i+start);
                dz.init(data[i]&255, data[i+1]&255, data[i+2]&255, data[i+3]&255);
                newLines.add(dz);
                i+=dz.size;
            }
        }catch (Exception exc){
            exc.printStackTrace();
        }
        lines = newLines;
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
    public String getString(int pos){
        DizCommand dz = lines.get(pos);
        return dz.getString();
    }
    public int getLinkTo(int pos){
        DizCommand dz = lines.get(pos);
        int index = Collections.binarySearch(lines, new DizCommand(dz.param));
        System.out.println("index: "+index);
        return index;
    }
    public int getBeginSelect(int pos){ return lines.get(pos).begin;}
    public int getEndSelect(int pos) { return lines.get(pos).end;}
    public int getCnt(){
        return lines.size();
    }
}
