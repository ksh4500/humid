package com.sds.study.humidgraph;

/**
 * Created by 석환 on 2016-12-20.
 */


public class Bluetooth_DataDTO {
    private String MacAddress;
    private Double humidity1;
    private int temperature1;
    private Double humidity2;
    private int temperature2;
    private Double humidity3;
    private int temperature3;
    private String regdate;

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public Double getHumidity1() {
        return humidity1;
    }

    public void setHumidity1(Double humidity1) {
        this.humidity1 = humidity1;
    }

    public int getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(int temperature1) {
        this.temperature1 = temperature1;
    }

    public Double getHumidity2() {
        return humidity2;
    }

    public void setHumidity2(Double humidity2) {
        this.humidity2 = humidity2;
    }

    public int getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(int temperature2) {
        this.temperature2 = temperature2;
    }

    public Double getHumidity3() {
        return humidity3;
    }

    public void setHumidity3(Double humidity3) {
        this.humidity3 = humidity3;
    }

    public int getTemperature3() {
        return temperature3;
    }

    public void setTemperature3(int temperature3) {
        this.temperature3 = temperature3;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }
}
