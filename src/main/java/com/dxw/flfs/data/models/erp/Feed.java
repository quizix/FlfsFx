package com.dxw.flfs.data.models.erp;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhang on 2016-06-11.
 */
@Entity
@Table(name="erp_feed")
@Access(AccessType.FIELD)
public class Feed {
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
     * 名称
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
     * 编码
     */
    @NaturalId(mutable=true)
    @Column(name="description")
    private String description;

    /**
     * 单位
     */
    @ManyToOne
    @JoinColumn(name="unitId")
    private Unit unit;

    /**
     * 供应商
     */
    @ManyToOne
    @JoinColumn(name="vendorId")
    private Vendor vendor;

    /**
     * 分类
     */
    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
