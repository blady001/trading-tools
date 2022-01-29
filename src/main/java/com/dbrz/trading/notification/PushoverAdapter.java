package com.dbrz.trading.notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Qualifier("pushoverAdapter")
class PushoverAdapter implements NotificationAdapter {

    private final WebClient webClient;
    private final NotificationProperties.PushoverProperties pushoverProperties;

    private static final String TOKEN_PARAM = "token";
    private static final String USER_PARAM = "user";
    private static final String MESSAGE_PARAM = "message";

    public PushoverAdapter(NotificationProperties notificationProperties, WebClient.Builder webClientBuilder) {
        this.pushoverProperties = notificationProperties.getPushoverProperties();
        this.webClient = webClientBuilder.baseUrl(pushoverProperties.getApiUrl()).build();
    }

    @Override
    public Mono<Void> send(Notification notification) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(TOKEN_PARAM, pushoverProperties.getApiKey())
                        .queryParam(USER_PARAM, pushoverProperties.getUserKey())
                        .queryParam(MESSAGE_PARAM, notification.message())
                        .build())
                .exchangeToMono(this::handleResponse)
                .then();
    }

    private Mono<PushoverResponse> handleResponse(ClientResponse response) {
        if (response.statusCode().is2xxSuccessful())
            return response.bodyToMono(PushoverResponse.class);
        else
            return Mono.error(new ResponseStatusException(response.statusCode(), "Pushover request failed!"));
    }

    private record PushoverResponse(int status, String request) {

    }
}
