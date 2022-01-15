package currencyparsing.currencyurlworker;

import dataconverter.Formatter;
import exchangerateclass.CurrencyName;

import java.io.IOException;

public class CSVParser implements Formatter<CurrencyName> {

    StringBuilder sB = new StringBuilder(20);

    @Override
    public String getAsCSVString(CurrencyName o) {
        sB.delete(0, sB.length());
        return sB.append(o.getCurrency())
                .append(delimiter)
                .append(o.getCode()).toString();
    }

    @Override
    public CurrencyName parseFromCSVString(String stringToParse) throws IOException {
        String[] splitArr = stringToParse.split(delimiter);
        if (splitArr.length != 2){
            throw new IllegalArgumentException("Incorrect Data Format");
        }
        return new CurrencyName(splitArr[0], splitArr[1]);
    }
}
