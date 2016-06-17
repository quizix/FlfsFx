package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.data.models.erp.Feed;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by zhang on 2016-05-26.
 */
public class FeedDetailController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldCode;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonOk;

    private boolean dialogResult;
    private Feed feed;

    public void onOk(){
        dialogResult = true;
        close();
    }

    public void onCancel(){
        dialogResult = false;
        close();
    }

    private void close(){
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    public String getTextFieldName() {
        return textFieldName.getText();
    }

    public String getTextFieldCode() {
        return textFieldCode.getText();
    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;

        if(feed != null){
            this.textFieldCode.setText(feed.getCode());
            this.textFieldName.setText(feed.getName());
        }
    }
}
