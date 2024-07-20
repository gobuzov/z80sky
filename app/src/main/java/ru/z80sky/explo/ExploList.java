package ru.z80sky.explo;

import java.util.ArrayList;
import ru.z80sky.z80.AbstractSpectrum;

public class ExploList extends ArrayList{
    public final static int LIST_PART = 1024;
    public final static int LIST_PART2 = 256;

    public ExploList(AbstractSpectrum as){
        int SZ = as.getMem().length;
        for (int i=0; i<SZ; i+=LIST_PART){
            Item item = new Item(i, LIST_PART, Item.TYPE_DEF);
            add(item);
        }
    }
    // for inside RecyclerView
    public ExploList(Item it){
        for (int i=0; i<it.len; i+=LIST_PART2){
            Item item = new Item(i+it.start, LIST_PART2, Item.TYPE_DEF);
            add(item);
        }
    }
}
