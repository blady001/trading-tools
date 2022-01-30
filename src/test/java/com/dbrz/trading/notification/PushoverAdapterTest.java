package com.dbrz.trading.notification;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PushoverAdapterTest {

    private MockWebServer mockWebServer;
    private PushoverAdapter pushoverAdapter;

    @Mock
    private NotificationProperties.PushoverProperties pushoverProperties;

    private static final String API_KEY_MOCK = "apiKey";
    private static final String USER_KEY_MOCK = "userKey";

    @BeforeAll
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void init() {
        when(pushoverProperties.getApiUrl()).thenReturn(String.format("http://localhost:%s", mockWebServer.getPort()));
        when(pushoverProperties.getApiKey()).thenReturn(API_KEY_MOCK);
        when(pushoverProperties.getUserKey()).thenReturn(USER_KEY_MOCK);
        var notificationProperties = new NotificationProperties();
        notificationProperties.setPushoverProperties(pushoverProperties);
        pushoverAdapter = new PushoverAdapter(notificationProperties);
    }

    @AfterAll
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldSendRequestToPushoverAndHandleSuccessfulResponse() throws InterruptedException {
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
    void shouldReturnException() throws InterruptedException {
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
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"status\": 1, \"request\": \"test\"}")
                .addHeader("Content-Type", "application/json"));
    }

    private void givenUnsuccessfulPushoverResponse() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
    }

    private void verifyPushoverCalled(String message) throws InterruptedException {
        var recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getPath()).isEqualTo(
                String.format("/?token=%s&user=%s&message=%s", API_KEY_MOCK, USER_KEY_MOCK, message));
    }
}