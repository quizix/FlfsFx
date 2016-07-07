package com.dxw.flfs.data.models.erp;

import com.dxw.flfs.data.models.mes.PigEntry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 栏位
 *
 * @author pronics3
 *
 */
@Entity
@Table(name="erp_sty")
@Access(AccessType.FIELD)
public class Sty{
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
     * 猪舍
     */
    @ManyToOne
    @JoinColumn(name="shedId")
    private Shed shed;

    /**
     * 名字
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
     * 编号
     */
    @Column(name="no")
    private int no;


    /**
     * 猪的数量，并由此推断栏位状态
     */
    @Column(name="currentNumber")
    private int currentNumber;

    /**
     * 猪的数量，并由此推断栏位状态
     */
    @Column(name="lastNumber")
    private int lastNumber;

    @OneToMany(mappedBy = "sty", cascade = CascadeType.PERSIST)
    @OrderBy("createDate DESC")
    private Set<PigEntry> styOperations = new HashSet<>();

    @Transient
    private BooleanProperty checked = new SimpleBooleanProperty();

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

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
        this.shed.getSties().add(this);
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

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }


    public boolean getChecked() {
        return checked.get();
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public void addCurrent(int value){
        this.currentNumber += value;
    }

    public void subCurrent(int value){
        this.currentNumber -= value;
    }

    @Override
    public String toString(){
        return this.getName() + "-" + this.getCode();
    }



    public void addStyOperation(PigEntry styOperation){
        styOperation.setSty(this);
        this.styOperations.add(styOperation);
    }

    public void removeStyOperation(PigEntry styOperation){
        styOperation.setSty(null);
        this.styOperations.remove(styOperation);
    }

    public Set<PigEntry> getStyOperations() {
        return styOperations;
    }

    public void setStyOperations(Set<PigEntry> styOperations) {
        this.styOperations = styOperations;
    }
}
