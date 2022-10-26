package be.jeromebertaux.nvidiastock;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NvidiaStockApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(NvidiaStockApplication.class)
				.web(WebApplicationType.NONE)
						.run(args);
	}

}
