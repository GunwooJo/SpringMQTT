package study.mqtt.mqtt_practice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.mqtt.mqtt_practice.service.MqttPublishService;

@RestController
@RequiredArgsConstructor
public class MqttController {

    private final MqttPublishService mqttPublishService;

    @GetMapping("/publish")
    public String publish() {
        mqttPublishService.publishMessage("vehicle/status", "MOVING");
        return "완료.";
    }
}
