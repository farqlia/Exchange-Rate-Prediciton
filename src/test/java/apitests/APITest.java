package apitests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient; // since java 11
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APITest {

    String baseAddress = "http://api.nbp.pl/api/";
    String responseFormat = "?format=json";
    HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {

        APITest apiTest = new APITest();

        String exchangesRates = "exchangerates/";
        String rates = "rates/";
        String getExchangeRatesForTable = "tables/";
        String getTopCount = "last/";

        System.out.println("Get exchange rates for table C");
        apiTest.sendAndOutputRequest(exchangesRates  + getExchangeRatesForTable + "C/" + getTopCount + "2/");

        System.out.println("Get exchange rates for currency EUR");
        apiTest.sendAndOutputRequest(exchangesRates + rates + "C/" + "EUR/" + getTopCount + "10/");
    }

    // params should be built like this: "/param1/param2...paramn/"
    public void sendAndOutputRequest(String params) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseAddress + params + responseFormat))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
