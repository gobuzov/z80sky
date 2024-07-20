package ru.z80sky;

public interface c {
    boolean DEBUG = 1==1;
    String ADD = "add";
    String EDIT = "edit";
    String MODE = "mode";
    String USE_NEXT = "use_next";
    String HEX_MODE = "hex_mode";
    String HEX_UPPER = "hex_upper";
    String DIS_ADR = "dis_adr";
    String DIS_8   = "dis_8";
    String DIS_16  = "dis_16";
    String SCR_WIDTH  = "scr_width";
    String TELEGRAM_LINK = "https://play.google.com/store/apps/details?id=org.telegram.messenger";
    int UNDEF = -10000000; //
    int NEXT_BG = 0xffb0d0ff;
    int UNOFF_BG = 0xffd0d0d0;
    int NORMAL_BG = 0xfff0f0f0;
    int FRAG_COMMAND_SET = 1;
    int FRAG_SETTINGS = 2;
    int FRAG_ABOUT = 3;
    int FRAG_EXPLO = 4;
    int FRAG_ = 5;
    //
    int REQUEST_EXTERNAL_STORAGE_PERMISSIONS_CONTENT = 77;
    int REQUEST_CONTENT_CODE = 331;
}