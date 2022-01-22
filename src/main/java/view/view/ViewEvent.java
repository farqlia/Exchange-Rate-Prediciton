package view.view;

import algorithms.algorithmsinitializer.AlgorithmInitializer;

import java.time.LocalDate;

public class ViewEvent {

    LocalDate startDate;
    LocalDate endDate;
    AlgorithmInitializer chosenAlgorithm;
    String currencyCode;

    public ViewEvent(){};

    public ViewEvent(LocalDate startDate, LocalDate endDate,
                     AlgorithmInitializer chosenAlgorithm,
                     String currencyCode) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.chosenAlgorithm = chosenAlgorithm;
        this.currencyCode = currencyCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public AlgorithmInitializer getChosenAlgorithm() {
        return chosenAlgorithm;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

}