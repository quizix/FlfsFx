/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.messages;

/**
 *
 * @author pronics3
 */
public interface MessageListener {
    void onMessage(String tag, Message notification);
}
