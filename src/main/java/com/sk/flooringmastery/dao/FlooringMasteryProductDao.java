
package com.sk.flooringmastery.dao;

import com.sk.flooringmastery.dto.Product;

import java.util.List;

public interface FlooringMasteryProductDao {
    
 List<Product> loadProductList() throws FMPersistenceException; 
    
}
