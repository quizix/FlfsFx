package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceException;
import com.dxw.flfs.ui.model.FermentBarrelStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;

/**
 * Created by zhang on 2016-05-20.
 */
public class StatusController {
    private ToggleGroup group = new ToggleGroup();

    @FXML
    private RadioButton radioSystemStatus1;

    @FXML
    private RadioButton radioSystemStatus2;

    @FXML
    private RadioButton radioSystemStatus3;

    @FXML
    private RadioButton radioSystemStatus4;

    @FXML
    private RadioButton radioSystemStatus5;

    @FXML
    private TableView<FermentBarrelStatus> tableView;

    private final ObservableList<FermentBarrelStatus> data =
            FXCollections.observableArrayList(
                    new FermentBarrelStatus(1,""),
                    new FermentBarrelStatus(2,""),
                    new FermentBarrelStatus(3,""),
                    new FermentBarrelStatus(4,""),
                    new FermentBarrelStatus(5,""),
                    new FermentBarrelStatus(6,""),
                    new FermentBarrelStatus(7,""));
    @FXML
    public void initialize() throws ServiceException {
        radioSystemStatus1.setToggleGroup(group);
        radioSystemStatus2.setToggleGroup(group);
        radioSystemStatus3.setToggleGroup(group);
        radioSystemStatus4.setToggleGroup(group);
        radioSystemStatus5.setToggleGroup(group);
        tableView.setItems(data);
    }

}
