package com.dxw.flfs.data.models.erp;

import javax.persistence.*;
import java.util.Date;

/**
 * 供应商/厂家
 * Created by zhang on 2016-06-17.
 */
@Entity
@Table(name = "erp_vendor")
@Access(AccessType.FIELD)
public class Vendor {
    /**
     * 内部id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modifyTime")
    private Date modifyTime;

    /**
     * 供应商名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 供应商描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 供应商编码
     */
    @Column(name = "code")
    private String code;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return this.name + "-" + this.code;
    }
}
