package com.dxw.flfs.ui.controllers.wizards;

import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Device;
import com.dxw.flfs.data.models.mes.Site;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by zhang on 2016-05-28.
 */
public class DeviceWizardPageController {

    private UnitOfWork unitOfWork;

    @FXML
    private TableView<Device> tableView;

    @FXML
    public void initialize(){
    }

    public void setUnitOfWork(UnitOfWork unitOfWork){
        if( this.unitOfWork != null)
            return;
        this.unitOfWork = unitOfWork;

        String siteCode = FlfsApp.getContext().getSiteCode();

        try{
            DefaultGenericRepository<Site> repository = unitOfWork.getSiteRepository();
            Site site = repository.findByNaturalId(siteCode);
            Collection<Device> devices = site.getDevices();

            tableView.getItems().addAll(devices);
            tableView.getSelectionModel().setSelectionMode(
                    SelectionMode.MULTIPLE
            );

            Platform.runLater(() -> {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAssociate(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/wizard/chooseDevices.fxml"));
            Parent root = loader.load();

            ChooseDevicesController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("选择设备");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);

            controller.setUnitOfWork(this.unitOfWork);
            controller.setStage(stage);

            stage.showAndWait();

            if( controller.isDialogResult()){
                Set<Device> selectedSties = controller.getSelectedDevices();

                String siteCode = FlfsApp.getContext().getSiteCode();
                unitOfWork.begin();
                DefaultGenericRepository<Site> repository = unitOfWork.getSiteRepository();
                Site site = repository.findByNaturalId(siteCode);
                Collection<Device> devices = site.getDevices();
                devices.addAll( selectedSties);
                repository.save(site);

                unitOfWork.commit();

                tableView.getItems().addAll(selectedSties);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onUnassociate(){
        ObservableList<Device> selectedItems = tableView.getSelectionModel().getSelectedItems();

        if( selectedItems == null)
            return;

        String siteCode = FlfsApp.getContext().getSiteCode();
        try {
            unitOfWork.begin();

            DefaultGenericRepository<Site> repository = unitOfWork.getSiteRepository();
            Site site = repository.findByNaturalId(siteCode);
            site.removeDevices(selectedItems);
            unitOfWork.commit();

            tableView.getItems().removeAll(selectedItems);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
