package com.example.EcomSpring.repo;

import com.example.EcomSpring.model.Order;
import com.example.EcomSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    Order findByOrderId(String orderId);
}
