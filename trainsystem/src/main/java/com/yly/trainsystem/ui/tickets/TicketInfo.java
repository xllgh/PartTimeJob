package com.yly.trainsystem.ui.tickets;

import java.io.Serializable;
import java.util.List;

public class TicketInfo implements Serializable {

    private TrainInfo trainInfo;

    private List<TrainStationInfo> trainStationInfo;

    public TrainInfo getTrainInfo() {
        return trainInfo;
    }

    public void setTrainInfo(TrainInfo trainInfo) {
        this.trainInfo = trainInfo;
    }

    public List<TrainStationInfo> getTrainStationInfo() {
        return trainStationInfo;
    }

    public void setTrainStationInfo(List<TrainStationInfo> trainStationInfo) {
        this.trainStationInfo = trainStationInfo;
    }

    @Override
    public String toString() {
        return "TicketInfo{" +
                "trainInfo=" + trainInfo +
                ", trainStationInfo=" + trainStationInfo +
                '}';
    }
}
