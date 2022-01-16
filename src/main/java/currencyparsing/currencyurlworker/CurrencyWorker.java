package currencyparsing.currencyurlworker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class CurrencyWorker {

    HttpClient client = HttpClient.newHttpClient();

    public Optional<String> send(String request){

        HttpRequest r = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(request))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(r, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        if (response.statusCode() != 200){
            return Optional.empty();
        }
        return Optional.of(response.body());
    }

}
