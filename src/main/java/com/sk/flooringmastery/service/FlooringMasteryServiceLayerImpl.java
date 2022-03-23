package com.sk.flooringmastery.service;

import com.sk.flooringmastery.dao.AuditDao;
import com.sk.flooringmastery.dao.FMPersistenceException;
import com.sk.flooringmastery.dao.FlooringMasteryOrderDao;
import com.sk.flooringmastery.dao.FlooringMasteryProductDao;
import com.sk.flooringmastery.dao.FlooringMasteryTaxDao;
import com.sk.flooringmastery.dto.Order;
import com.sk.flooringmastery.dto.Product;
import com.sk.flooringmastery.dto.Tax;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_DOWN;
import static java.math.RoundingMode.HALF_UP;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryServiceLayerImpl implements FlooingMasteryServiceLayer {

    private FlooringMasteryOrderDao orderDao;
    private FlooringMasteryProductDao productDao;
    private FlooringMasteryTaxDao taxDao;
    private AuditDao auditDao;

    public FlooringMasteryServiceLayerImpl(FlooringMasteryOrderDao dao1, FlooringMasteryProductDao dao2,
            FlooringMasteryTaxDao dao3, AuditDao auditDao) {
        this.orderDao = dao1;
        this.productDao = dao2;
        this.taxDao = dao3;
        this.auditDao = auditDao;
    }

    public Order addOrder(Order order) throws FMPersistenceException,CustomernamenullvalidationException {
        if (order.getCustomerName().equals("")) {
            throw new CustomernamenullvalidationException("You must Enter a Customer Name!");
        } else {
            orderDao.addOrder(order);

        }
        return order;
    }

    @Override
    public void removeOrder(LocalDate date, int orderNumber) throws FMPersistenceException {
        orderDao.removeOrder(date, orderNumber);
        auditDao.writeAuditEntry("Order  " + orderNumber + " of " + date + " Removed.");
    }

    @Override
    public List<Order> searchOrders(LocalDate date) {
        return orderDao.searchOrders(date);
    }

    @Override
    public Order getOrder(List<Order> orderList, int orderNumber) {
        return orderDao.getOrder(orderList, orderNumber);
    }

    @Override
    public void editOrder(LocalDate date, Order order) throws FMPersistenceException,CustomernamenullvalidationException {
        if (order.getCustomerName().equals("")) {
            throw new CustomernamenullvalidationException ("You must Enter a Customer Name!");
        } else {
            orderDao.editOrder(date, order);
        }
    }

    

    @Override
    public Order calculateCost(Order order) throws FMPersistenceException {

        BigDecimal taxRate = new BigDecimal(0);
        BigDecimal area = order.getArea();
        List<Tax> taxes = taxDao.loadTaxRates();
        List<Product> products = productDao.loadProductList();

        if (area.compareTo(BigDecimal.ZERO) >= 0) {

            for (Tax t : taxes) {
                if (t.getState().equals(order.getState())) {
                    taxRate = t.getTaxRate();
                }
            }

            for (Product p : products) {

                if (p.getProductType().equals(order.getProductType())) {

                    BigDecimal costSqFt = (p.getCostPerSqFt());
                    BigDecimal laborCostSqFt = (p.getLaborCostPerSqFt());

                    BigDecimal material = area.multiply(costSqFt);
                    BigDecimal materialCost = material.setScale(2, HALF_UP);

                    BigDecimal labor = area.multiply(laborCostSqFt);
                    BigDecimal laborCost = labor.setScale(2, HALF_UP);

                    BigDecimal tax = (materialCost.add(laborCost)).multiply(taxRate.divide(new BigDecimal(100.00)));

                    BigDecimal taxCost = tax.setScale(2, HALF_DOWN);

                    BigDecimal totalCost = (materialCost.add(laborCost).add(tax));

                    order.setTaxRate(taxRate);
                    order.setCostPerSqFt(costSqFt);
                    order.setLaborCostPerSqFt(laborCostSqFt);
                    order.setMaterialCost(materialCost);
                    order.setLaborCost(laborCost);
                    order.setTotalTax(taxCost);
                    order.setTotalCost(totalCost);
                }
            }
        } else {
            throw new FMPersistenceException("Area Must be Larger than Zero!");
        }

        return order;
    }

}
