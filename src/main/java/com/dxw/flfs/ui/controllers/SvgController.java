package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.communication.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Created by zhang on 2016-05-19.
 */
public class SvgController {

    @FXML
    private WebView webView;

    @FXML
    private void initialize() {
        WebEngine engine = webView.getEngine();
        webView.setContextMenuEnabled(false);
        String url = getClass().getResource("/svg/flfs.html").toExternalForm();
        engine.load(url);

        addPlcListener();

    }

    private void addPlcListener() {
        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
        delegate.addModelChangedListener(event -> Platform.runLater(() -> updateStatus(event)));
    }

    private void updateStatus(PlcModelChangedEvent event) {
        long field = event.getField();
        PlcModel model = event.getModel();
        WebEngine engine = webView.getEngine();

        if (field == PlcModelField.SYSTEM_STATUS) {
            Short status = model.getSystemStatus();

            String text = "";
            switch (status) {
                case 1:
                    text = "停机";
                    break;
                case 2:
                    text = "做料";
                    break;
                case 3:
                    text = "清洗";
                    break;
                case 4:
                    text = "紧停";
                    break;
                case 5:
                    text = "冷启动";
                    break;
            }
            engine.executeScript(
                    String.format("document.getElementById(\"textSystemStatus\").textContent = \"%s\"",
                            text)
            );

        } else if (field == PlcModelField.FERMENT_BARREL_STATUS) {
            boolean[] data = model.getFermentBarrelStatus();
            String text = "";
            for (int i = 0; i < Math.min(data.length, 7); i++) {
                text += ((data[i]) ? "满" : "空") + " ";
            }
            engine.executeScript(
                    String.format("document.getElementById(\"textFermentBarrelStatus\").textContent = \"%s\"",
                            text)
            );

        }else if (field == PlcModelField.MIXING_BARREL_STATUS) {
            Short status = model.getMixingBarrelStatus();
            String text = status == 0 ? "空闲" : "运行";

            engine.executeScript(
                    String.format("document.getElementById(\"textMixingBarrelStatus\").textContent = \"%s\"",
                            text)
            );
        } /*else if (field == PlcModelField.MATERIAL_TOWER_ALARM) {
            Boolean lowAlarm = model.getMaterialTowerLowAlarm();
            Boolean emptyAlarm = model.getMaterialTowerEmptyAlarm();

            if (lowAlarm) {
                lblMaterialTowerLow.setIcon(iconAlert);
                lblMaterialTowerLow.setText("");
            } else {
                lblMaterialTowerLow.setIcon(null);
                lblMaterialTowerLow.setText("正常");
            }

            if (emptyAlarm) {
                lblMaterialTowerEmpty.setIcon(iconAlert);
                lblMaterialTowerEmpty.setText("");
            } else {
                lblMaterialTowerEmpty.setIcon(null);
                lblMaterialTowerEmpty.setText("正常");
            }
        } else if (field == PlcModelField.FERMENT_BARREL_IN_OUT) {
                short in = model.getFermentBarrelInNo();
                short out = model.getFermentBarrelOutNo();
                lblFermentBarrelIn.setText(Short.toString(in));
                lblFermentBarrelOut.setText(Short.toString(out));
            }  else if (field == PlcModelField.PH_VALUE) {
                float ph = model.getPh();
                lblPh.setText(Float.toString(ph));
            }*/
    }
}
