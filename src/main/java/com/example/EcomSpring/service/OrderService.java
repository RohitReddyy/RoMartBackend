package com.example.EcomSpring.service;

import com.example.EcomSpring.model.Order;
import com.example.EcomSpring.model.OrderItem;
import com.example.EcomSpring.model.Product;
import com.example.EcomSpring.model.dto.OrderItemRequest;
import com.example.EcomSpring.model.dto.OrderItemResponse;
import com.example.EcomSpring.model.dto.OrderRequest;
import com.example.EcomSpring.model.dto.OrderResponse;
import com.example.EcomSpring.repo.OrderRepo;
import com.example.EcomSpring.repo.ProductRepo;
import jakarta.persistence.Lob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;

    public OrderResponse postOrder(OrderRequest request) {

        Order order = new Order();
        String oid = "ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(oid);
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemRequest item : request.items()){
            OrderItem orderItem = new OrderItem();
            Product product = productRepo.findById(item.productId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));
            product.setStockQuantity(product.getStockQuantity() - item.quantity());
            productRepo.save(product);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.quantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> responseItems = new ArrayList<>();

        for(OrderItem item: orderItems){
            OrderItemResponse itemResponse = new OrderItemResponse(
            item.getProduct().getName(),
            item.getQuantity(),
            item.getPrice()
            );
            responseItems.add(itemResponse);
        }


        OrderResponse orderResponse = new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                responseItems
        );



        return orderResponse;
    }

    public List<OrderResponse> getAllResponseOrders() {
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> response = new ArrayList<>();



        for(Order order: orders){
            List<OrderItemResponse> responseItems = new ArrayList<>();

            for(OrderItem item: order.getItems()){
                OrderItemResponse itemResponse = new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()
                );
                responseItems.add(itemResponse);
            }

            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    responseItems
            );

            response.add(orderResponse);
        }



        return response;
    }
}
