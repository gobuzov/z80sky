package ru.z80sky;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class AsmManager {
    private static AsmManager instance;
    private Hashtable<String, AsmDirective> table = new Hashtable ();
    private String[] ckeys;

    public static AsmManager getInstance(){
        if (null==instance)
            instance = new AsmManager();
        return instance;
    }
    public ArrayList<AsmDirective> find(String s) {
        ArrayList<AsmDirective> result = new ArrayList<>();
        if (0 != s.length()) {
            int id = Arrays.binarySearch(ckeys, s);
            if (id<0)
                id = -id - 1;
            if (id < ckeys.length) { // not last ?
                if (s.equals(ckeys[id])) { // exact value ?
                    result.add(table.get(s));
                    return result;
                }
                for (int i = id; i < ckeys.length; i++) {
                    if (ckeys[i].startsWith(s))
                        result.add(table.get(ckeys[i]));
                    else
                        return result;
                }
            }
        }
        return result;
    }
    private AsmManager() {
        ckeys = new String[asmString.length];
        for (int i=asmString.length-1; i>=0; i--) {
            AsmDirective adir = new AsmDirective(asmString[i]);
            table.put(adir.val, adir);
            ckeys[i] = adir.val;
        }
        Arrays.sort(ckeys);
    }
    static String[] asmString = { // {Code, Data, Build}
            "C|include \"f.asm\"|Include code file as text to be assembled.|include",
            "D|db <n>[,<n2>...]|	Define Byte	Allocates 1 byte in memory set to <n>.|db",
            "D|dw <n>[,<n2>...]|Define Word	Allocates 2 bytes in memory set to <n>.|dw",
            "D|dd <n>[,<n2>...]|Define Double Word Allocates 4 bytes in memory set to <n>.|dd",
            "D|ds <c>[,<n>]|Define Space Allocates <c> bytes in memory, all set to <n> or 0.|ds",
            "D|incbin \"f.ext\"|Include binary file	Include file as bytes.|incbin",
            "B|display <txt>[,<txt>...]|Display text.Print some text to the build output.|display",
            "B|savebin \"f.bin\",<s>,<e>|Save the assembled binary code starting from <s> and ending at <e>.|savebin",
            "B|savesna \"f.sna\",<s>|Save the assembled snapshot starting from <s>.|savesna",
            "C|IFDEF <L>|If the label <L> is defined then do the following until ELSE or ENDIF.|IFDEF",
            "C|IFNDEF <L>|If the label <L> isn't defined then do the following until ELSE or ENDIF.|IFNDEF",
            "B|org <a>|Origination.Tell the assembler to place the code at this address.|org",
    };
}