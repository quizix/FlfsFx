package com.dxw.flfs.ui.controllers;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.erp.Privilege;
import com.dxw.flfs.data.models.erp.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by zhang on 2016-05-26.
 */
public class UserController {

    private HibernateService hibernateService;

    private UnitOfWork unitOfWork;

    @FXML
    TableView<User> userTableView;

    @FXML
    TableView<Privilege> privilegeTableView;

    @FXML
    public void initialize(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        unitOfWork = new UnitOfWork(hibernateService.getSession());

        try{
            DefaultGenericRepository<User> userRepository = unitOfWork.getUserRepository();
            Collection<User> sheds = userRepository.findAll();
            userTableView.getItems().addAll(sheds);

            Platform.runLater(() -> {
                userTableView.requestFocus();
                userTableView.getSelectionModel().select(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        userTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    privilegeTableView.getItems().clear();
                    if(newValue != null){
                        /*Set<Sty> sties = newValue.getSties();
                        if( sties!=null)
                            privilegeTableView.getItems().addAll(sties);*/
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

    public void onAddUser(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/userDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加用户");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            UserDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();
                String password = controller.getTextFieldPassword();

                User user = new User();
                Date now = new Date();
                user.setModifyTime(now);
                user.setCreateTime(now);
                user.setCode(code);
                user.setName(name);
                user.setPassword(password);

                unitOfWork.begin();
                unitOfWork.getUserRepository().save(user);
                unitOfWork.commit();

                userTableView.getItems().add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditUser(){
        User user = userTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/userDetail.fxml"));
            Parent root = loader.load();

            UserDetailController controller = loader.getController();
            controller.setUser(user);
            Stage stage = new Stage();
            stage.setTitle("修改用户");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            if( controller.isDialogResult()){
                String name = controller.getTextFieldName();
                String code = controller.getTextFieldCode();
                String password = controller.getTextFieldPassword();

                Date now = new Date();
                user.setModifyTime(now);

                user.setCode(code);
                user.setPassword(password);
                user.setName(name);
                unitOfWork.begin();
                unitOfWork.getUserRepository().save(user);
                unitOfWork.commit();

                refreshShedTable();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onDeleteUser(){

    }

    public void onAddSty(){
        /*Shed shed = userTableView.getSelectionModel().getSelectedItem();

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

                privilegeTableView.getItems().add(sty);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void onEditSty(){
        /*Sty sty = privilegeTableView.getSelectionModel().getSelectedItem();

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
        }*/
    }

    public void onDeleteSty(){
       /* Sty sty = privilegeTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个栏位？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        //unitOfWork.getStyRepository().delete(sty);
                        sty.getShed().getSties().remove(sty);
                        unitOfWork.getStyRepository().delete(sty);
                        unitOfWork.commit();

                        privilegeTableView.getItems().remove(sty);

                    });*/
    }

    private void refreshShedTable() {
        userTableView.getColumns().get(0).setVisible(false);
        userTableView.getColumns().get(0).setVisible(true);
    }

    private void refreshStyTable(){
        privilegeTableView.getColumns().get(0).setVisible(false);
        privilegeTableView.getColumns().get(0).setVisible(true);
    }

}
