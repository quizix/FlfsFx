package com.dxw.flfs.data.models.mes;

import com.dxw.flfs.data.models.erp.Shed;

import javax.persistence.*;
import java.util.Date;

/**
 * PH值
 *
 * @author pronics3
 *
 */
@Entity
@Table(name="mes_ph")
@Access(AccessType.FIELD)
public class Ph{
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
     * ph值
     */
    @Column(name="value")
    private float value;

    /**
     * 猪舍
     */
    @ManyToOne
    @JoinColumn(name="shedId")
    private Shed shed;


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

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "";
    }
}
