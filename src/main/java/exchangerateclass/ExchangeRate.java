package exchangerateclass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {

    private String currency, code;
    private Date effectiveDate;
    private BigDecimal bid, ask;

    public ExchangeRate(){}

    public ExchangeRate(String currency, String code, Date effectiveDate, BigDecimal bid, BigDecimal ask) {
        this.currency = currency;
        this.code = code;
        this.effectiveDate = effectiveDate;
        this.bid = bid;
        this.ask = ask;
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

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "currency='" + currency + '\'' +
                ", code='" + code + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}
