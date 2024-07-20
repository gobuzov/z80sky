package ru.z80sky.model;

import ru.z80sky.Command;
import ru.z80sky.Tools;
import ru.z80sky.c;

public class DizCommand implements Comparable<DizCommand> {
    public int start;
    public int size; // in bytes, [1..4]
    public int param = -1; // mean no param
    public int begin = -1, end = -1; // for Spannable, see ExplorerAdapter
    private String text;
    public DizCommand(int s){ start = s;}
    public void init (int b1, int b2, int b3, int b4) {
        StringBuilder sb = new StringBuilder(32);
        String adr = Tools.getNumber16(start, c.DIS_ADR);
        sb.append(adr).append(' ');
        Command tmpl = Command.getCommandByBytes(b1, b2, b4);
        if (null == tmpl) { /// mean no template, undocument nops
            size = 2;
            sb.append("nop# <").append(Integer.toHexString(b1)).append(' ').append(Integer.toHexString(b2)).append('>');
            text = sb.toString();
            return;
        }
        size = tmpl.len;
        String s = tmpl.mnem;
        int id = s.indexOf('*');
        if (-1 != s.indexOf("x+") || -1 != s.indexOf("y+")) { // -128...+ 127
            String d = "+" + b3;
            if (b3 > 127)
                d = "-" + (256 - b3);
            s = Tools.replace(s, "+*", d);
        } else if (-1 != s.indexOf("**")) {
            param = (b3 << 8) | b2;
            if (4 == size)
                param = 0xed8a == tmpl.val ? (b3 << 8) | b4 : (b4 << 8) | b3;
            String n = Tools.getNumber16(param, c.DIS_16);
            begin = adr.length() + 1 + id;
            end = begin + n.length();
            s = Tools.replace(s, id, 2, n);
        }
        id = s.indexOf('*');
        if (-1 != id) {
            if (s.startsWith("jr") || s.startsWith("djnz")) {
                if (b2 > 127)
                    b2 = b2 - 254;
                else
                    b2 += 2;
                param = start + b2;
                String n = Tools.getNumber16(param, c.DIS_16);
                begin = adr.length() + 1 + id;
                end = begin + n.length();
                s = s.substring(0, id) + n;
            } else { /// cases: in a,(*); ld xh,*; ld (ix+0),*
                int n = 4 == size ? b4 : 3 == size ? b3 : b2;
                s = Tools.replace(s, id, 1, Tools.getNumber8(n, c.DIS_8));
            }
        }
        sb.append(s);
        text = sb.toString();
    }
    public String getString(){
        return text;
    }
    public int compareTo(DizCommand dc) {
        if(this.start > dc.start)
            return 1;
        else if(this.start < dc.start)
            return -1;
        else
            return 0;
    }
}