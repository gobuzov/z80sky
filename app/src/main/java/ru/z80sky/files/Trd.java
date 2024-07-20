package ru.z80sky.files;

import java.io.InputStream;

public class Trd extends BaseFile{ // todo getType remake
    public boolean isMultiParts(){return true;}
    public void read (InputStream is){};
    public String[] getNames(){
        return null;
    };
    public byte[] getData(int id){
        return null;
    }
    public int getStart(int id){
        return 0;
    };
    public int getType (int id){
        return 'C';
    }
}
