package com.yly.trainsystem.ui.tickets;

import java.io.Serializable;
import java.sql.Timestamp;

public class TrainInfo implements Serializable {

    private int trainId;

    private String departure;

    private String destination;

    private Timestamp departureTime;

    private String trainFlag;

    private int speed;

    private String trainName;

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public String getTrainFlag() {
        return trainFlag;
    }

    public void setTrainFlag(String trainFlag) {
        this.trainFlag = trainFlag;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    @Override
    public String toString() {
        return "TrainInfo{" +
                "trainId=" + trainId +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", trainFlag='" + trainFlag + '\'' +
                ", speed=" + speed +
                ", trainName='" + trainName + '\'' +
                '}';
    }
}
