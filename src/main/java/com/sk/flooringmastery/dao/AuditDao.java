/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sk.flooringmastery.dao;

/**
 *
 * @author SHIVALI
 */
public interface AuditDao { 
    
    public void writeAuditEntry(String entry) throws FMPersistenceException;
    
}
