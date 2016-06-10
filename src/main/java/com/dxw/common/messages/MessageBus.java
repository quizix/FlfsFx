/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.messages;

import com.dxw.common.services.Service;

/**
 *
 * @author pronics3
 */
public interface MessageBus extends Service {
    void notify(String tag, Message notification);
    
    void addListener(MessageListener receiver);
    
    void removeListener(MessageListener receiver);
}
