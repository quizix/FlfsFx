package com.dxw.flfs.ui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by zhang on 2016-05-21.
 */
public class FermentBarrelStatus {
    private SimpleIntegerProperty no= new SimpleIntegerProperty();
    private SimpleStringProperty status = new SimpleStringProperty("");

    public FermentBarrelStatus(){
        this.setNo(0);
        this.setStatus("");
    }

    public FermentBarrelStatus(int no, String status){
        this.setNo(no);
        this.setStatus(status);
    }

    public int getNo() {
        return no.get();
    }

    public void setNo(int no) {
        this.no.set(no);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
