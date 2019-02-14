package com.ioter.uhfscan.data.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class EpcModel {
    @Id(autoincrement = true)
    private Long id;
    private String epc;
    private String casecode;
    private String message;
    private String state;
    private String warehouse;
    @Generated(hash = 37528945)
    public EpcModel(Long id, String epc, String casecode, String message,
            String state, String warehouse) {
        this.id = id;
        this.epc = epc;
        this.casecode = casecode;
        this.message = message;
        this.state = state;
        this.warehouse = warehouse;
    }
    @Generated(hash = 2138003262)
    public EpcModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEpc() {
        return this.epc;
    }
    public void setEpc(String epc) {
        this.epc = epc;
    }
    public String getCasecode() {
        return this.casecode;
    }
    public void setCasecode(String casecode) {
        this.casecode = casecode;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getWarehouse() {
        return this.warehouse;
    }
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
}
