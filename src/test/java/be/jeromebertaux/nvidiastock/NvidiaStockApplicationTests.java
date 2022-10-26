package be.jeromebertaux.nvidiastock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(args = {"--ifttt.key=test-key", "--ifttt.event=event-test", "--store.locale=fr-fr"})
class NvidiaStockApplicationTests {

	@Test
	void contextLoads() {
	}

}
