package be.jeromebertaux.nvidiastock.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "ifttt")
public class IftttConfiguration {

    @NotBlank
    private String key;

    @NotBlank
    private String event;

}
