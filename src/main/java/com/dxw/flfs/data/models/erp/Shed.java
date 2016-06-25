/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data.models.erp;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 猪舍
 *
 * @author Administrator
 */
@Entity
@Table(name="erp_shed")
public class Shed{
    /**
     * 内部id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    protected Long id;

    /**
     * 创建时间
     */
    @Column(name="createTime")
    protected Date createTime;

    /**
     * 修改时间
     */
    @Column(name="modifyTime")
    protected Date modifyTime;

    /**
     * 名字
     */
    @Column(name="name")
    private String name;

    /**
     * 编码
     */
    @NaturalId(mutable=true)
    @Column(name="code")
    private String code;

    /**
     * 地址
     */
    @Column(name="address")
    private String address;


    /**
     * 猪舍是否属于激活状态
     */
    @Column(name="active")
    private boolean active;

    /**
     * 栏位
     */
    @OneToMany(mappedBy = "shed", cascade = CascadeType.PERSIST)
    @OrderBy("id")
    private Set<Sty> sties = new HashSet<>();

    /**
     * 药品仓库
     */
    @OneToMany(mappedBy = "shed", cascade = CascadeType.PERSIST)
    @OrderBy("id")
    private Set<MedicineWarehouse> medicineWarehouses = new HashSet<>();

    /**
     * 药品仓库
     */
    @OneToMany(mappedBy = "shed", cascade = CascadeType.PERSIST)
    @OrderBy("id")
    private Set<FeedWarehouse> feedWarehouses = new HashSet<>();

    /**
     * 设备
     */
    @OneToMany(mappedBy = "shed", cascade = CascadeType.PERSIST)
    @OrderBy("id")
    private Set<Device> devices = new HashSet<>();

    /**
     * 负责人
     */
    @ManyToOne
    @JoinColumn(name="headId")
    private User head;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Sty> getSties() {
        return sties;
    }

    public void setSties(Set<Sty> sties) {
        this.sties = sties;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Set<MedicineWarehouse> getMedicineWarehouses() {
        return medicineWarehouses;
    }

    public void setMedicineWarehouses(Set<MedicineWarehouse> medicineWarehouses) {
        this.medicineWarehouses = medicineWarehouses;
    }

    public void addSty(Sty sty){
        this.getSties().add(sty);
        sty.setShed(this);
    }

    public void removeSty(Sty sty){
        this.getSties().remove(sty);
        sty.setShed(null);
    }
    public void addMedicineWarehouse(MedicineWarehouse warehouse){
        this.getMedicineWarehouses().add(warehouse);
        warehouse.setShed(this);
    }

    public void removeMedicineWarehouse(MedicineWarehouse warehouse){
        this.getMedicineWarehouses().remove(warehouse);
        warehouse.setShed(null);
    }

    public void addDevice(Device device){
        this.getDevices().add(device);
        device.setShed(this);
    }

    public void removeDevice(Device device){
        this.getDevices().remove(device);
        device.setShed(null);
    }

    public User getHead() {
        return head;
    }

    public void setHead(User head) {
        this.head = head;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public Set<FeedWarehouse> getFeedWarehouses() {
        return feedWarehouses;
    }

    public void setFeedWarehouses(Set<FeedWarehouse> feedWarehouses) {
        this.feedWarehouses = feedWarehouses;
    }

    public void addFeedWarehouse(FeedWarehouse warehouse){
        this.getFeedWarehouses().add(warehouse);
        warehouse.setShed(this);
    }

    public void removeFeedWarehouse(FeedWarehouse warehouse){
        this.getFeedWarehouses().remove(warehouse);
        warehouse.setShed(null);
    }
}
