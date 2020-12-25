package com.giants.hd.desktop.local;

public class ComboItem {

    public int value;
    public String name;

    public ComboItem(int value,String name)
    {
        this.value = value;
        this.name = name;
    }

    public String toString()
    {
        return name;
    }
}
