package ca.gbc.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {
		org.springframework.boot.actuate.autoconfigure.metrics.mongo.MongoMetricsAutoConfiguration.class
})
@EnableFeignClients(basePackages = "ca.gbc.orderservice.client")
public class OrderServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class,args);
	}
}