package be.jeromebertaux.nvidiastock.service;

import be.jeromebertaux.nvidiastock.configuration.IftttConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Map;

import static java.net.http.HttpResponse.BodyHandlers.discarding;

@Service
@Slf4j
public class NotificationSender {

    private static final String IFTTT_URL = "https://maker.ifttt.com/trigger/%s/with/key/%s";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final URI iftttUri;

    public NotificationSender(HttpClient httpClient,
                              ObjectMapper objectMapper,
                              IftttConfiguration iftttConfiguration) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.iftttUri = URI.create(IFTTT_URL.formatted(iftttConfiguration.getEvent(), iftttConfiguration.getKey()));
    }

    public boolean sendNotification(String content) {
        log.info("Send notification with content {}", content);
        try {
            var values = Map.of("value1", content);

            String requestBody = objectMapper.writeValueAsString(values);
            log.debug("Request IFTTT body {}", requestBody);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(iftttUri)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();

            httpClient.send(httpRequest, discarding());
            return true;
        } catch (Exception e) {
            log.error("Cannot send notification", e);
        }
        return false;
    }

}
