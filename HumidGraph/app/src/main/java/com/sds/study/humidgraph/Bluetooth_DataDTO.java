package com.sds.study.humidgraph;

/**
 * Created by 석환 on 2016-12-20.
 */


public class Bluetooth_DataDTO {
    private String MacAddress;
    private int humidity1;
    private int temperature1;
    private int humidity2;
    private int temperature2;
    private int humidity3;
    private int temperature3;

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    private String regdate;

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public int getHumidity1() {
        return humidity1;
    }

    public void setHumidity1(int humidity1) {
        this.humidity1 = humidity1;
    }

    public int getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(int temperature1) {
        this.temperature1 = temperature1;
    }

    public int getHumidity2() {
        return humidity2;
    }

    public void setHumidity2(int humidity2) {
        this.humidity2 = humidity2;
    }

    public int getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(int temperature2) {
        this.temperature2 = temperature2;
    }

    public int getHumidity3() {
        return humidity3;
    }

    public void setHumidity3(int humidity3) {
        this.humidity3 = humidity3;
    }

    public int getTemperature3() {
        return temperature3;
    }

    public void setTemperature3(int temperature3) {
        this.temperature3 = temperature3;
    }


}
