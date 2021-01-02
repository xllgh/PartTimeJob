package com.yly.trainsystem.ui.tickets;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class TicketViewModel extends ViewModel {

    public final ObservableField<String> trainName = new ObservableField<>();

    public final ObservableField<String> departure = new ObservableField<>();

    public final ObservableField<String> destination = new ObservableField<>();

    public final ObservableField<String> departureTime = new ObservableField<>();


}
