package com.giants.hd.desktop.interf;

/**
 *  操作接口  基本上 CRUD
 */
public interface Operatable<T> {



    public void delete(T data);
    public void add(T data);

    public void update(T data);
}
