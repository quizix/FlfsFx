package com.dxw.flfs.data.models.erp;

import javax.persistence.*;
import java.util.Date;

/**
 * 猪的品种
 * Created by zhang on 2016-06-11.
 */
@Entity
@Table(name="erp_pig")
@Access(AccessType.FIELD)
public class Pig {
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
}
