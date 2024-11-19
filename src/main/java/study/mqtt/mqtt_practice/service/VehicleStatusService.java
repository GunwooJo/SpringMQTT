package study.mqtt.mqtt_practice.service;

import org.springframework.stereotype.Service;

@Service
public class VehicleStatusService {

    public void processStatus(String status) {
        // 수신한 상태 메시지 처리
        switch (status) {
            case "STOPPED":
                System.out.println("Vehicle has stopped. Taking no action.");
                break;
            case "MOVING":
                System.out.println("Vehicle is moving. Monitoring...");
                break;
            case "ERROR":
                System.out.println("Vehicle reported an error. Sending alert!");
                sendAlert();
                break;
            default:
                System.out.println("Unknown status received: " + status);
        }
    }

    private void sendAlert() {
        // 알림 또는 특정 대응 로직 구현
        System.out.println("Alert sent to the monitoring team.");
    }
}
