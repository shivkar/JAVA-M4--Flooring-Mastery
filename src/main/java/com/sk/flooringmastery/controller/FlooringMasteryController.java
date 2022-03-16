package com.sk.flooringmastery.controller;

import com.sk.flooringmastery.dao.FMPersistenceException;
import com.sk.flooringmastery.dto.Order;
import com.sk.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.sk.flooringmastery.ui.FlooringMasteryView;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {

    private FlooringMasteryView view;
    private FlooringMasteryServiceLayerImpl service;

    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceLayerImpl service) {
        this.view = view;
        this.service = service;
    }

    public void run() {

        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            System.out.println("\n");
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        searchOrders();
                        break;
                    case 2:
                        createOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }

            exitMessage();
        } catch (FMPersistenceException e) {
            System.out.println("ERROR" + e);
        }
    }

    private int getMenuSelection() throws FMPersistenceException {
        return view.getSelection();
    }

    private void searchOrders() {
        view.SearchBanner();
        LocalDate date = view.getDate();
        List<Order> orderedDates = service.searchOrders(date);
        view.displayByDate(date, orderedDates);

    }

    private void createOrder() {
        try {
            view.createOrderBanner();
            Order order = view.getOrderData();
            Order completedOrder = service.calculateCost(order);
            view.orderSummary(completedOrder);
            String accepted = view.acceptOrder();
            if (accepted.equals("Y")) {
                service.addOrder(completedOrder);
                view.orderSuccesfullBanner();
            } else {
                view.orderNotSavedBanner();
            }

        } catch (FMPersistenceException ex) {
            System.out.println("\n");
            System.out.println(ex);
        }
    }

    private void editOrder() {
        try {
            view.editBanner();
            LocalDate date = view.getDate();
            int orderNumber = view.getOrderNum();
            List<Order> orderedDates = service.searchOrders(date);
            Order updated = service.getOrder(orderedDates, orderNumber);
            Order updatedOrder = view.editOrder(updated);
            Order finalOrder = service.calculateCost(updatedOrder);
            view.orderSummary(finalOrder);
            service.editOrder(date, finalOrder);
            view.editSuccessBanner();
        } catch (FMPersistenceException ex) {
            System.out.println("\n");
            System.out.println(ex);

        }
    }

    private void removeOrder() {
        view.removeBanner();
        LocalDate date = view.getDate();
        int orderNumber = view.getOrderNum();
        try {
            service.removeOrder(date, orderNumber);
            view.removeSucessBanner();
        } catch (FMPersistenceException ex) {
            System.out.println("Order Not Removed!");
        }
    }

    private void unknownCommand() {
        System.out.println("Unknown Command! Please Enter a Valid Selection");
    }

    private void exitMessage() {
        System.out.println("Thanks for Visiting! See You Next Time!");
    }
}
