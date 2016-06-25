package com.dxw.flfs.data.models.mes;

import javax.persistence.*;
import java.util.Date;

/**
 * 表示小猪入栏计划
 * * Created by zhang on 2016-04-28.
 */
@Entity
@Table(name="mes_piglet_plan")
@Access(AccessType.FIELD)
public class PigletPlan {
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
     * 日期
     */
    @Column(name="date")
    private Date date;


    /**
     * 数量
     */
    @Column(name="count")
    private int count;

    /**
     * 站点
     * @return
     */
    @ManyToOne
    @JoinColumn(name="siteId")
    private Site site;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
