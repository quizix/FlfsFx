package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.Shed;
import com.dxw.flfs.data.models.Sty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
public class ShedManagementController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<Shed> shedTableView;

    @FXML
    TableView<Sty> styTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<Shed> shedRepository = unitOfWork.getShedRepository();
            Collection<Shed> sheds = shedRepository.findAll();
            shedTableView.getItems().addAll(sheds);
        } catch (Exception e) {
            e.printStackTrace();
        }

        shedTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    styTableView.getItems().clear();
                    if(newValue != null){
                        Set<Sty> sties = newValue.getSties();
                        if( sties!=null)
                            styTableView.getItems().addAll(sties);
                    }
                }
        );
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

    public void onClickAddShed(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/shed.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加猪舍");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            ShedController controller = loader.getController();
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

    public void onClickEditShed(){
        Shed shed = shedTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/shed.fxml"));
            Parent root = loader.load();

            ShedController controller = loader.getController();
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

    public void onClickAddSty(){
        Shed shed = shedTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/sty.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加栏位");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            StyController controller = loader.getController();
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

    public void onClickEditSty(){
        Sty sty = styTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/sty.fxml"));
            Parent root = loader.load();

            StyController controller = loader.getController();
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

    public void onClickDeleteSty(){
        Sty sty = styTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个栏位？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        unitOfWork.getStyRepository().delete(sty);
                        unitOfWork.commit();

                        styTableView.getItems().remove(sty);

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

}
