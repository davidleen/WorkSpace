package com.rnmap_wb.android.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MessageState {


    @Id
    public Long id;
    public  String messageId;
    public boolean read;
    @Generated(hash = 723702105)
    public MessageState(Long id, String messageId, boolean read) {
        this.id = id;
        this.messageId = messageId;
        this.read = read;
    }
    @Generated(hash = 1584660776)
    public MessageState() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMessageId() {
        return this.messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public boolean getRead() {
        return this.read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }



}