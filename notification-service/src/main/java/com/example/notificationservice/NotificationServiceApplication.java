package com.example.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@KafkaListener(id="notificationId" ,topics = "notifications")
	public void handleNotification(CartPlacedEvent cartPlacedEvent) {

		log.info("Đơn hàng số: " + cartPlacedEvent.getCartId() + " đã được tạo!");
	}
}
