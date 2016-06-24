package com.dxw.flfs.data.models.erp;

import com.dxw.flfs.data.models.mes.Shed;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;

/**
 * 饲料仓库
 * Created by zhang on 2016-06-11.
 */
@Entity
@Table(name = "erp_medicine_warehouse")
@Access(AccessType.FIELD)
public class MedicineWarehouse {
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
     * 仓库名
     */
    @Column(name = "name")
    private String name;

    /**
     * 库位号
     */
    @Column(name = "no")
    private int no;

    /**
     * 编码
     */
    @NaturalId(mutable = true)
    @Column(name = "code")
    private String code;

    /**
     * 仓库属性
     */
    @Column(name = "property")
    private String property;

    /**
     * 猪舍
     */
    @ManyToOne
    @JoinColumn(name="shedId")
    private Shed shed;

    /**
     * 负责人
     */
    @ManyToOne
    @JoinColumn(name="headId")
    private User head;

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

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

    public User getHead() {
        return head;
    }

    public void setHead(User head) {
        this.head = head;
    }
}
