package view.view;

import algorithms.AlgorithmInitializerExPost;

import java.time.LocalDate;

public class ViewEvent {

    LocalDate startDate;
    LocalDate endDate;
    AlgorithmInitializerExPost chosenAlgorithm;
    String currencyCode;

    public ViewEvent(){};

    public ViewEvent(LocalDate startDate, LocalDate endDate,
                     AlgorithmInitializerExPost chosenAlgorithm,
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

    public AlgorithmInitializerExPost getChosenAlgorithm() {
        return chosenAlgorithm;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

}