package com.dxw.common.messages;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MessageFlags {
    public final static int NOTHING = 0X0;

    public final static int SYSTEM_STATUS = 0x1;

    public final static int MATERIAL_TOWER_STATUS = 0x10;


    public final static int MIXING_BARREL_STATUS = 0x20;

    public final static int FERMENT_BARREL_STATUS = 0x30;

    public final static int FERMENT_BARREL_ACTION = 0x31;

    public final static int FERMENT_BARREL_PH_VALUE = 0x32;



    public final static int VALVE_PUMP_STATUS = 0x40;


    public final static int SOFTWARE_INITIALIZED = 0x1000;
}
