package be.jeromebertaux.nvidiastock.service;

import be.jeromebertaux.nvidiastock.configuration.StoreConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

@Service
@Slf4j
public class StockPoller {
    public static final String NVIDIA_API_URL = "https://api.store.nvidia.com/partner/v1/feinventory?status=1&skus=NVGFT490&locale=%s";

    // 3090TI = NVGFT090T
    // 4090 = NVGFT490

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final NotificationSender notificationSender;

    private final HttpRequest nvidiaApiRequest;

    public StockPoller(HttpClient httpClient,
                       ObjectMapper objectMapper,
                       NotificationSender notificationSender,
                       StoreConfiguration storeConfiguration) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.notificationSender = notificationSender;

        nvidiaApiRequest = HttpRequest.newBuilder()
                .uri(URI.create(NVIDIA_API_URL.formatted(storeConfiguration.getLocale())))
                .GET()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0")
                .header("Accept-Language", "en-GB,en-US;q=0.7,en;q=0.3")
                .build();
    }

    public void poll() {
        try {
            HttpResponse<String> response = httpClient.send(nvidiaApiRequest, ofString());

            int statusCode = response.statusCode();
            String body = response.body();

            if (statusCode == 200) {
                log.info("Response {}", body);
                JsonNode json = objectMapper.readTree(body);
                if (json.path("success").asBoolean()) {
                    if (json.path("listMap").get(0).path("is_active").asBoolean()) {
                        log.info("There is stock");
                        notificationSender.sendNotification("Available");
                    } else {
                        log.info("No stock");
                    }
                } else {
                    log.error("Nvidia response is not successful, response {}", body);
                    notificationSender.sendNotification("ERROR");
                }
            } else {
                log.error("Error calling Nvidia. Status Code {}, response {}", statusCode, body);
                notificationSender.sendNotification("ERROR");
            }
        } catch (Exception e) {
            log.error("Cannot poll Nvidia", e);
            notificationSender.sendNotification("ERROR");
        }
    }

}
