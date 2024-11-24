package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
@Slf4j

public class Routes {

    @Value("${services.product-url}")
    private String productServiceUrl;

    @Value("${services.order-url}")
    private String orderServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        log.info("Initializing product service route with the URL : {}", productServiceUrl);

        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product"), request -> {
                    log.info("Received request for product service : {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(productServiceUrl).handle(request);
                        log.info("Response status : {}", response.statusCode());
                        return response;
                    }catch(Exception e){
                        log.error("Error while handling request for product service : {}",e.getMessage(), e);
                        return  ServerResponse.status(500).body("Error while handling request for product service : "+e.getMessage());
                    }
                })
                .build();

    }
    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        log.info("Initializing order service route with the URL : {}", orderServiceUrl);

        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"), request -> {
                    log.info("Received request for order service : {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(orderServiceUrl).handle(request);
                        log.info("Response status : {}", response.statusCode());
                        return response;
                    }catch(Exception e){
                        log.error("Error while handling request for order service : {}",e.getMessage(), e);
                        return  ServerResponse.status(500).body("Error while handling request for order service ");
                    }
                })
                .build();


    }

}