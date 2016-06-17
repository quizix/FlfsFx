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
                        Set<Privilege> privileges = newValue.getPrivileges();
                        if( privileges != null)
                            privilegeTableView.getItems().addAll(privileges);
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
        //删除用户
        User user = userTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个用户？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        unitOfWork.getUserRepository().delete(user);
                        unitOfWork.commit();

                        userTableView.getItems().remove(user);
                    });
    }

    public void onAddPrivilege(){
        User user = userTableView.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/privilegeDetail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("添加权限");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.showAndWait();

            PrivilegeDetailController controller = loader.getController();
            if( controller.isDialogResult()){
                String name = controller.getName();

                Privilege privilege = new Privilege();
                Date now = new Date();
                privilege.setModifyTime(now);
                privilege.setCreateTime(now);
                privilege.setModule(name);

                privilege.setUser(user);
                privilege.getUser().getPrivileges().add(privilege);

                unitOfWork.begin();
                unitOfWork.getUserRepository().save(user);
                unitOfWork.commit();

                privilegeTableView.getItems().add(privilege);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void onDeletePrivilege(){
       Privilege privilege = privilegeTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除这个权限？");
            alert.setHeaderText(null);
            alert.showAndWait().filter(response -> response == ButtonType.OK)
                    .ifPresent(response ->{
                        unitOfWork.begin();
                        privilege.getUser().getPrivileges().remove(privilege);
                        unitOfWork.getPrivilegeRepository().delete(privilege);
                        unitOfWork.commit();

                        privilegeTableView.getItems().remove(privilege);

                    });
    }

    private void refreshShedTable() {
        userTableView.getColumns().get(0).setVisible(false);
        userTableView.getColumns().get(0).setVisible(true);
    }

    private void refreshPrivilegeTable(){
        privilegeTableView.getColumns().get(0).setVisible(false);
        privilegeTableView.getColumns().get(0).setVisible(true);
    }

}
