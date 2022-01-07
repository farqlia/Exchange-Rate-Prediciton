package currencyparsing.currencyurlbuilders;

public enum MoneyType {

    CURRENCY("exchangerates"),
    GOLD("cenyzlota");

    private String name;
    MoneyType(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
