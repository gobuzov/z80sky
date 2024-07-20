package ru.z80sky.model;

public interface MyContentProvider {
    public int getLinkTo(int pos);
    public String getString(int pos);
    public int getBeginSelect(int pos);
    public int getEndSelect(int pos);
    public int getCnt();
    public String getChecked();
    public boolean isChecked(int pos);
    public void setCheck(int pos, boolean b);
    public void changeStart(int start);
}
