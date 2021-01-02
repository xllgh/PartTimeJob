package com.yly.trainsystem.ui.tickets;

import java.io.Serializable;

public class TrainStationInfo implements Serializable {
    private int stationId;
    private int stationNo;
    private String stationName;
    private int waitingTime;
    private int distanceToNext;
    private int trainId;

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getStationNo() {
        return stationNo;
    }

    public void setStationNo(int stationNo) {
        this.stationNo = stationNo;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getDistanceToNext() {
        return distanceToNext;
    }

    public void setDistanceToNext(int distanceToNext) {
        this.distanceToNext = distanceToNext;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }
}
