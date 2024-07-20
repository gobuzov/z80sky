package ru.z80sky;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class Sysvar { // X38	23568	STRMS	Addresses of channels attached to streams.
    public int address;
    public int len; // length in bytes
    public char effect; // 0 - no, N - Poking the variable will have no lasting effect
    // X - The variables should not be poked because the system might crash.
    public String name;
    public String desc; // description

    public Sysvar(String s) {
        try {
            String[] arr = Tools.getSubstrings(s, '|');
            address = Integer.parseInt(arr[1]);
            effect = 0;
            char ch = arr[0].charAt(0);
            if ('X' == ch || 'N' == ch) {
                effect = ch;
                len = Integer.parseInt(arr[0].substring(1));
            } else
                len = Integer.parseInt(arr[0]);
            name = arr[2];
            desc = arr[3];
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    /// static taken from SysvarManager
    private static Hashtable<Object, Sysvar> sysvars = new Hashtable<>();
    private static int[] starts;
    private static String[] names;

    public static ArrayList<Sysvar> find(String s) {
        ArrayList<Sysvar> result = new ArrayList<>();
        if (null==starts)
            init();
        if (0 != s.length()) {
            Sysvar cc = checkNumbers(s);
            if (null != cc)
                result.add(cc);
            else {
                getSysvars(result, s);
            }
        }
        return result;
    }
    private static void getSysvars(ArrayList<Sysvar> result, String s) {
        s = s.toUpperCase();
        int id = Arrays.binarySearch(names, s);
        if (id < 0)
            id = -id - 1;
        if (id < names.length) { // not last ?
            if (s.equals(names[id])) { // exact value ?
                result.add(sysvars.get(s));
            }else
                for (int i = id; i < names.length; i++) {
                    if (names[i].startsWith(s))
                        result.add(sysvars.get(names[i]));
                    else
                        return;
                }
        }
    }
    private static Sysvar checkNumbers(String s) {
        int len = s.length();
        char a = s.charAt(0);
        char b = len > 1 ? s.charAt(1) : ' ';
        int val = -1;
        int radix = 16;
        if ('%' == a || '#' == a || '$' == a || a == '0' && b == 'x') {// check numeric values
            if ('%' == a) { // binary number
                val = Tools.getBinValue(s.substring(1));
                if (-1==val)
                    return null;
            }else {
                if (('$' == a || '#' == a) || '0' == a && 'x' == b) { // #, $ or 0x - sign of hex number
                    s = s.substring('0' == a ? 2 : 1);
                }
            }
        } else if (Tools.isDecString(s)) // if only numeric symbols, then suppose decimal number
            radix = 10;
        if (-1==val) {
            try {
                val = Integer.parseInt(s, radix);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        Sysvar sv = sysvars.get(val);
        if (null==sv) {
            int id = Arrays.binarySearch(starts, val);
            if (id < 0)
                id = -id-2;
            if (id >=0 && id < starts.length)
                sv = sysvars.get(starts[id]);
        }
        return sv;
    }
    private static void init() {
        int sz = SysvarString.length;
        names = new String[sz];
        starts = new int[sz];
        for (int i = 0; i < sz; i++) {
            Sysvar sv = new Sysvar(SysvarString[i]);
            sysvars.put(new Integer(sv.address), sv);
            sysvars.put(sv.name, sv);
            names[i] = sv.name;
            starts[i] = sv.address;
        }
        Arrays.sort(names);
        Arrays.sort(starts);
    }
    private static String[] SysvarString = {
            "N8|23552|KSTATE|Used in reading the keyboard.",
            "N1|23560|LAST K|Stores newly pressed key.",
            "1|23561|REPDEL|Time (in 50ths of a second in 60ths of a second in N. America) that a key must be held down before it repeats. This starts off at 35, but you can POKE in other values.",
            "1|23562|REPPER|Delay (in 50ths of a second in 60ths of a second in N. America) between successive repeats of a key held down: initially 5.",
            "N2|23563|DEFADD|Address of arguments of user defined function if one is being evaluated; otherwise 0.",
            "N1|23565|K DATA|Stores 2nd byte of colour controls entered from keyboard.",
            "N2|23566|TVDATA|Stores bytes of coiour, AT and TAB controls going to television.",
            "X38|23568|STRMS|Addresses of channels attached to streams.",
            "2|23606|CHARS|256 less than address of character set (which starts with space and carries on to the copyright symbol). Normally in ROM, but you can set up your own in RAM and make CHARS point to it.",
            "1|23608|RASP|Length of warning buzz.",
            "1|23609|PIP|Length of keyboard click.",
            "1|23610|ERR NR|1 less than the report code. Starts off at 255 (for 1) so PEEK 23610 gives 255.",
            "X1|23611|FLAGS|Various flags to control the BASIC system.",
            "X1|23612|TV FLAG|Flags associated with the television.",
            "X2|23613|ERR SP|Address of item on machine stack to be used as error return.",
            "N2|23615|LIST SP|Address of return address from automatic listing.",
            "N1|23617|MODE|Specifies K, L, C. E or G cursor.",
            "2|23618|NEWPPC|Line to be jumped to.",
            "1|23620|NSPPC|Statement number in line to be jumped to. Poking first NEWPPC and then NSPPC forces a jump to a specified statement in a line.",
            "2|23621|PPC|Line number of statement currently being executed.",
            "1|23623|SUBPPC|Number within line of statement being executed.",
            "1|23624|BORDCR|Border colour * 8; also contains the attributes normally used for the lower half of the screen.",
            "2|23625|E PPC|Number of current line (with program cursor).",
            "X2|23627|VARS|Address of variables.",
            "N2|23629|DEST|Address of variable in assignment.",
            "X2|23631|CHANS|Address of channel data.",
            "X2|23633|CURCHL|Address of information currently being used for input and output.",
            "X2|23635|PROG|Address of BASIC program.",
            "X2|23637|NXTLIN|Address of next line in program.",
            "X2|23639|DATADD|Address of terminator of last DATA item.",
            "X2|23641|E LINE|Address of command being typed in.",
            "2|23643|K CUR|Address of cursor.",
            "X2|23645|CH ADD|Address of the next character to be interpreted: the character after the argument of PEEK, or the NEWLINE at the end of a POKE statement.",
            "2|23647|X PTR|Address of the character after the ? marker.",
            "X2|23649|WORKSP|Address of temporary work space.",
            "X2|23651|STKBOT|Address of bottom of calculator stack.",
            "X2|23653|STKEND|Address of start of spare space.",
            "N1|23655|BREG|Calculator's b register.",
            "N2|23656|MEM|Address of area used for calculator's memory. (Usually MEMBOT, but not always.)",
            "1|23658|FLAGS2|More flags.",
            "X1|23659|DF SZ|The number of lines (including one blank line) in the lower part of the screen.",
            "2|23660|S TOP|The number of the top program line in automatic listings.",
            "2|23662|OLDPPC|Line number to which CONTINUE jumps.",
            "1|23664|OSPCC|Number within line of statement to which CONTINUE jumps.",
            "N1|23665|FLAGX|Various flags.",
            "N2|23666|STRLEN|Length of string type destination in assignment.",
            "N2|23668|T ADDR|Address of next item in syntax table (very unlikely to be useful).",
            "2|23670|SEED|The seed for RND. This is the variable that is set by RANDOMIZE.",
            "3|23672|FRAMES|3 byte (least significant first), frame counter. Incremented every 20ms.",
            "2|23675|UDG|Address of 1st user defined graphic You can change this for instance to save space by having fewer user defined graphics.",
            "1|23677|COORDS|x-coordinate of last point plotted.",
            "1|23678|COORDSY|y-coordinate of last point plotted.",
            "1|23679|P POSN|33 column number of printer position",
            "X2|23680|PR CC|Full address of next position for LPRINT to print at (in ZX printer buffer). Legal values 5B00 - 5B1F. [Not used in 128K mode or when certain peripherals are attached]",
            "2|23682|ECHO E|33 column number and 24 line number (in lower half) of end of input buffer.",
            "2|23684|DF CC|Address in display file of PRINT position.",
            "2|23686|DFCCL|Like DF CC for lower part of screen.",
            "X1|23688|S POSN|33 column number for PRINT position",
            "X1|23689|POSNY|24 line number for PRINT position.",
            "X2|23690|SPOSNL|Like S POSN for lower part",
            "1|23692|SCR CT|Counts scrolls: it is always 1 more than the number of scrolls that will be done before stopping with scroll? If you keep poking this with a number bigger than 1 (say 255), the screen will scroll on and on without asking you.",
            "1|23693|ATTR P|Permanent current colours, etc (as set up by colour statements).",
            "1|23694|MASK P|Used for transparent colours, etc. Any bit that is 1 shows that the corresponding attribute bit is taken not from ATTR P, but from what is already on the screen.",
            "N1|23695|ATTR T|Temporary current colours, etc (as set up by colour items).",
            "N1|23696|MASK T|Like MASK P, but temporary.",
            "1|23697|P FLAG|More flags.",
            "N30|23698|MEMBOT|Calculator's memory area; used to store numbers that cannot conveniently be put on the calculator stack.",
            "2|23728|NMIADD|This is the address of a user supplied NMI address which is read by the standard ROM when a peripheral activates the NMI. Probably intentionally disabled so that the effect is to perform a reset if both locations hold zero, but do nothing if the locations hold a non-zero value. Interface 1's with serial number greater than 87315 will initialize these locations to 0 and 80 to allow the RS232 \"T\" channel to use a variable line width. 23728 is the current print position and 23729 the width - default 80.",
            "2|23730|RAMTOP|Address of last byte of BASIC system area.",
            "2|23732|P RAMT|Address of last byte of physical RAM."};
}