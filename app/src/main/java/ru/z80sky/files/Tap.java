package ru.z80sky.files;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Tap extends BaseFile{ // todo Number, Character arrays
    ArrayList<TapBlock> list = new ArrayList<>();
    public void read (InputStream is){
        TapBlock.id = 0;
        try {
            while (true){
                int low = is.read();
                if (-1==low)
                    break;
                TapBlock tb = new TapBlock();
                tb.read(is, low);
                list.add(tb);
            }
            is.close();
            for (int i=0; i<list.size(); i++)
                System.out.println(list.get(i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[] getNames(){
        String[] result = new String[list.size()];
        for (int i=0; i<list.size(); i++)
            result[i] = list.get(i).toString();
        return result;
    }
    public byte[] getData(int id){ return list.get(id).data;}
    public int getStart(int id){ return list.get(id).par1;}
    public boolean isMultiParts(){return true;}

    public int getType (int id){
        return list.get(id).isBasic() ? 'B' : 'C';
    }
}
class TapBlock{
    static int id = 0;
    int type=5, len, par1, par2; // 5 mean noheader block
    String name = "nameless";
    byte[] data;
    public void read (InputStream is, int i) throws IOException{
        len = (i|(is.read()<<8))-2; // - flag - checkbyte // for header always 19
        int flag = is.read();
        if (0==flag){ // header
            type = is.read();
            data = new byte[10];
            is.read(data);
            name = new String(data);
            len = is.read()|(is.read()<<8);
            par1 = is.read()|(is.read()<<8);
            par2 = is.read()|(is.read()<<8);
            for (i=0; i<3; i++)
                is.read(); // skip checkbyte of header, len of block,
            flag = is.read();// read flag, ff indeed
        }else{
            name = "nameless".concat(Integer.toString(id++));
        }
        data = new byte[len];
        is.read(data);
        for (i=0; i<len; i++)
            flag ^= data[i]&255;
        if (flag!=(is.read()&255))
            throw new IOException("Checkbyte is wrong");
    }
    public boolean isBasic(){ return 0==type;}
    public boolean isCode(){ return 3==type;}
    public String getName(){ return name; }
    public int getLength(){ return len; };
    public int getStart(){ return par1; };
    public String toString(){
        return getName() + (isBasic()?" B ": " C ") + par1 +" "+len;
    }
}
