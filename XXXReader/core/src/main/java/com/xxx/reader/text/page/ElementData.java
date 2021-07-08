package com.xxx.reader.text.page;

public class ElementData {





    public float x,y;


    public float width;
    public float height;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ElementData{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append('}');
        return sb.toString();
    }
}
