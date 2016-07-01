package com.dxw.flfs.data.models.mes;

import com.dxw.flfs.data.models.erp.Device;
import com.dxw.flfs.data.models.erp.FeedWarehouse;
import com.dxw.flfs.data.models.erp.Sty;
import com.dxw.flfs.data.models.erp.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhang on 2016/4/6.
 */
@Entity
@Table(name="mes_site")
public class Site {
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
     * site code
     */
    @NaturalId(mutable=true)
    @Column(name="code")
    private String siteCode;

    /**
     * host
     */
    @Column(name="host")
    private String host;

    /**
     * 0:表示系统已经启动做料程序
     * 1：表示系统已经停止做料程序
     */
    @Column(name="status")
    @ColumnDefault("0")
    private int status;

    /**
     * 0：表示小猪入场阶段
     * 1：表示小猪入场结束
     */
    @Column(name="stage")
    @ColumnDefault("0")
    private int stage;

    /**
     *本站点所对应的栏位
     */
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="mes_site_sty",
            joinColumns=@JoinColumn(name="siteId"), inverseJoinColumns=@JoinColumn(name="styId"))
    @OrderBy("id")
    private Set<Sty> sties = new HashSet<>();


    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="mes_site_device",
            joinColumns=@JoinColumn(name="siteId"), inverseJoinColumns=@JoinColumn(name="deviceId"))
    @OrderBy("id")
    private Set<Device> devices = new HashSet<>();

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="mes_site_feed_warehouse",
            joinColumns=@JoinColumn(name="siteId"), inverseJoinColumns=@JoinColumn(name="feedWarehouseId"))
    @OrderBy("id")
    private Set<FeedWarehouse> feedWarehouses = new HashSet<>();

    /*@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="flfs_site_plan",
            joinColumns=@JoinColumn(name="siteId"), inverseJoinColumns=@JoinColumn(name="planId"))*/
    @OneToMany(mappedBy = "site", cascade = CascadeType.PERSIST)
    @OrderBy("id")
    private Set<PigletPlan> plans = new HashSet<>();

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="mes_site_user",
            joinColumns=@JoinColumn(name="siteId"), inverseJoinColumns=@JoinColumn(name="userId"))
    @OrderBy("id")
    private Set<User> users = new HashSet<>();

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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Sty> getSties() {
        return sties;
    }

    public void setSties(Set<Sty> sties) {
        this.sties = sties;
    }

    public void addSties(Collection<Sty> sties){
        this.sties.addAll(sties);
    }

    public void removeSties(Collection<Sty> sties){
        this.sties.removeAll(sties);
    }

    public void removeDevices(Collection<Device> devices){
        this.devices.removeAll(devices);
    }

    public void removeFeedWarehouses(Collection<FeedWarehouse> feedWarehouses){
        this.feedWarehouses.removeAll(feedWarehouses);
    }

    public Set<PigletPlan> getPlans() {
        return plans;
    }

    public void setPlans(Set<PigletPlan> plans) {
        this.plans = plans;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addUsers(Collection<User> users){
        this.users.addAll(users);
    }

    public void removeUsers(Collection<User> users){
        this.users.removeAll(users);
    }
}
