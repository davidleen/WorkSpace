package com.giants3.yourreader.text;

import com.giants3.pools.ObjectPool;
import com.giants3.pools.PoolCenter;
import com.giants3.yourreader.text.elements.WordElement;
import com.xxx.reader.core.PageInfo;
import com.xxx.reader.turnner.sim.SettingContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/12/31.
 */

public class TextPageInfo extends PageInfo {


    public List<WordElement> elements=new ArrayList<>();



    public float pageHeight;











    @Override
    public void recycle()
    {
        synchronized (elements) {
            ObjectPool<WordElement> objectPool= PoolCenter.getObjectPool(WordElement.class,10000);
            objectPool.release(elements);
            elements.clear();
        }

    }
}
