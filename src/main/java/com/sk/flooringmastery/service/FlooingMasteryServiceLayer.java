/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sk.flooringmastery.service;

import com.sk.flooringmastery.dao.FMPersistenceException;
import com.sk.flooringmastery.dto.Order;
import com.sk.flooringmastery.dto.Product;
import com.sk.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_DOWN;
import static java.math.RoundingMode.HALF_UP;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author SHIVALI
 */
public interface  FlooingMasteryServiceLayer {
    
     Order addOrder(Order order) throws FMPersistenceException ;
    
     void removeOrder(LocalDate date, int orderNumber) throws FMPersistenceException ;

    List<Order> searchOrders(LocalDate date); 

    Order getOrder(List<Order> orderList, int orderNumber)throws FMPersistenceException  ;
    

     void editOrder(LocalDate date, Order order) throws FMPersistenceException ;
        
    

    Order calculateCost(Order order) throws FMPersistenceException ;

        

       
    
}
