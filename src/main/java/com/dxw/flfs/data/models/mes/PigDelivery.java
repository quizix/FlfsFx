package com.dxw.flfs.data.models.mes;

import com.dxw.flfs.data.models.erp.Sty;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

/**
 * 育肥猪入库
 * Created by zhang on 2016-04-19.
 */
@Entity
@Table(name = "mes_pig_entry")
@Access(AccessType.FIELD)
public class PigDelivery {
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
     * 编号
     */
    @NaturalId(mutable=true)
    @Column(name="code")
    private String code;

    /**
     * 栏位
     */
    @ManyToOne
    @JoinColumn(name = "styId")
    private Sty sty;


    @Column(name = "number")
    protected int number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sty getSty() {
        return sty;
    }

    public void setSty(Sty sty) {
        this.sty = sty;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
