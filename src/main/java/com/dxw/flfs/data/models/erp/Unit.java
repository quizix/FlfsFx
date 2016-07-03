package com.dxw.flfs.data.models.erp;

import javax.persistence.*;
import java.util.Date;

/**
 * 计量单位
 * Created by zhang on 2016-06-17.
 */
@Entity
@Table(name="erp_unit")
@Access(AccessType.FIELD)
public class Unit {
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
     * 计量单位名称
     */
    @Column(name="name")
    private String name;

    /**
     * 计量单位符号
     */
    @Column(name="symbol")
    private String symbol;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
