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

        System.out.println("Get exchange rates TABLE C for currency EUR");
        apiTest.sendAndOutputRequest(exchangesRates + rates + "C/" + "EUR/" + "2021-01-01/2021-12-31/");

        System.out.println("Get exchange rates TABLE A for currency EUR");
        apiTest.sendAndOutputRequest(exchangesRates + rates + "A/" + "EUR/" + "2021-01-01/2021-12-31/");

        /*
        System.out.println("Get exchange rates for algorithmTableModel A");
        apiTest.sendAndOutputRequest(exchangesRates  + getExchangeRatesForTable + "A/" + getTopCount + "2/");

        apiTest.sendAndOutputRequest(exchangesRates + getExchangeRatesForTable + "C/" + "2021-12-01/2021-12-31/");

         */
    }

    // params should be built like this: "/param1/param2...paramn/"
    public void sendAndOutputRequest(String params) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseAddress + params + responseFormat))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println(response.statusCode());
    }
}
