package com.sds.study.humidgraph;

/**
 * Created by efro2 on 2016-12-21.
 */

public class ChartDTO {
    private int progressTime;
    private double humidity;
    private String etc;

    public int getProgressTime() {
        return progressTime;
    }

    public void setProgressTime(int progressTime) {
        this.progressTime = progressTime;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }
}
