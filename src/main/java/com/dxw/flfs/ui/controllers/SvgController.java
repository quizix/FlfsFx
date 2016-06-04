package com.dxw.flfs.ui.controllers;

import com.dxw.common.messages.Message;
import com.dxw.common.messages.MessageFlags;
import com.dxw.common.messages.MessageBus;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.communication.*;
import com.dxw.flfs.communication.protocol.PlcDelegate;
import com.dxw.flfs.communication.protocol.PlcDelegateFactory;
import com.dxw.flfs.communication.protocol.PlcModel;
import com.dxw.flfs.communication.protocol.PlcModelField;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.Date;

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

        engine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    if (newState == State.SUCCEEDED) {
                        MessageBus manager =
                                (MessageBus) ServiceRegistryImpl.getInstance().getService(Services.NOTIFICATION_MANAGER);
                        Message n = new Message();
                        n.setFlag(MessageFlags.SOFTWARE_INITIALIZED);
                        n.setContent("");
                        n.setWhen(new Date().getTime());
                        manager.notify("System", n);
                    }
                });

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
                    String.format("setText(\'textSystemStatus\', \'%s\');",
                            text)
            );

        } else if (field == PlcModelField.FERMENT_BARREL_STATUS) {
            boolean[] data = model.getFermentBarrelStatus();
            String text = "";
            for (int i = 0; i < Math.min(data.length, 7); i++) {
                text += ((data[i]) ? "满" : "空") + " ";
            }
            engine.executeScript(
                    String.format("setText(\'textFermentBarrelStatus\', \'%s\');",
                            text)
            );

        } else {
            if (field == PlcModelField.MIXING_BARREL_STATUS) {
                Short status = model.getMixingBarrelStatus();
                String text = status == 0 ? "空闲" : "运行";

                engine.executeScript(
                        String.format("setText(\'textMixingBarrelStatus\', \'%s\');",
                                text)
                );
            } else {
                if (field == PlcModelField.MATERIAL_TOWER_ALARM) {
                    Boolean lowAlarm = model.getMaterialTowerLowAlarm();
                    Boolean emptyAlarm = model.getMaterialTowerEmptyAlarm();

                    String lowAlarmText = lowAlarm ? "料位器:报警" : "料位器:正常";
                    String emptyAlarmText = emptyAlarm ? "料位器:报警" : "料位器:正常";

                    engine.executeScript(
                            String.format("setText(\'textMaterialTowerLow\', \'%s\');"+
                                            "setText(\'textMaterialTowerEmpty\', \'%s\');",
                                    lowAlarmText,emptyAlarmText)
                    );

                } else if (field == PlcModelField.FERMENT_BARREL_IN_OUT) {
                    short in = model.getFermentBarrelInNo();
                    short out = model.getFermentBarrelOutNo();

                    String inText = "进料发酵罐号#" + in;
                    String outText = "出料发酵罐号#" +out;
                    engine.executeScript(
                            String.format("setText(\'textFermentBarrelIn\', \'%s\');"+
                                            "setText(\'textFermentBarrelOut\', \'%s\');",
                                    inText,outText)
                    );
                } else if (field == PlcModelField.PH_VALUE) {
                    float ph = model.getPh();
                    String text = String.format("PH值:%.2f",ph);
                    engine.executeScript(
                            String.format("setText(\'textPh\', \'%s\');",
                                    text)
                    );
                }
            }
        }
    }
}
