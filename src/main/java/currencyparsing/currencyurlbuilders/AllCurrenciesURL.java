package currencyparsing.currencyurlbuilders;

public class AllCurrenciesURL extends CurrencyURL{

    private static final StringBuilder urlStrB = new StringBuilder();

    @Override
    StringBuilder getMiddlePart() {
        return urlStrB
                .append("tables").append(sep)
                .append(table);
    }

    AllCurrenciesURL(URLBuilder<?> builder) {
        super(builder);
    }

    public static class Builder extends CurrencyURL.URLBuilder<Builder>{


        public Builder(MoneyType moneyType){
            super(moneyType);
        }

        @Override
        public CurrencyURL build() {
            return new AllCurrenciesURL(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

}
