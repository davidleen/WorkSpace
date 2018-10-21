package com.xxx.reader.api;

/**
 * Created by davidleen29 on 2018/4/12.
 */

public interface Pageable {

    boolean canTurnPrevious();
    boolean canTurnNext();
    void turnNext();
    void turnPrevious();
}
