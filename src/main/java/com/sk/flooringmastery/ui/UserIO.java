package com.sk.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {

    void print(String msg);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);
     
    String readState(String prompt);
    
    String readOptionalState(String prompt);
    
    String readStateName(String prompt);
    
    String readOptionalStateName(String prompt);

    BigDecimal readBigDecimal(String prompt);

    BigDecimal readOptionalBigDecimal(String prompt);

    LocalDate readLocalDate(String prompt);
    
   
    
    String readProduct(String prompt);
    
    String readOptionalProduct(String prompt);

    String readString(String prompt);

}
