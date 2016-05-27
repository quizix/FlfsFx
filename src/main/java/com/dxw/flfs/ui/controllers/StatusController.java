package com.dxw.flfs.ui.controllers;

import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.communication.PlcModel;
import com.dxw.flfs.communication.PlcModelField;
import com.dxw.flfs.ui.model.FermentBarrelStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private Label lblPh;

    @FXML
    private Label lblMixingBarrelStatus;

    @FXML
    private Label lblFermentBarrelIn;

    @FXML
    private Label lblFermentBarrelOut;

    @FXML
    private ImageView imageViewAlarmEmpty;

    @FXML
    private ImageView imageViewAlarmLow;

    @FXML
    private TableView<FermentBarrelStatus> tableView;

    private Image imageNormal = new Image(this.getClass()
            .getClassLoader().getResource("images/alert-green.png").toExternalForm());

    private Image imageAlert = new Image(this.getClass()
            .getClassLoader().getResource("images/alert-red.png").toExternalForm());

    private final ObservableList<FermentBarrelStatus> fermentBarrelStatus =
            FXCollections.observableArrayList(
                    new FermentBarrelStatus(1,""),
                    new FermentBarrelStatus(2,""),
                    new FermentBarrelStatus(3,""),
                    new FermentBarrelStatus(4,""),
                    new FermentBarrelStatus(5,""),
                    new FermentBarrelStatus(6,""),
                    new FermentBarrelStatus(7,""));

    NotificationManager notificationManager;
    PlcDelegate delegate;

    @FXML
    public void initialize() throws ServiceException {
        radioSystemStatus1.setToggleGroup(group);
        radioSystemStatus2.setToggleGroup(group);
        radioSystemStatus3.setToggleGroup(group);
        radioSystemStatus4.setToggleGroup(group);
        radioSystemStatus5.setToggleGroup(group);
        tableView.setItems(fermentBarrelStatus);

        ServiceRegistry r = ServiceRegistryImpl.getInstance();
        notificationManager = (NotificationManager) r.getService(Services.NOTIFICATION_MANAGER);

        delegate = PlcDelegateFactory.getPlcDelegate();
        delegate.addModelChangedListener(event -> {
            PlcModel model = event.getModel();
            long field = event.getField();
            if (field == PlcModelField.SYSTEM_STATUS) {
                Short status = model.getSystemStatus();
                switch (status) {
                    case 1:
                        radioSystemStatus1.setSelected(true);
                        break;
                    case 2:
                        radioSystemStatus2.setSelected(true);
                        break;
                    case 3:
                        radioSystemStatus3.setSelected(true);
                        break;
                    case 4:
                        radioSystemStatus4.setSelected(true);
                        break;
                    case 5:
                        radioSystemStatus5.setSelected(true);
                        break;
                }
            }
            else  if( field== PlcModelField.MATERIAL_TOWER_ALARM){
                Boolean lowAlarm = model.getMaterialTowerLowAlarm();
                Boolean emptyAlarm = model.getMaterialTowerEmptyAlarm();

                if(emptyAlarm){
                    imageViewAlarmEmpty.setImage(imageAlert);
                }
                else{
                    imageViewAlarmEmpty.setImage(imageNormal);
                }

                if(lowAlarm){
                    imageViewAlarmLow.setImage(imageAlert);
                }
                else{
                    imageViewAlarmLow.setImage(imageNormal);
                }

            }
            else if (field == PlcModelField.MIXING_BARREL_STATUS) {
                Short status = model.getMixingBarrelStatus();
                lblMixingBarrelStatus.setText(status == 0 ? "空闲" : "运行");
            }
            else  if( field== PlcModelField.FERMENT_BARREL_STATUS){
                boolean[] data = model.getFermentBarrelStatus();

                for(int i=0;i<data.length;i++){
                    if( i<data.length){
                        FermentBarrelStatus status = fermentBarrelStatus.get(i);
                        status.setStatus(data[i]?"满":"空");
                    }
                }
            }
            else if (field == PlcModelField.FERMENT_BARREL_IN_OUT) {
                short in = model.getFermentBarrelInNo();
                short out = model.getFermentBarrelOutNo();
                lblFermentBarrelIn.setText(Short.toString(in));
                lblFermentBarrelOut.setText(Short.toString(out));
            }
            else if (field == PlcModelField.PH_VALUE) {
                float ph = model.getPh();
                lblPh.setText(Float.toString(ph));
            }
        });
    }

}
