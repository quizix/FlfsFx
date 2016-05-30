package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * Created by zhang on 2016-05-30.
 */
public class UserController {

    @FXML
    private TableView<User> tableViewUser;

    private UnitOfWork unitOfWork;

    @FXML
    public void initialize(){

    }

    public void setUnitOfWork(UnitOfWork unitOfWork){
        if( this.unitOfWork != null)
            return;
    }



}
