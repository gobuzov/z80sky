package ru.z80sky.files;

import java.io.InputStream;

public abstract class BaseFile {
    public abstract void read (InputStream is);
    public abstract String[] getNames();
    public abstract byte[] getData(int id);
    public abstract int getStart(int id);
    public abstract boolean isMultiParts();
    public abstract int getType (int id);
}
