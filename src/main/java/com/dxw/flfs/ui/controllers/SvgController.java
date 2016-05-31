package com.dxw.flfs.ui.controllers;

import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.communication.PlcModel;
import com.dxw.flfs.communication.PlcModelField;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

        //addPlcListener();

    }

    private void addPlcListener() {
        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
        delegate.addModelChangedListener(event -> {
            long field = event.getField();
            PlcModel model = event.getModel();

            if (field == PlcModelField.SYSTEM_STATUS) {
                Short status = model.getSystemStatus();
                WebEngine engine = webView.getEngine();
                Document document = engine.getDocument();
                Element ele = document.getElementById("textSystemStatus");

                switch (status) {
                    case 1:
                        ele.setTextContent("停机");
                        break;
                    case 2:
                        ele.setTextContent("做料");
                        break;
                    case 3:
                        ele.setTextContent("清洗");
                        break;
                    case 4:
                        ele.setTextContent("紧停");
                        break;
                    case 5:
                        ele.setTextContent("冷启动");
                        break;
                }

            } /*else if (field == PlcModelField.FERMENT_BARREL_STATUS) {
                boolean[] data = model.getFermentBarrelStatus();
                String status = "";
                for (int i = 0; i < Math.min(data.length,7); i++) {
                    status += ((data[i]) ? "满" : "空") + " ";
                }
                Element ele = document.getElementById("textFermentBarrelStatus");
                ele.setTextContent(status);

            } *//*else if (field == PlcModelField.MATERIAL_TOWER_ALARM) {
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
            } else if (field == PlcModelField.MIXING_BARREL_STATUS) {
                Short status = model.getMixingBarrelStatus();
                lblMixingBarrelStatus.setText(status == 0 ? "空闲" : "运行");
            } else if (field == PlcModelField.PH_VALUE) {
                float ph = model.getPh();
                lblPh.setText(Float.toString(ph));
            }*/
            //canvas.repaint();
            //canvas.getCanvasGraphicsNode().fireGraphicsNodeChangeCompleted();

        });
    }
}
