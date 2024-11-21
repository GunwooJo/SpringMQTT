package study.mqtt.mqtt_practice.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import study.mqtt.mqtt_practice.service.VehicleStatusService;

import java.util.UUID;

@Configuration
@Slf4j
public class MqttConfig {

    private static final String BROKER_URL = "tcp://localhost:1883"; // Mosquitto 브로커 주소
    private static final String CLIENT_ID = "vehicleControlCenter_" + UUID.randomUUID(); //  Spring 애플리케이션을 여러 서버에서 실행할 때 클라이언트 ID가 중복되지 않도록 UUID 사용. 단일 서버면 UUID를 사용하지 않아도 됨.

    @Value("${mqtt.qos}")
    private int mqttQos;

    @Value("${mqtt.topics.input}")
    private String inputTopic;

    @Value("${mqtt.topics.output}")
    private String outputTopic;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{BROKER_URL});
        options.setCleanSession(true); // 클라이언트 연결 시 브로커가 이전 세션 정보를 제거하도록 설정.
        return options;
    }

    @Bean
    public DefaultMqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions()); // 위에서 설정한 MqttConnectOptions를 사용하여 브로커 연결 설정을 전달
        return factory;
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter adapter(MessageChannel mqttInputChannel) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(CLIENT_ID + "_subscriber", mqttClientFactory(), inputTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(mqttQos);
        adapter.setOutputChannel(mqttInputChannel);
        return adapter;
    }

    /*
    DirectChannel은 Spring Integration에서 제공하는 메시지 채널 중 하나로, 메시지를 동기적으로 전달.
    그 외 QueueChannel, PublishSubscribeChannel, ExecutorChannel, PriorityChannel 등이 있음.
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttInputHandler(VehicleStatusService vehicleStatusService) {
        return message -> {
            String payload = message.getPayload().toString();
            log.info("Received message: {}", payload);

            // 메시지에 따라 로직 처리
            vehicleStatusService.processStatus(payload);
        };
    }

    @Bean
    public MessageChannel mqttOutboundChannel() { // 메시지 발행에 사용할 출력 채널을 정의
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutboundHandler() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(CLIENT_ID + "_publisher", mqttClientFactory());
        messageHandler.setAsync(true); // 비동기 전송
        messageHandler.setDefaultTopic(outputTopic);
        return messageHandler;
    }

    // 메시지 전송 실패에 대한 핸들러
    @Bean
    @ServiceActivator(inputChannel = "errorChannel")
    public MessageHandler errorHandler() {
        return message -> {
            Throwable cause = (Throwable) message.getPayload();
            log.error("Error sending MQTT message: {}", cause.getMessage());
        };
    }
}
