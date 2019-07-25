package com.rnmap_wb.android.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;


@Entity(indexes = {
        @Index(value = "name, createTime DESC", unique = true)
})
public class DownloadTask {

    public static  final int STATE_DOWNLOADING=0;
    public static  final int STATE_STOP=1;
    public static  final int STATE_COMPLETE=2;

    public int count;
    public int downloadedCount;
    @Id
    private Long id;


    private String name;

    private String createTime;
    private String latLngs;
    private int fromZoom;
    private int toZoom;

    /**
     * 0 下载中， 1 停止， 2 完成
     */
    private int state;

    public float percent;

    public String discribe;



@Generated(hash = 617679754)
public DownloadTask(int count, int downloadedCount, Long id, String name,
        String createTime, String latLngs, int fromZoom, int toZoom, int state,
        float percent, String discribe) {
    this.count = count;
    this.downloadedCount = downloadedCount;
    this.id = id;
    this.name = name;
    this.createTime = createTime;
    this.latLngs = latLngs;
    this.fromZoom = fromZoom;
    this.toZoom = toZoom;
    this.state = state;
    this.percent = percent;
    this.discribe = discribe;
}

@Generated(hash = 1999398913)
public DownloadTask() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getName() {
    return this.name;
}

public void setName(String name) {
    this.name = name;
}

public String getCreateTime() {
    return this.createTime;
}

public void setCreateTime(String createTime) {
    this.createTime = createTime;
}

public int getState() {
    return this.state;
}

public void setState(int state) {
    this.state = state;
}

public float getPercent() {
    return this.percent;
}

public void setPercent(float percent) {
    this.percent = percent;
}

public String getDiscribe() {
    return this.discribe;
}

public void setDiscribe(String discribe) {
    this.discribe = discribe;
}

public int getCount() {
    return this.count;
}

public void setCount(int count) {
    this.count = count;
}

public int getDownloadedCount() {
    return this.downloadedCount;
}

public void setDownloadedCount(int downloadedCount) {
    this.downloadedCount = downloadedCount;
}



public int getFromZoom() {
    return this.fromZoom;
}

public void setFromZoom(int fromZoom) {
    this.fromZoom = fromZoom;
}

public int getToZoom() {
    return this.toZoom;
}

public void setToZoom(int toZoom) {
    this.toZoom = toZoom;
}

public String getLatLngs() {
    return this.latLngs;
}

public void setLatLngs(String latLngs) {
    this.latLngs = latLngs;
}


}
