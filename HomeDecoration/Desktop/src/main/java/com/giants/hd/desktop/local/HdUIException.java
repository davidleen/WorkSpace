package com.giants.hd.desktop.local;

import com.giants3.hd.exception.HdException;

import java.awt.*;

/**
 *
 */
public class HdUIException extends HdException {




    public Component component;



    public static HdUIException create(Component component,String message
    )
    {

        HdUIException hdException= new HdUIException();
        hdException.errorCode=-1;
        hdException.message=message;
        hdException.component=component;
        return hdException;
    }
}
