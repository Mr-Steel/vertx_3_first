package net.steel.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class MyVerticle extends AbstractVerticle {

    Vertx vertx = null;
    WebClient webClient = null;
    HttpRequest<Buffer> getRequest = null;

    @Override
    public void start(Future<Void> startFuture) {
        System.out.println("MyVerticle started!");
        startFuture.complete();
        vertx = Vertx.vertx();

        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setKeepAlive(false);
        WebClient webClient = WebClient.create(vertx, webClientOptions);
        getRequest = webClient.get(8080, "localhost", "/adminsection");
        MultiMap headers = getRequest.headers();
        headers.set("Accept", "text/html");
        String auth = "YWRtaW46YWRtaW5wYXNzZTE=";
        headers.set("Authorization", "Basic " + auth);
        makeRequest(auth);

        for (int i = 0; i < 5; i++) {
            auth = "ZWRtaW46YWRtaW5wYXNzZTE";
            makeRequest(auth);
        }
        auth = "YWRtaW46YWRtaW5wYXNzZTE=";
        makeRequest(auth);
    }

    @Override
    public void stop(Future stopFuture) throws Exception {
        System.out.println("MyVerticle stopped!");
    }

    private void makeRequest(String auth) {
        MultiMap headers = getRequest.headers();
        headers.set("Authorization", "Basic " + auth);
        getRequest.send(httpResponseAsyncResult -> {
            if (httpResponseAsyncResult.succeeded()) {
                HttpResponse<Buffer> httpResponse = httpResponseAsyncResult.result();
                int statusCode = httpResponse.statusCode();
                if (statusCode == 200) {
                    System.out.println("Got it!");
                } else {
                    System.out.println("Not working" + httpResponse.statusCode());
                }
            } else {
                System.out.println("Something went wrong. " + httpResponseAsyncResult.cause().getMessage());
            }
        });
    }

}