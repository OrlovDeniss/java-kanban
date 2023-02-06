package ru.yandex.practicum.kanban.manager.web.kv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final URI url;
    private String apiToken;
    private final HttpClient client;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = URI.create(url);
        client = HttpClient.newHttpClient();
        register();
    }

    private void register() throws IOException, InterruptedException {

        var registerRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        apiToken = clientSend(registerRequest).body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {

        var saveRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        clientSend(saveRequest);
    }

    public String load(String key) throws IOException, InterruptedException {

        var loadRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();

        return clientSend(loadRequest).body();
    }

    private HttpResponse<String> clientSend(HttpRequest request) throws IOException, InterruptedException {

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        return client.send(request, handler);

    }

}
