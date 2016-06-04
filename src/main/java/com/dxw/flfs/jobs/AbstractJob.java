/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.common.messages.Message;
import com.dxw.common.messages.MessageBus;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import org.quartz.Job;

/**
 *
 * @author pronics3
 */
public abstract class AbstractJob implements Job {

    MessageBus notificationManager;

    public AbstractJob() {
        this.notificationManager = (MessageBus) ServiceRegistryImpl.getInstance()
                .getService(Services.NOTIFICATION_MANAGER);;
    }
    
    protected void notify(String message){
        if(notificationManager != null){
            Message n = new Message();
            n.setContent(message);
            n.setWhen( System.currentTimeMillis());
            notificationManager.notify("JOB", n);
        }
    }

    protected void notifyData(int flag, Object data){
        if(notificationManager != null){
            Message n = new Message();
            n.setFlag(flag);
            n.setContent(data);
            n.setWhen( System.currentTimeMillis());
            notificationManager.notify("DATA", n);
        }
    }

}
