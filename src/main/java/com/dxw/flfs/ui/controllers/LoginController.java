package com.dxw.flfs.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Created by zhang on 2016-05-30.
 */
public class LoginController {

    private Stage stage;

    @FXML
    private TextField textFieldUserName;

    @FXML
    private PasswordField passwordF;

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        textFieldUserName.requestFocus();
    }

    private Optional<ButtonType> result = Optional.of(ButtonType.CANCEL);

    @FXML
    public void onOk() {
        result = Optional.of(ButtonType.OK);
        this.close();
    }

    @FXML
    public void onCancel() {
        result = Optional.of(ButtonType.CANCEL);
        this.close();
    }

    private void close() {
        if (stage != null)
            stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Optional<ButtonType> getDialogResult() {
        return result;
    }
}
