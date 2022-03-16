package com.sk.flooringmastery.dao;


import com.sk.flooringmastery.dto.Tax;
import java.util.List;

 public interface FlooringMasteryTaxDao {

    List<Tax> loadTaxRates() throws FMPersistenceException;
}
