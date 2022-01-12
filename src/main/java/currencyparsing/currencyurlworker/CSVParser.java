package currencyparsing.currencyurlworker;

import dataconverter.IncorrectDataFormat;
import dataconverter.PrintableAsCSV;
import exchangerateclass.CurrencyName;

public class CSVParser implements PrintableAsCSV<CurrencyName> {

    StringBuilder sB = new StringBuilder(20);

    @Override
    public String getAsCSVString(CurrencyName o) {
        sB.delete(0, sB.length());
        return sB.append(o.getCurrency())
                .append(delimiter)
                .append(o.getCode()).toString();
    }

    @Override
    public CurrencyName parseFromCSVString(String stringToParse) throws IncorrectDataFormat {
        String[] splitArr = stringToParse.split(delimiter);
        if (splitArr.length != 2){
            throw new IncorrectDataFormat("Incorrect Data Format");
        }
        return new CurrencyName(splitArr[0], splitArr[1]);
    }
}
