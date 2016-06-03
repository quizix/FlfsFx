/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.ms;

import java.util.Date;

/**
 * @author pronics3
 */
public class Notification {

    public Notification(){
        this.content = null;
        this.id = 0;
        this.when = new Date().getTime();
        this.flag = NotificationFlags.NOTHING;
    }

    private int id;

    private Object content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    /**
     * 什么时候
     */
    private long when;

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


}
