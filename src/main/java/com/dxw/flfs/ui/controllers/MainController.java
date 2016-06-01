package com.dxw.flfs.ui.controllers;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationFlags;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationTags;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.app.SiteStatus;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.Site;
import com.dxw.flfs.jobs.JobManager;
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
    private UnitOfWork unitOfWork;


    @FXML
    public void initialize() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService) registry.getService(Services.HIBERNATE_SERVICE);
        this.unitOfWork = new UnitOfWork(hibernateService.getSession());

        NotificationManager manager =
                (NotificationManager)registry.getService(Services.NOTIFICATION_MANAGER);

        manager.addReceiver( (tag, notification)->{
            if( tag.equals("System")){
                if(notification.getFlag() == NotificationFlags.SYSTEM_INITIALIZED){
                    initCheck();
                }
            }
        });

    }

    private void updateButtons(int status) {
        if (status == SiteStatus.STOPPED) {
            this.buttonStart.setDisable(false);
            this.buttonStop.setDisable(true);
            this.buttonClean.setDisable(false);
        } else {
            this.buttonStart.setDisable(true);
            this.buttonStop.setDisable(false);
            this.buttonClean.setDisable(false);
        }
    }

    public void onClickTest() {
        // PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
        // delegate.getModel().setSystemStatus((short)2);
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();

        NotificationManager notificationManager = (NotificationManager) registry.getService(Services.NOTIFICATION_MANAGER);
        Notification n = new Notification();
        n.setContent("系统提示：请输入明天的入栏计划");
        n.setWhen(System.currentTimeMillis());
        if (notificationManager != null)
            notificationManager.notify(NotificationTags.Remind, n);
    }

    public void onClickStart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/wizard/startupWizard.fxml"));
            Parent root = loader.load();

            StartupWizardController controller = loader.getController();
            controller.setUnitOfWork(new UnitOfWork(hibernateService.getSession()));


            Stage stage = new Stage();
            stage.setTitle("启动向导");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.initOwner(null);
            controller.setStage(stage);
            stage.showAndWait();

            if (controller.isDialogResult()) {
                doStart();
                String siteCode = FlfsApp.getContext().getSiteCode();
                unitOfWork.begin();
                Site site = unitOfWork.getSiteConfigRepository().findByNaturalId(siteCode);
                site.setStatus(SiteStatus.STARTED);
                unitOfWork.commit();

                updateButtons(site.getStatus());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doStart() {
        PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
        plcProxy.start();

        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        JobManager jobManager = (JobManager) registry.getService(Services.JOB_MANAGER);
        if (jobManager != null)
            try {
                jobManager.startAll();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
    }

    public void onClickStop() {
        PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
        plcProxy.halt();

        try(UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession())){
            String siteCode = FlfsApp.getContext().getSiteCode();
            unitOfWork.begin();
            Site site = unitOfWork.getSiteConfigRepository().findByNaturalId(siteCode);
            site.setStatus(SiteStatus.STOPPED);
            unitOfWork.commit();

            updateButtons(SiteStatus.STOPPED);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
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

    public void onClickBatchManagement() {

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

        NotificationManager notificationManager = (NotificationManager) registry.getService(Services.NOTIFICATION_MANAGER);
        if (notificationManager != null) {
            Notification n = new Notification();
            n.setContent("系统提示：请输入明天的入栏计划");
            n.setWhen(System.currentTimeMillis());
            notificationManager.notify(NotificationTags.Remind, n);
        }

    }

    public void initCheck() {
        String siteCode = FlfsApp.getContext().getSiteCode();

        try {
            Site site = unitOfWork.getSiteConfigRepository().findByNaturalId(siteCode);

            int status = site.getStatus();

            updateButtons(status);

            if (status == SiteStatus.STARTED) {
                doStart();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
