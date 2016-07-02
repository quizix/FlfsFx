package com.dxw.flfs.data.models.mes;

import javax.persistence.*;
import java.util.Date;

/**
 * 送料需求表
 * Created by zhang on 2016-07-02.
 */
@Entity
@Table(name="mes_feed_requirement")
@Access(AccessType.FIELD)
public class FeedRequirement {
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
     * 第一天需求
     */
    @Column(name="day1")
    private float day1;

    /**
     * 第二天需求
     */
    @Column(name="day2")
    private float day2;

    /**
     * 第三天需求
     */
    @Column(name="day3")
    private float day3;

    /**
     * 第四天需求
     */
    @Column(name="day4")
    private float day4;

    /**
     * 第五天需求
     */
    @Column(name="day5")
    private float day5;

    /**
     * 第六天需求
     */
    @Column(name="day6")
    private float day6;

    /**
     * 第七天需求
     */
    @Column(name="day7")
    private float day7;

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

    public float getDay1() {
        return day1;
    }

    public void setDay1(float day1) {
        this.day1 = day1;
    }

    public float getDay2() {
        return day2;
    }

    public void setDay2(float day2) {
        this.day2 = day2;
    }

    public float getDay3() {
        return day3;
    }

    public void setDay3(float day3) {
        this.day3 = day3;
    }

    public float getDay4() {
        return day4;
    }

    public void setDay4(float day4) {
        this.day4 = day4;
    }

    public float getDay5() {
        return day5;
    }

    public void setDay5(float day5) {
        this.day5 = day5;
    }

    public float getDay6() {
        return day6;
    }

    public void setDay6(float day6) {
        this.day6 = day6;
    }

    public float getDay7() {
        return day7;
    }

    public void setDay7(float day7) {
        this.day7 = day7;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
