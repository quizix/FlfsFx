package com.dxw.flfs.data.models.erp;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

/**
 * 仓库
 * Created by zhang on 2016-06-11.
 */
@Entity
@Table(name="erp_warehouse")
@Access(AccessType.FIELD)
public class Warehouse {
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
     * 编码
     */
    @NaturalId(mutable=true)
    @Column(name="code")
    private String code;
}