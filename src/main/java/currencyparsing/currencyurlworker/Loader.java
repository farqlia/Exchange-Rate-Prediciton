package currencyparsing.currencyurlworker;

import currencyparsing.currencymapper.CurrencyObjectMapper;
import currencyparsing.currencyurlbuilders.CurrencyURL;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Loader<E> {

    private CurrencyURL url;
    private final CurrencyWorker currencyWorker;
    private CurrencyObjectMapper<E> mapper;

    public Loader(CurrencyObjectMapper<E> mapper){
        this.mapper = mapper;
        currencyWorker = new CurrencyWorker();
    }
    
    public List<E> load(){

        Optional<String> response
                = currencyWorker.send(url.getURL());

        if (response.isEmpty()){
            return Collections.emptyList();
        }

        List<E> mappedObjects = mapper.parse(response.get());

        if (mappedObjects.isEmpty()){
            return Collections.emptyList();
        }

        return mappedObjects;
    }

    public void setCurrencyURL(CurrencyURL newURL){
        this.url = newURL;
    }

}
