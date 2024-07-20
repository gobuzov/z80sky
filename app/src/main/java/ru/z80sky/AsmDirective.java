package ru.z80sky;

public class AsmDirective {
    public char type; // {Code, Data, Build}
    public String example;
    public String desc; // description
    public String val; // key

    public AsmDirective(String s){
        try {
            String[] arr = Tools.getSubstrings(s, '|');
            type = arr[0].charAt(0);
            example = arr[1];
            desc = arr[2];
            val = arr[3];
        }catch (Exception exc){exc.printStackTrace();}
    }
}
