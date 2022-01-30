package com.dbrz.trading.notification;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PushoverAdapterTest {

    private WireMockServer wireMockServer;
    private PushoverAdapter pushoverAdapter;

    @Mock
    private NotificationProperties.PushoverProperties pushoverProperties;

    private static final String API_KEY = "apiKey";
    private static final String USER_KEY = "userKey";
    private static final int TEST_PORT = 8090;

    @BeforeAll
    void setUp() {
        wireMockServer = new WireMockServer(TEST_PORT);
        wireMockServer.start();
    }

    @BeforeEach
    void init() {
        when(pushoverProperties.getApiUrl()).thenReturn(String.format("http://localhost:%s", TEST_PORT));
        when(pushoverProperties.getApiKey()).thenReturn(API_KEY);
        when(pushoverProperties.getUserKey()).thenReturn(USER_KEY);
        var notificationProperties = new NotificationProperties();
        notificationProperties.setPushoverProperties(pushoverProperties);
        pushoverAdapter = new PushoverAdapter(notificationProperties);
    }

    @AfterAll
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void shouldSendRequestToPushoverAndHandleSuccessfulResponse() {
        // given
        var givenMessage = "Message";
        var givenNotification = new Notification("Title", givenMessage);
        givenSuccessfulPushoverResponse();

        // when
        var result = pushoverAdapter.send(givenNotification);

        // then
        StepVerifier.create(result).verifyComplete();
        verifyPushoverCalled(givenMessage);
    }

    @Test
    void shouldReturnException() {
        // given
        var givenMessage = "Message";
        var givenNotification = new Notification("Title", givenMessage);
        givenUnsuccessfulPushoverResponse();

        // when
        var result = pushoverAdapter.send(givenNotification);

        // then
        StepVerifier.create(result)
                .expectError(ResponseStatusException.class)
                .verify();
        verifyPushoverCalled(givenMessage);
    }

    private void givenSuccessfulPushoverResponse() {
        wireMockServer.stubFor(post(urlPathEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"status\": 1, \"request\": \"test\"}")
                        .withHeader("Content-Type", "application/json")));
    }

    private void givenUnsuccessfulPushoverResponse() {
        wireMockServer.stubFor(post(urlPathEqualTo("/"))
                .willReturn(aResponse()
                        .withStatus(500)));
    }

    private void verifyPushoverCalled(String message) {
        var expectedPath = String.format("/?token=%s&user=%s&message=%s", API_KEY, USER_KEY, message);
        wireMockServer.verify(postRequestedFor(urlEqualTo(expectedPath)));
    }
}