package ru.z80sky.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*The first 8 bytes of the file is the ASCII string "SINCLAIR";
the 9th byte is the number of TR-Dos files contained in the file. There then follows a 14 byte long descriptor for each file in archive:
     8 bytes - file name
     1 byte  - file type
     2 bytes - start address
     2 bytes - file length in bytes
     1 byte  - file size in sectors (each sector is 256 bytes)
After all the descriptors follows the sector data and after that a 4 bytes long simple sum (not a checksum!)
of all the previous bytes in the file. Please note that a TR-Dos file cannot be longer that 255 sectors;
each sector is 256 bytes long, so maximum file size is be 65280 bytes.*/
public class Scl extends BaseFile{ // todo Number, Character arrays
    SclBlock[] blocks;
    public void read (InputStream is){
        try {
            is.read(new byte[8]); // skip 'SINCLAIR'
            int len = is.read();
            blocks = new SclBlock[len];
            for(int i=0; i<len; i++){
                SclBlock bl = new SclBlock();
                byte[] name = new byte[8];
                is.read(name);
                bl.name = new String(name);
                bl.type = is.read();
                bl.start = is.read()|(is.read()<<8);
                bl.len = is.read()|(is.read()<<8);
                bl.sectors = is.read();
                blocks[i] = bl;
            }
            for(int i=0; i<len; i++){
                SclBlock bl = blocks[i];
                bl.data = new byte[bl.sectors<<8];
                is.read(bl.data);
            }
            is.close();
        } catch (IOException e) { e.printStackTrace();}
    }
    public String[] getNames(){
        String[] result = new String[blocks.length];
        for (int i=0; i<blocks.length; i++)
            result[i] = blocks[i].toString();
        return result;
    }
    public byte[] getData(int id){ return blocks[id].data;}
    public int getStart(int id){ return blocks[id].start;}
    public boolean isMultiParts(){return true;}
    public int getType (int id){
        return (char)(blocks[id].type);
    }
}
class SclBlock{
    int type, len, start, sectors;
    String name;
    byte[] data;

    public String toString(){
        return name + " "+(char)type+" "+start +" "+len+" bytes "+sectors+" sec.";
    }
}

