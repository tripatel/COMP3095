package ca.gbc.productservice;

import io.restassured.RestAssured;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer= new MongoDBContainer("mongo:latest");
    @LocalServerPort
    private Integer port;
    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

    }
    static {
        mongoDBContainer.start();
    }
    @Test
    void createProductTest(){
        String requestBody = """
                {
                "name" : "Samsung TV",
                "description" : "Samsung TV - Model 2024",
                "price" : "2000.00"
                
                }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id",  Matchers.notNullValue())
                .body("name" ,Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000.0F));



    }
    @Test
    void getAllProductTest() {
        // Create a product first to ensure data exists
        String requestBody = """
                {
                "name" : "Samsung TV",
                "description" : "Samsung TV - Model 2024",
                "price" : "2000.00"
                }
                """;

        // POST request to create a product
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201);

        // GET request to retrieve all products
        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))  // Correctly checking the size of the array
                .body("[0].name", Matchers.equalTo("Samsung TV"))
                .body("[0].description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("[0].price", Matchers.equalTo(2000.0F));  // Ensuring float comparison
    }
}
 /*void getAllProductTest(){
        String requestBody = """
                {
                "name" : "Samsung TV",
                "description" : "Samsung TV - Model 2024",
                "price" : "2000.00"

                }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id",  Matchers.notNullValue())
                .body("name" ,Matchers.equalTo("Samsung TV"))
                .body("description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("price", Matchers.equalTo(2000.0F));

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size", Matchers.greaterThan(0))
                .body("[0].name" ,Matchers.equalTo("Samsung TV"))
                .body("[0].description", Matchers.equalTo("Samsung TV - Model 2024"))
                .body("[0].price", Matchers.equalTo(2000.0F));
    }

}*/