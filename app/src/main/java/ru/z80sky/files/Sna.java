package ru.z80sky.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Sna extends BaseFile{
/*  Offset   Size   Description
   ------------------------------------------------------------------------
0 1  byte I
1 8  word HL',DE',BC',AF
9 10 word HL,DE,BC,IY,IX
19 1 byte Interrupt (bit 2 contains IFF2, 1=EI/0=DI)
20 1 byte R
21 4 words AF,SP
25 1 byte IntMode (0=IM0/1=IM1/2=IM2)
26 1 byte BorderColor (0..7, not used by Spectrum 1.7)
27  49152  bytes  RAM dump 16384..65535
------------------------------------------------------------------------
Total: 49179 bytes

0        27 bytes SNA header (see above)
27       16Kb bytes  RAM bank 5 \
16411    16Kb bytes  RAM bank 2  } - as standard 48Kb SNA file
32795    16Kb bytes  RAM bank n / (currently paged bank)
49179    2      word   PC
49181    1 byte port 7ffd setting
49182    1 byte TR-DOS rom paged (1) or not (0)
49183    16Kb bytes remaining RAM banks in ascending order
...
------------------------------------------------------------------------
Total: 131103 or 147487 bytes */
protected int af, bc, de, hl, af2, bc2, de2, hl2, ix, iy, sp, ir;
    protected int inter, intMode, border;
    protected byte[] mem = new byte[49152];
    public void read(InputStream is) {
        try {
            int i = is.read();
            hl2 = (is.read() | (is.read() << 8));
            de2 = (is.read() | (is.read() << 8));
            bc2 = (is.read() | (is.read() << 8));
            af2 = (is.read() | (is.read() << 8));
            hl =  (is.read() | (is.read() << 8));
            de =  (is.read() | (is.read() << 8));
            bc =  (is.read() | (is.read() << 8));
            iy =  (is.read() | (is.read() << 8));
            ix =  (is.read() | (is.read() << 8));
            inter = is.read();
            //int i = is.read();
            ir = (is.read() | (i << 8));
            af = (is.read() | (is.read() << 8));
            sp = (is.read() | (is.read() << 8));
            intMode = is.read();
            border = is.read();
            is.read(mem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write(OutputStream os){
        try{
            os.write(ir>>8);
            os.write(hl2&255);os.write(hl2>>8);
            os.write(de2&255);os.write(de2>>8);
            os.write(bc2&255);os.write(bc2>>8);
            os.write(af2&255);os.write(af2>>8);
            os.write(hl&255);os.write(hl>>8);
            os.write(de&255);os.write(de>>8);
            os.write(bc&255);os.write(bc>>8);
            os.write(iy&255);os.write(iy>>8);
            os.write(ix&255);os.write(ix>>8);
            os.write(inter);
            os.write(ir&255);
            os.write(af&255);os.write(af>>8);
            os.write(sp&255);os.write(sp>>8);
            os.write(intMode);
            os.write(border);
            os.write(mem);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[] getNames(){return null;}
    public byte[] getData(int id){return mem;}
    public int getStart(int id){return 16384;}
    public boolean isMultiParts(){return false;};

    public int getType (int id){
        return 'C';
    }
}
