package com.dxw.flfs.ui.controllers.wizards;

import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Device;
import com.dxw.flfs.data.models.erp.Shed;
import com.dxw.flfs.data.models.mes.Site;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zhang on 2016-05-29.
 */
public class ChooseDevicesController {

    private Stage stage;
    private UnitOfWork unitOfWork;

    @FXML
    private TableView<Device> deviceTableView;

    @FXML
    private TableView<Shed> tableViewShed;

    private Set<Device> devices;

    public Set<Device> getSelectedDevices(){
        return devices;
    }

    @FXML
    public void initialize(){
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private boolean dialogResult;
    public void onOk(){
        dialogResult = true;
        devices = new HashSet<>();
        ObservableList<Device> selectedItems =
                deviceTableView.getItems();

        devices.addAll(selectedItems.stream().filter(device -> device.getChecked()).collect(Collectors.toList()));

        this.close();
    }

    public void onCancel(){
        dialogResult = false;
        this.close();
    }

    private void close(){
        if( stage != null)
            stage.close();

    }

    public boolean isDialogResult() {
        return dialogResult;
    }

    public void setUnitOfWork(UnitOfWork unitOfWork){
        if(this.unitOfWork != null)
            return;

        this.unitOfWork = unitOfWork;

        try{
            unitOfWork.begin();
            DefaultGenericRepository<Shed> shedRepository = unitOfWork.getShedRepository();
            Collection<Shed> sheds = shedRepository.findAll();
            tableViewShed.getItems().addAll(sheds);
            unitOfWork.commit();

            Platform.runLater(() -> {
                tableViewShed.requestFocus();
                tableViewShed.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableViewShed.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    deviceTableView.getItems().clear();
                    if(newValue != null){
                        unitOfWork.begin();
                        Set<Device> devices = newValue.getDevices();
                        DefaultGenericRepository<Site> siteRepository = unitOfWork.getSiteRepository();
                        Site site = siteRepository.findByNaturalId(FlfsApp.getContext().getSiteCode());
                        unitOfWork.commit();

                        if( devices!=null)
                            deviceTableView.getItems().addAll(
                                    devices.stream()
                                            .filter(device-> !site.getDevices().contains(device))
                                            .map(device->{device.setChecked(false); return device;})
                                            .collect(Collectors.toSet())

                            );
                    }
                }
        );
    }
}
