package com.dxw.flfs.ui.controllers;

import com.dxw.common.ms.*;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.ui.model.Reminder;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

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

    private NotificationManager notificationManger;

    @FXML
    public void initialize() throws ServiceException {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        notificationManger = (NotificationManager) registry.getService(Services.NOTIFICATION_MANAGER);

        if (notificationManger != null) {
            notificationManger.addReceiver((String tag, Notification notification) -> {
                onNotify(tag, notification);
            });
        }


    }

    private void onNotify(String tag, Notification notification) {
        if (tag.equals(NotificationTags.Remind)) {
            ObservableList<Reminder> items = tableView.getItems();

            String datetime= TimeUtil.formatDateTime( new Date(notification.getWhen()));
            String content = notification.getContent().toString();

            items.add( new Reminder(tag,datetime,content));
        }

    }
}
