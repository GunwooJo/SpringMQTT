package study.mqtt.mqtt_practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
//@IntegrationComponentScan
public class MqttPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqttPracticeApplication.class, args);
	}

}
