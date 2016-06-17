package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.mes.Device;
import com.dxw.flfs.data.models.mes.Shed;
import com.dxw.flfs.data.models.mes.Sty;
import com.dxw.flfs.data.models.mes.Warehouse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Created by zhang on 2016-05-26.
 */
public class ShedController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Shed> shedTableView;

    @FXML
    TableView<Sty> styTableView;

    @FXML
    TableView<Device> deviceTableView;

    @FXML
    TableView<Warehouse> warehouseTableView;

    @FXML
    TabPane tabPane;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<Shed> shedRepository = unitOfWork.getShedRepository();
            Collection<Shed> sheds = shedRepository.findAll();
            shedTableView.getItems().addAll(sheds);

            Platform.runLater(() -> {
                shedTableView.requestFocus();
                shedTableView.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        shedTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    switch (tabPane.getSelectionModel().getSelectedIndex()){
                        case 0:
                            styTableView.getItems().clear();
                            if(newValue != null){
                                Set<Sty> sties = newValue.getSties();
                                if( sties!=null)
                                    styTableView.getItems().addAll(sties);
                            }
                            break;
                        case 1:
                            warehouseTableView.getItems().clear();
                            if(newValue != null){
                                Set<Warehouse> warehouses = newValue.getWarehouses();
                                if( warehouses!=null)
                                    warehouseTableView.getItems().addAll(warehouses);
                            }
                            break;
                        case 2:
                            deviceTableView.getItems().clear();
                            if(newValue != null){
                                Set<Device> devices = newValue.getDevices();
                                if( devices!=null)
                                    deviceTableView.getItems().addAll(devices);
                            }
                            break;
                    }

                }
        );

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Shed shed = shedTableView.getSelectionModel().getSelectedItem();
            if(shed == null)
                return;
            switch (tabPane.getSelectionModel().getSelectedIndex()){
                case 0:
                    styTableView.getItems().clear();
                    if(newValue != null){
                        Set<Sty> sties = shed.getSties();
                        if( sties!=null)
                            styTableView.getItems().addAll(sties);
                    }
                    break;
                case 1:
                    warehouseTableView.getItems().clear();
                    if(newValue != null){
                        Set<Warehouse> warehouses = shed.getWarehouses();
                        if( warehouses!=null)
                            warehouseTableView.getItems().addAll(warehouses);
                    }
                    break;
                case 2:
                    deviceTableView.getItems().clear();
                    if(newValue != null){
                        Set<Device> devices = shed.getDevices();
                        if( devices!=null)
                            deviceTableView.getItems().addAll(devices);
                    }
                    break;
            }
        });
    }

    public void dispose() {
        if( unitOfWork != null){
            try {
                unitOfWork.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddShed(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/shedDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加猪舍");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            ShedDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();
                String address = controller.getTextFieldAddress();

                Shed shed = new Shed();
                Date now = new Date();
                shed.setModifyTime(now);
                shed.setCreateTime(now);
                shed.setCode(code);
                shed.setAddress(address);
                shed.setName(name);

                unitOfWork.begin();
                unitOfWork.getShedRepository().save(shed);
                unitOfWork.commit();

                shedTableView.getItems().add(shed);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditShed(){
        Shed shed = shedTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/shedDetail.fxml"));
            Parent root = loader.load();

            ShedDetailController controller = loader.getController();
            controller.setShed(shed);
            Stage stage = new Stage();
            stage.setTitle("修改猪舍");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();
                String address = controller.getTextFieldAddress();

                Date now = new Date();
                shed.setModifyTime(now);

                shed.setCode(code);
                shed.setAddress(address);
                shed.setName(name);
                unitOfWork.begin();
                unitOfWork.getShedRepository().save(shed);
                unitOfWork.commit();

                refreshShedTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddSty(){
        Shed shed = shedTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/styDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加栏位");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            StyDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getName();
                String code = controller.getCode();
                int no = controller.getNo();

                Sty sty = new Sty();
                Date now = new Date();
                sty.setModifyTime(now);
                sty.setCreateTime(now);
                sty.setCode(code);
                sty.setNo(no);
                sty.setName(name);

                sty.setShed(shed);

                unitOfWork.begin();
                unitOfWork.getStyRepository().save(sty);
                unitOfWork.commit();

                styTableView.getItems().add(sty);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddWarehouse(){
        Shed shed = shedTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/warehouseDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加仓库");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            WarehouseDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getName();
                String code = controller.getCode();
                int no = controller.getNo();

                Warehouse warehouse = new Warehouse();
                Date now = new Date();
                warehouse.setModifyTime(now);
                warehouse.setCreateTime(now);
                warehouse.setCode(code);
                warehouse.setNo(no);
                warehouse.setName(name);

                shed.addWarehouse(warehouse);

                unitOfWork.begin();
                unitOfWork.getShedRepository().save(shed);
                unitOfWork.commit();

                warehouseTableView.getItems().add(warehouse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddDevice(){
        Shed shed = shedTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/deviceDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加设备");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            DeviceDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getName();
                String code = controller.getCode();

                Device device = new Device();
                Date now = new Date();
                device.setModifyTime(now);
                device.setCreateTime(now);
                device.setCode(code);
                device.setName(name);

                shed.addDevice(device);

                unitOfWork.begin();
                unitOfWork.getShedRepository().save(shed);
                unitOfWork.commit();

                deviceTableView.getItems().add(device);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onEditSty(){
        Sty sty = styTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/styDetail.fxml"));
            Parent root = loader.load();

            StyDetailController controller = loader.getController();
            controller.setSty(sty);
            Stage stage = new Stage();
            stage.setTitle("修改栏位");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                String name = controller.getName();
                String code = controller.getCode();
                int no = controller.getNo();

                Date now = new Date();
                sty.setModifyTime(now);

                sty.setCode(code);
                sty.setNo(no);
                sty.setName(name);
                unitOfWork.begin();
                unitOfWork.getStyRepository().save(sty);
                unitOfWork.commit();

                refreshStyTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditWarehouse(){
        Warehouse warehouse = warehouseTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/warehouseDetail.fxml"));
            Parent root = loader.load();

            WarehouseDetailController controller = loader.getController();
            controller.setWarehouse(warehouse);
            Stage stage = new Stage();
            stage.setTitle("修改仓库");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                String name = controller.getName();
                String code = controller.getCode();
                int no = controller.getNo();

                Date now = new Date();
                warehouse.setModifyTime(now);

                warehouse.setCode(code);
                warehouse.setNo(no);
                warehouse.setName(name);
                unitOfWork.begin();
                unitOfWork.getWarehouseRepository().save(warehouse);
                unitOfWork.commit();

                refreshWarehouseTable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditDevice(){
        Device device = deviceTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/deviceDetail.fxml"));
            Parent root = loader.load();

            DeviceDetailController controller = loader.getController();
            controller.setDevice(device);
            Stage stage = new Stage();
            stage.setTitle("修改设备");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                String name = controller.getName();
                String code = controller.getCode();

                Date now = new Date();
                device.setModifyTime(now);

                device.setCode(code);
                device.setName(name);
                unitOfWork.begin();
                unitOfWork.getDeviceRepository().save(device);
                unitOfWork.commit();

                refreshDeviceTable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDeleteSty(){
        Sty sty = styTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个栏位？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        //unitOfWork.getStyRepository().delete(sty);
                        sty.getShed().getSties().remove(sty);
                        unitOfWork.getStyRepository().delete(sty);
                        unitOfWork.commit();

                        styTableView.getItems().remove(sty);

                    });
    }

    public void onDeleteWarehouse(){
        Warehouse warehouse = warehouseTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个仓库？");
        alert.setHeaderText(null);
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response ->{
                    Shed shed = warehouse.getShed();
                    unitOfWork.begin();
                    shed.removeWarehouse(warehouse);
                    unitOfWork.getShedRepository().save(shed);
                    unitOfWork.commit();

                    warehouseTableView.getItems().remove(warehouse);

                });
    }

    public void onDeleteDevice(){
        Device device = deviceTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个设备？");
        alert.setHeaderText(null);
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response ->{
                    Shed shed = device.getShed();
                    unitOfWork.begin();
                    shed.removeDevice(device);
                    unitOfWork.getShedRepository().save(shed);
                    unitOfWork.commit();

                    deviceTableView.getItems().remove(device);

                });
    }

    private void refreshShedTable() {
        shedTableView.getColumns().get(0).setVisible(false);
        shedTableView.getColumns().get(0).setVisible(true);
    }

    private void refreshStyTable(){
        styTableView.getColumns().get(0).setVisible(false);
        styTableView.getColumns().get(0).setVisible(true);
    }

    private void refreshWarehouseTable(){
        warehouseTableView.getColumns().get(0).setVisible(false);
        warehouseTableView.getColumns().get(0).setVisible(true);
    }

    private void refreshDeviceTable(){
        deviceTableView.getColumns().get(0).setVisible(false);
        deviceTableView.getColumns().get(0).setVisible(true);
    }

}
