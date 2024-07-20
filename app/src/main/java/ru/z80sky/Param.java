package ru.z80sky;

public class Param {
    boolean brk = false;
    String orig, vis = null; // original (need save ???), visible
    int iv = c.UNDEF;

    public Param(String s){
        orig = s;
        init(s);
    }
    public static Param create(String s){
        Param p = new Param(s);
        if (c.UNDEF==p.iv) {
            int val = ValuesPool.getInstance().get(s);
            if (c.UNDEF!=val) {
                p.iv = val;
            }
        }
        return p;
    }
    public void init(String s){
        int len = s.length();
        if (len>0 && '('==s.charAt(0)){
            brk = true;
            s = s.substring(1).trim();
            len = s.length();
            if (len>0 && ')'==s.charAt(len-1)){
                s = s.substring(0, len-1).trim();
                len = s.length();
            }
        }
        vis = s;
        if ((1==len && -1!="abcdehl".indexOf(s.charAt(0))) || // these params not hex numbers
            (2==len && 0=="afbcde".indexOf(s)%2)){
            return;
        }
        if (brk && (s.startsWith("ix") || s.startsWith("iy")) && (-1!=s.indexOf('+')||(-1!=s.indexOf('-')))){
            // todo init index distance
        }
        Integer v = Tools.getNumber(s); //
        if (null!=v)
            iv = v.hashCode();
    }
    public String forTemplate(String add){
        StringBuilder sb = new StringBuilder();
        if (brk)
            sb.append('(');
        if (iv!=c.UNDEF || "".equals(vis))// "" for case (,a
            sb.append(add);
        else
            sb.append(vis);
        if (brk)
            sb.append(')');
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (brk)
            sb.append('(');
        sb.append(vis);
        if (brk)
            sb.append(')');
        return sb.toString();
    }
}
