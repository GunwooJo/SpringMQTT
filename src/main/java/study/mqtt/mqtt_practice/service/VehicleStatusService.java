package study.mqtt.mqtt_practice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VehicleStatusService {

    public void processStatus(String status) {
        // 수신한 상태 메시지 처리
        switch (status) {
            case "STOPPED":
                log.info("Vehicle has stopped. Taking no action.");
                break;
            case "MOVING":
                log.info("Vehicle is moving. Monitoring...");
                break;
            case "ERROR":
                log.info("Vehicle reported an error. Sending alert!");
                sendAlert();
                break;
            default:
                log.error("Unknown status received: {}", status);
        }
    }

    private void sendAlert() {
        // 알림 또는 특정 대응 로직 구현
        System.out.println("Alert sent to the monitoring team.");
    }
}
