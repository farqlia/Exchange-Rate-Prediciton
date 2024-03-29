package currencyparsing.currencyurlbuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class CurrencyURL {

    static char sep = '/';
    String table, date;
    private final String baseAddress = "http://api.nbp.pl/api/";
    private final StringBuilder urlStrB;

    // Middle part of the request is what varies
    abstract StringBuilder getMiddlePart();

    // http://api.nbp.pl/api/exchangerates/rates/{table}/{code}/{startDate}/{endDate}/

    public abstract static class URLBuilder<T extends URLBuilder<T>>{

        static DateTimeFormatter dTF = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String table = "", date = "";
        MoneyType moneyType;

        public URLBuilder(MoneyType moneyType){
            this.moneyType = moneyType;
        }

        // Reset all the fields so we can reuse this object
        public T reset(){
            table = "";
            date = "";
            return self();
        }

        public T addTable(Table table){
            this.table = table.toString() + sep;
            return self();
        }

        public T addDate(int last){
            if (last > 0 && last < 93){
                date = "last" + sep + last + sep;
            }
            return self();
        }

        public T addDate(LocalDate start, LocalDate end){
            if (start.compareTo(end) > 0){
                return addDate(end, start);
            }
            date = start.format(dTF) + sep + end.format(dTF) + sep;
            return self();
        }

        public T addDate(LocalDate date){
            if (date.compareTo(LocalDate.now()) == 0){
                this.date = "today" + sep;
            }
            else if (date.compareTo(LocalDate.now()) < 0){
                this.date = date.format(dTF) + sep;
            }
            return self();
        }

        public abstract CurrencyURL build();

        protected abstract T self();

    }

    CurrencyURL(URLBuilder<?> builder) {

        this.table = builder.table;
        this.date = builder.date;

        urlStrB = new StringBuilder(baseAddress).append(builder.moneyType).append(sep);
    }

    public String getURL(){
        return urlStrB
                .append(getMiddlePart())
                .append(date)
                .toString();
    }

}
