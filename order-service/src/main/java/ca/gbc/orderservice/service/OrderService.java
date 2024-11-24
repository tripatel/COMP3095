package ca.gbc.orderservice.service;

import ca.gbc.orderservice.dto.OrderRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;



public interface OrderService {

    void placeOrder(OrderRequest orderRequest);

}