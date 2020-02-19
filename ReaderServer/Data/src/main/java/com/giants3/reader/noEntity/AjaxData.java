package com.giants3.reader.noEntity;

import java.util.List;

public class AjaxData<T> {

   public  int draw;
    public int recordsTotal;
    public int recordsFiltered;
    public List<T> data;
}
