package com.dxw.flfs.data.models.erp;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

/**
 * erp：采购订单
 * Created by zhang on 2016-07-02.
 */
@Entity
@Table(name="erp_order")
@Access(AccessType.FIELD)
public class Order {
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
     * 订单编号
     */
    @NaturalId(mutable=true)
    @Column(name="code")
    private String code;


    /**
     * 订单数量
     */
    @Column(name="code")
    private float amount;

}
