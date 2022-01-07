package currencyparsing.currencyurlbuilders;

public class ConcreteCurrencyURL extends CurrencyURL{

    private static final StringBuilder urlStrB = new StringBuilder();
    private String currencyCode;

    @Override
    StringBuilder getMiddlePart() {
        return urlStrB
                .append("rates").append(sep)
                .append(table)
                .append(currencyCode);
    }

    ConcreteCurrencyURL(URLBuilder<?> builder){
        super(builder);
        this.currencyCode = ((Builder) builder).currencyCode;
    }

    public static class Builder extends URLBuilder<Builder>{

        String currencyCode = "";
        public Builder(MoneyType moneyType){
            super(moneyType);

        }

        public Builder addCurrencyCode(String currencyCode){
            this.currencyCode = currencyCode + sep;
            return self();
        }

        @Override
        public Builder reset() {
            super.reset();
            currencyCode = "";
            return self();
        }

        @Override
        public CurrencyURL build() {
            return new ConcreteCurrencyURL(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

}
