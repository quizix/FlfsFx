/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data.models.mes;

import com.dxw.flfs.data.models.erp.Shed;

import javax.persistence.*;
import java.util.Date;

/**
 * 生产指令
 *
 * @author Administrator
 */
@Entity
@Table(name="mes_production_instruction")
@Access(AccessType.FIELD)
public class ProductionInstruction{
    /**
     * 内部id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name="createTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name="modifyTime")
    private Date modifyTime;

    /**
     * 所属的猪舍
     */
    @ManyToOne
    @JoinColumn(name="shedId")
    private Shed shed;

    /**
     * 搅拌桶一桶所需水量
     */
    private float water;

    /**
     * 搅拌桶一桶所需干料量
     */
    private float dry;

    /**
     * 搅拌桶一桶所需菌量
     */
    private float bacteria;

    /**
     * 发酵桶做料量1
     */
    private float barrel1;

    /**
     * 发酵桶做料量2
     */
    private float barrel2;

    /**
     * 发酵桶做料量3
     */
    private float barrel3;

    /**
     * 发酵桶做料量4
     */
    private float barrel4;

    /**
     * 发酵桶做料量5
     */
    private float barrel5;

    /**
     * 发酵桶做料量6
     */
    private float barrel6;

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public float getDry() {
        return dry;
    }

    public void setDry(float dry) {
        this.dry = dry;
    }

    public float getBacteria() {
        return bacteria;
    }

    public void setBacteria(float bacteria) {
        this.bacteria = bacteria;
    }

    public float getBarrel1() {
        return barrel1;
    }

    public void setBarrel1(float barrel1) {
        this.barrel1 = barrel1;
    }

    public float getBarrel2() {
        return barrel2;
    }

    public void setBarrel2(float barrel2) {
        this.barrel2 = barrel2;
    }

    public float getBarrel3() {
        return barrel3;
    }

    public void setBarrel3(float barrel3) {
        this.barrel3 = barrel3;
    }

    public float getBarrel4() {
        return barrel4;
    }

    public void setBarrel4(float barrel4) {
        this.barrel4 = barrel4;
    }

    public float getBarrel5() {
        return barrel5;
    }

    public void setBarrel5(float barrel5) {
        this.barrel5 = barrel5;
    }

    public float getBarrel6() {
        return barrel6;
    }

    public void setBarrel6(float barrel6) {
        this.barrel6 = barrel6;
    }

    @Override
    public String toString() {
        return "";
    }

}
