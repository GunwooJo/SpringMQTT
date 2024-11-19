# Spring에서 MQTT를 사용하기 위한 설정

- 메시지 브로커로 mosquitto를 사용.
- 특정 토픽에 대해 메시지를 수신하면 로그를 출력.

## 버전

- spring-integration-mqtt:6.3.5
- mosquitto:2.0.20
- Spring Boot 3.3.5
- Java 17

## mosquitto 설치와 실행(mac)

Homebrew를 사용하여 설치

`brew install mosquitto`

실행

`mosquitto -v`

## 작동 테스트

### 1. Spring 애플리케이션 실행.

### 2. MQTT 브로커에 메시지를 publish.

`mosquitto_pub -h localhost -p 1883 -t "vehicle/status" -m "MOVING"`

- mosquitto_pub: Mosquitto에서 제공하는 MQTT 퍼블리셔(Publisher) 클라이언트 도구.

- -h localhost: 브로커 주소(host) 지정. ex) -h 192.168.1.100 또는 -h broker.example.com

- -p 1883: 브로커 포트 지정.

- -t "vehicle/status": 토픽 지정.

- -m "MOVING": 메시지 지정.

### 3. Spring 애플리케이션에서 메시지 수신 확인.
```
Received message: MOVING
Vehicle is moving. Monitoring...
```
