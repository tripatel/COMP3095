package ca.gbc.orderservice;


import ca.gbc.orderservice.stub.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

	@ServiceConnection
	static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine");


	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		postgresContainer.start();
	}

	@Test
	void SubmitOrder() {
		// JSON payload to submit an order
		String submitOrderJson = """
                {
                    "skuCode": "samsung_tv_2024",
                    "price": 5000,
                    "quantity": 10
                }
                """;

		//Mock a call to inventory-service
		InventoryClientStub.stubInventoryCall("samsung_tv_2024", 10);

		// Send POST request and validate response
		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201) // Validate HTTP status
				.extract()
				.body()
				.asString(); // Extract response as string

		// Validate the response body matches the expected output
		assertThat(responseBodyString, Matchers.is("Order Placed successfully"));
	}
}