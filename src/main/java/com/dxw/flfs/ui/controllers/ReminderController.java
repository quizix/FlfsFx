package com.dxw.flfs.ui.controllers;

import com.dxw.common.messages.*;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.ui.model.Reminder;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

/**
 * Created by zhang on 2016-05-20.
 */
public class ReminderController {
    @FXML
    private TableView<Reminder> tableView;

    public ReminderController(){

    }
    public void addReminder(Reminder reminder){
        ObservableList<Reminder> items = tableView.getItems();
        items.add( reminder);
    }

    public void removeAll(){
        ObservableList<Reminder> items = tableView.getItems();
        items.clear();
    }

    private MessageBus notificationManger;

    @FXML
    public void initialize() throws ServiceException {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        notificationManger = (MessageBus) registry.getService(Services.NOTIFICATION_MANAGER);

        if (notificationManger != null) {
            notificationManger.addReceiver((String tag, Message notification) -> {
                onNotify(tag, notification);
            });
        }


    }

    private void onNotify(String tag, Message notification) {
        if (tag.equals(MessageTags.Remind)) {
            ObservableList<Reminder> items = tableView.getItems();

            String datetime= TimeUtil.formatDateTime( new Date(notification.getWhen()));
            String content = notification.getContent().toString();

            items.add( new Reminder(tag,datetime,content));
        }

    }

    @FXML
    public void onPigletPlan(){
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
}
