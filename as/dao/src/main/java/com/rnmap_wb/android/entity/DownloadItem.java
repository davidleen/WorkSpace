package com.rnmap_wb.android.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 下载item
 */
@Entity
public class DownloadItem {


    @Id
    private Long id;

    private String name;
    private int tileX;
    private int tileY;
    private int tileZ;

    private Long taskId;


    private String url;

    private String downloadFilePath;

    /**
     * 状态 0 未下载  1 下载中  2 下载完成
     */
    private int state;

    @Generated(hash = 292662926)
    public DownloadItem(Long id, String name, int tileX, int tileY, int tileZ,
            Long taskId, String url, String downloadFilePath, int state) {
        this.id = id;
        this.name = name;
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileZ = tileZ;
        this.taskId = taskId;
        this.url = url;
        this.downloadFilePath = downloadFilePath;
        this.state = state;
    }

    @Generated(hash = 187637005)
    public DownloadItem() {
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

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadFilePath() {
        return this.downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }

    public int getTileX() {
        return this.tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return this.tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getTileZ() {
        return this.tileZ;
    }

    public void setTileZ(int tileZ) {
        this.tileZ = tileZ;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }





}
