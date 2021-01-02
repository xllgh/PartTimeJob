package com.yly.trainsystem.ui.order;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class OrderItem extends BaseObservable {

    private String transportationNum;

    private String trainName;

    private String departureTime;

    private String departure;

    private String destination;

    public OrderItem() {

    }

    public OrderItem(String transportationNum, String departureTime) {
        this.transportationNum = transportationNum;
        this.departureTime = departureTime;
    }

    public String getTransportationNum() {
        return transportationNum;
    }

    @Bindable
    public void setTransportationNum(String transportationNum) {
        this.transportationNum = transportationNum;
    }
    @Bindable
    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    @Bindable
    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    @Bindable
    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    @Bindable
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
