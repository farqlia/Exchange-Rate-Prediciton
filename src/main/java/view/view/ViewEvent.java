package view.view;

import algorithms.AlgorithmName;
import algorithms.algorithmsparameters.AlgorithmArguments;
import static algorithms.algorithmsparameters.AlgorithmArguments.Names;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class ViewEvent {

    LocalDate startDate;
    LocalDate endDate;
    AlgorithmName chosenAlgorithm;
    String currencyCode;

    public ViewEvent(){};

    public ViewEvent(LocalDate startDate, LocalDate endDate,
                     AlgorithmName chosenAlgorithm,
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

    public AlgorithmName getChosenAlgorithm() {
        return chosenAlgorithm;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

}