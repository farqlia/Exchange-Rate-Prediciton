package view;

import algorithms.AlgorithmName;

import java.time.LocalDate;

public class ViewEvent {

    LocalDate startDate;
    LocalDate endDate;
    AlgorithmName chosenAlgorithm;
    String currencyCode;
    int lookbackPeriod;

    public ViewEvent(){};

    public ViewEvent(LocalDate startDate, LocalDate endDate,
                     AlgorithmName chosenAlgorithm,
                     String currencyCode,
                     int lookbackPeriod) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.chosenAlgorithm = chosenAlgorithm;
        this.currencyCode = currencyCode;
        this.lookbackPeriod = lookbackPeriod;
    }

    public ViewEvent(LocalDate startDate, LocalDate endDate,
                     AlgorithmName chosenAlgorithm,
                     String currencyCode) {
        this(startDate, endDate, chosenAlgorithm, currencyCode, 5);
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

    public int getLookbackPeriod() {
        return lookbackPeriod;
    }
}
