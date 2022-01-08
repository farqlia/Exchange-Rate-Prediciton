package exchangerateclass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Class holding currency and code names to load into
// view before making any request for values
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyName {

    private String currency, code;

    public CurrencyName(){}

    public CurrencyName(String currency, String code) {
        this.currency = currency;
        this.code = code;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
