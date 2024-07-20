package ru.z80sky.z80;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractSpectrum {
    /** The main registers, for first as header SNA */
    protected int af, bc, de, hl, af2, bc2, de2, hl2, ix, iy, sp, ir;
    protected int inter, intMode, border;
    protected int[] mem = new int[49152];
   /* 0        1      byte   I
   1        8      word   HL',DE',BC',AF'
            9        10     word   HL,DE,BC,IY,IX
   19       1      byte   Interrupt (bit 2 contains IFF2, 1=EI/0=DI)
   20       1      byte   R
   21       4      words  AF,SP
   25       1      byte   IntMode (0=IM0/1=IM1/2=IM2)
   26       1      byte   BorderColor (0..7, not used by Spectrum 1.7)
   27       49152  bytes  RAM dump 16384..65535//*/
    public int[] getMem() { return mem;}
    public void readSNA(InputStream is) throws IOException {
        int i = is.read();
        hl2 = ((is.read() << 8) | is.read());
        de2 = ((is.read() << 8) | is.read());
        bc2 = ((is.read() << 8) | is.read());
        af2 = ((is.read() << 8) | is.read());
        hl =  ((is.read() << 8) | is.read());
        de =  ((is.read() << 8) | is.read());
        bc =  ((is.read() << 8) | is.read());
        iy =  ((is.read() << 8) | is.read());
        ix =  ((is.read() << 8) | is.read());
        inter = is.read();
        ir = ((i << 8) | is.read());
        af = ((is.read() << 8) | is.read());
        sp = ((is.read() << 8) | is.read());
        intMode = is.read();
        border = is.read();
        for (i=0; i<49152; i++)
            mem[i] = is.read();
        int k = 0;
        k++;
    }
    public int getMem(int adr){
        return mem[adr];
    }
}
