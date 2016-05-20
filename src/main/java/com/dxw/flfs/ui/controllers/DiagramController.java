package com.dxw.flfs.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.io.IOException;

/**
 * Created by zhang on 2016-05-20.
 */
public class DiagramController extends VBox {
    @FXML
    private WebView webView;

    public  DiagramController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ui/diagram.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
