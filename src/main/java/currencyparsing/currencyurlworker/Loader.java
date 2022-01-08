package currencyparsing.currencyurlworker;

import currencyparsing.currencymapper.CurrencyNameMapper;
import currencyparsing.currencymapper.CurrencyObjectMapper;
import currencyparsing.currencyurlbuilders.CurrencyURL;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Loader<E> {

    private CurrencyURL url;
    private CurrencyWorker currencyWorker;
    private CurrencyObjectMapper<E> mapper;

    public Loader(CurrencyURL url, CurrencyObjectMapper<E> mapper){
        this.url = url;
        this.mapper = mapper;
        currencyWorker = new CurrencyWorker();
    }

    public List<E> load(){

        Optional<String> response
                = currencyWorker.send(url.getURL());

        if (response.isEmpty()){
            return getDefault();
        }

        List<E> mappedObjects = mapper.parse(response.get());

        if (mappedObjects.isEmpty()){
            return getDefault();
        }
        return mappedObjects;
    }

    public void setCurrencyURL(CurrencyURL newURL){
        this.url = newURL;
    }

    // This can be overridden by subclasses to provide
    // a solution in case the load method failed
    protected List<E> getDefault(){
        return Collections.emptyList();
    }

}
