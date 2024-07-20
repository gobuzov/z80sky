package ru.z80sky;

public class ValuesPool {
    private static ValuesPool instance;
    private ValuesPool(){}

    public static ValuesPool getInstance(){
        if (null==instance)
            instance = new ValuesPool();
        return instance;
    }
    public int get(String name){
        // todo check for var
        return c.UNDEF;
    }
}
