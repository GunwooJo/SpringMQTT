package study.mqtt.mqtt_practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MqttPublishService {

    private final MessageChannel mqttOutboundChannel;

    // topic과 payload를 설정하여 메시지 발행
    public void publishMessage(String topic, String payload) {

        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader("mqtt_topic", topic)
                .build();

        mqttOutboundChannel.send(message);

        log.info("Published to topic: {} with payload: {}", topic, payload);
    }
}
