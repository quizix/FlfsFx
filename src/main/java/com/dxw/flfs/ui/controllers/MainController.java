package com.dxw.flfs.ui.controllers;

import com.dxw.common.messages.Message;
import com.dxw.common.messages.MessageBus;
import com.dxw.common.messages.MessageFlags;
import com.dxw.common.messages.MessageTags;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.communication.protocol.*;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.mes.Site;
import com.dxw.flfs.jobs.JobManager;
import com.dxw.flfs.ui.controllers.settings.UserSettingController;
import com.dxw.flfs.ui.controllers.wizards.ConfigWizardController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.quartz.SchedulerException;

import java.io.IOException;

/**
 * Created by zhang on 2016-05-19.
 */
public class MainController {

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonStop;

    @FXML
    private Button buttonClean;

    private HibernateService hibernateService;
    //private UnitOfWork unitOfWork;

    @FXML
    public void initialize() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService) registry.getService(Services.HIBERNATE_SERVICE);
        //this.unitOfWork = new UnitOfWork(hibernateService.getSession());

        MessageBus manager =
                (MessageBus) registry.getService(Services.NOTIFICATION_MANAGER);

        manager.addListener((tag, notification) -> {
            if (tag.equals("System")) {
                if (notification.getFlag() == MessageFlags.SOFTWARE_INITIALIZED) {
                    initCheck();
                }
            }
        });

    }

    private void updateButtons(short status) {
        switch (status) {
            case PlcConsts.SYSTEM_STATUS_STARTED: {
                this.buttonStart.setDisable(true);
                this.buttonStop.setDisable(false);
                this.buttonClean.setDisable(true);
            }
            break;
            case PlcConsts.SYSTEM_STATUS_STOPPED: {
                this.buttonStart.setDisable(false);
                this.buttonStop.setDisable(true);
                this.buttonClean.setDisable(false);
            }
            break;
            case PlcConsts.SYSTEM_STATUS_EMERGENT_STOP: {
                this.buttonStart.setDisable(false);
                this.buttonStop.setDisable(true);
                this.buttonClean.setDisable(false);
            }
            break;
            case PlcConsts.SYSTEM_STATUS_CODE_STARTED: {
                this.buttonStart.setDisable(true);
                this.buttonStop.setDisable(false);
                this.buttonClean.setDisable(true);
            }
            break;
            case PlcConsts.SYSTEM_STATUS_CLEANNING: {
                this.buttonStart.setDisable(true);
                this.buttonStop.setDisable(true);
                this.buttonClean.setDisable(true);
            }
            break;
        }
    }

    public void onClickTest() {
        // PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
        // delegate.getModel().setSystemStatus((short)2);
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();

        MessageBus notificationManager = (MessageBus) registry.getService(Services.NOTIFICATION_MANAGER);
        Message n = new Message();
        n.setContent("系统提示：请输入明天的入栏计划");
        n.setWhen(System.currentTimeMillis());
        if (notificationManager != null)
            notificationManager.notify(MessageTags.Remind, n);
    }

    public void onConfigWizard() {
        try (UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession())) {
            doConfigInternal(unitOfWork);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void doConfigInternal(UnitOfWork unitOfWork) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/wizard/configWizard.fxml"));
            Parent root = loader.load();

            ConfigWizardController controller = loader.getController();
            controller.setUnitOfWork(unitOfWork);

            Stage stage = new Stage();
            stage.setTitle("配置向导——第一步：设置栏位关联");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            controller.setStage(stage);
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onClickStart() {
        try {
            doStart();
            /*String siteCode = FlfsApp.getContext().getSiteCode();
            unitOfWork.begin();
            Site site = unitOfWork.getSiteRepository().findByNaturalId(siteCode);
            site.setStatus(SiteStatus.STARTED);
            unitOfWork.commit();
            */
            //updateButtons(site.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doStart() {
        PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
        plcProxy.start();

        /*ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        JobManager jobManager = (JobManager) registry.getService(Services.JOB_MANAGER);
        if (jobManager != null)
            try {
                jobManager.startAll();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }*/
    }

    public void onClickStop() {
        PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
        plcProxy.halt();

        /*try (UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession())) {
            String siteCode = FlfsApp.getContext().getSiteCode();
            unitOfWork.begin();
            Site site = unitOfWork.getSiteRepository().findByNaturalId(siteCode);
            site.setStatus(SiteStatus.STOPPED);
            unitOfWork.commit();

            updateButtons(SiteStatus.STOPPED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }

    public void onClickClean() {
        PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
        plcProxy.clean();


    }

    public void onClickExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要退出系统吗？");
        alert.setHeaderText(null);
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    System.exit(0);
                });
    }

    public void onClickShedManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/shed.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("猪舍管理");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.setOnCloseRequest(e -> {
                ShedController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickUserManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/user.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("用户管理");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);

            stage.setOnCloseRequest(e -> {
                UserController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickPigManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/pig.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("猪种管理");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);

            stage.setOnCloseRequest(e -> {
                PigController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickMedicineManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/medicine.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("药品管理");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);

            stage.setOnCloseRequest(e -> {
                MedicineController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickFeedManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/feed.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("饲料管理");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);

            stage.setOnCloseRequest(e -> {
                FeedController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onClickBatchManagement() {

    }

    public void onClickUnitManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/unit.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("单位管理");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);

            stage.setOnCloseRequest(e -> {
                UnitController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickVendorManagement(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/vendor.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("供应商管理");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);

            stage.setOnCloseRequest(e -> {
                VendorController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickPigletPlan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/dialogs/pigletPlan.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("小猪入栏计划");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.setOnCloseRequest(e -> {
                PigletPlanController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickAbout() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/dialogs/about.fxml"));
            Stage stage = new Stage();
            stage.setTitle("关于发酵式液态料饲喂系统");
            stage.setScene(new Scene(root, 450, 320));
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
            stage.initOwner(null);
            stage.show();

            //hide this current window (if this is whant you want
            //((Node)(event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 液喂系统配置
     */
    public void onSetting(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/setting/setting.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("液喂系统设置");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.setOnCloseRequest(e -> {
                SettingController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onConfigUser(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/setting/userSetting.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("用户设置");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            stage.setOnCloseRequest(e -> {
                UserSettingController controller = loader.getController();
                controller.dispose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public void popupErrorMsg() {
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.APPLICATION_MODAL);
        Button okButton = new Button("Ok");
        okButton.setOnAction(arg0 -> myDialog.close());
        Date todayDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Scene myDialogScene = new Scene(VBoxBuilder.create()
                .children(new Text("Please Enter Validate Date \n \t "+ dateFormat.format(todayDate)), okButton)
                .spacing(30)
                .alignment(Pos.CENTER)
                .padding(new Insets(10))
                .build());

        myDialog.setScene(myDialogScene);
        myDialog.show();
    }*/

    public void onSendNotification() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();

        MessageBus notificationManager = (MessageBus) registry.getService(Services.NOTIFICATION_MANAGER);
        if (notificationManager != null) {
            Message n = new Message();
            n.setContent("系统提示：请输入明天的入栏计划");
            n.setWhen(System.currentTimeMillis());
            notificationManager.notify(MessageTags.Remind, n);
        }

    }

    public void initCheck() {

        try (UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession())) {
            String siteCode = FlfsApp.getContext().getSiteCode();
            unitOfWork.begin();
            Site site = unitOfWork.getSiteRepository().findByNaturalId(siteCode);
            unitOfWork.commit();

            if (site.getSties().size() == 0) {
                doConfigInternal(unitOfWork);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        doStartInternal();

        /*String siteCode = FlfsApp.getContext().getSiteCode();

        try {
            Site site = unitOfWork.getSiteRepository().findByNaturalId(siteCode);

            int status = site.getStatus();

            updateButtons(status);

            if (status == SiteStatus.STARTED) {
                doStart();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }

    private void doStartInternal() {
        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
        delegate.addModelChangedListener(event -> {
            long field = event.getField();
            if (field == PlcModelField.SYSTEM_STATUS) {
                PlcModel model = event.getModel();
                Short status = model.getSystemStatus();
                Platform.runLater(() -> updateButtons(status));
            }
        });
        delegate.getSystemStatus();

        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        JobManager jobManager = (JobManager) registry.getService(Services.JOB_MANAGER);
        if (jobManager != null)
            try {
                jobManager.startAll();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
    }
}
