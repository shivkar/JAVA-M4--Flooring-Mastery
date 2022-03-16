
package com.sk.flooringmastery.dao;

import com.sk.flooringmastery.dto.Order;
import java.util.List;
import java.time.LocalDate;

public interface FlooringMasteryOrderDao {
    
    Order addOrder(Order order);

    List<Order> searchOrders(LocalDate date);
    
    void removeOrder(LocalDate date, int orderNumber);
    
    void editOrder (LocalDate date ,Order order);
    
    Order getOrder(List<Order> orderList, int orderNumber);
    
}
