/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sk.flooringmastery.service;

/**
 *
 * @author SHIVALI
 */
public class OrderNotFoundException extends Exception {

    OrderNotFoundException(String message) {
        super(message);
    }

    OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
