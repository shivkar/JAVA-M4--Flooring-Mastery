package com.sk.flooringmastery.service;

import com.sk.flooringmastery.dao.AuditDao;
import com.sk.flooringmastery.dao.AuditDaoFileImpl;
import com.sk.flooringmastery.dao.FMPersistenceException;
import com.sk.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.sk.flooringmastery.dao.FlooringMasteryOrderDao;
import com.sk.flooringmastery.dao.FlooringMasteryOrderDaoStub;
import com.sk.flooringmastery.dao.FlooringMasteryProductDao;
import com.sk.flooringmastery.dao.FlooringMasteryProductDaoImpl;
import com.sk.flooringmastery.dao.FlooringMasteryTaxDao;
import com.sk.flooringmastery.dao.FlooringMasteryTaxDaoImpl;
import com.sk.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class FlooringMasteryServiceLayerImplTest {

    FlooringMasteryOrderDao dao1 = new FlooringMasteryOrderDaoStub();
    FlooringMasteryProductDao dao2 = new FlooringMasteryProductDaoImpl();
    FlooringMasteryTaxDao dao3 = new FlooringMasteryTaxDaoImpl();
    AuditDao auditDao = new AuditDaoFileImpl();
    FlooringMasteryServiceLayerImpl service = new FlooringMasteryServiceLayerImpl(dao1, dao2, dao3,auditDao);
    LocalDate date = LocalDate.of(2022, Month.MARCH, 14);
   
    public FlooringMasteryServiceLayerImplTest() {
        
    }

    @Before
    public void setUp() throws FMPersistenceException  {

        List<Order> orders = dao1.searchOrders(date);

        for (Order order : orders) {
            dao1.removeOrder(date, order.getOrderNumber());
        }
    }

    @Test
    public void testAreaLessThanZero() throws FMPersistenceException {
        // ARRANGE
        List<Order> itemList = dao1.searchOrders(date);
        Order stubOrder = new Order();
        BigDecimal area = new BigDecimal(-25);

        stubOrder.setCustomerName("Sima Inc.");
        stubOrder.setState("WA");
        stubOrder.setProductType("Wood");
        stubOrder.setArea(area);
        // ACT
        try {
            service.calculateCost(stubOrder);
        // ASSERT
            fail("Expected Exception was not thrown.");
        } catch (FMPersistenceException ex) {

        }
    }

    @Test
    public void testCustomerNameNull() throws FMPersistenceException {
   // ARRANGE
        List<Order> itemList = dao1.searchOrders(date);
        Order stubOrder = new Order();
        BigDecimal area = new BigDecimal(25);

        stubOrder.setCustomerName("");
        stubOrder.setState("KY");
        stubOrder.setProductType("Wood");
        stubOrder.setArea(area);
  // ACT
        try {
            service.addOrder(stubOrder);
   // ASSERT
            fail("Expected Exception was not thrown.");
        } catch (FMPersistenceException ex) {

        }

    }
}
