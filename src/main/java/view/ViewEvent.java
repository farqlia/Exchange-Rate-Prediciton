package view;

import algorithms.Algorithm;
import algorithms.AlgorithmNames;

import java.time.LocalDate;

public class ViewEvent {

    LocalDate startDate;
    LocalDate endDate;
    AlgorithmNames chosenAlgorithm;
    String currencyCode;

    public ViewEvent(){};

    public ViewEvent(LocalDate startDate, LocalDate endDate,
                     AlgorithmNames chosenAlgorithm,
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

    public AlgorithmNames getChosenAlgorithm() {
        return chosenAlgorithm;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }


}
