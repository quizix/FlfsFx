package com.dxw.flfs.ui.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by zhang on 2016-05-20.
 */
public class Reminder {

    private SimpleStringProperty when = new SimpleStringProperty("");
    private SimpleStringProperty content = new SimpleStringProperty("");
    private SimpleStringProperty tag = new SimpleStringProperty("");

    public Reminder(){
        this("","","");

    }
    public Reminder(String tag, String when, String content){
        this.setTag(tag);
        this.setWhen(when);
        this.setContent(content);
    }

    public String getContent(){
        return content.get();
    }

    public  void setContent(String content){
        this.content.set(content);
    }

    public String getWhen(){
        return when.get();
    }

    public  void setWhen(String when){
        this.when.set(when);
    }

    public String getTag() {
        return tag.get();
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }
}
