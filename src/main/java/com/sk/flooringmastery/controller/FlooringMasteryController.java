package com.sk.flooringmastery.controller;

import com.sk.flooringmastery.dao.FMPersistenceException;
import com.sk.flooringmastery.dto.Order;
import com.sk.flooringmastery.service.CustomernamenullvalidationException;
import com.sk.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.sk.flooringmastery.ui.FlooringMasteryView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private void createOrder() throws FMPersistenceException {
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

        } catch (CustomernamenullvalidationException ex) {
            System.out.println("\n");
            System.out.println(ex);
        }
    }

    private void editOrder() throws FMPersistenceException {
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
        } catch (CustomernamenullvalidationException ex) {
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

    private void exitMessage() throws FMPersistenceException  {

        String exitMessage = view.exitMessage();
        if (exitMessage.equals("Y")) {

            // create instance of directory
            File dir = new File("orders");

            // create object of PrintWriter for output file
            PrintWriter pw;
            try {
                pw = new PrintWriter("Databackup/Exporteddata.txt");
            } catch (FileNotFoundException ex) {
                throw new FMPersistenceException ("Error occured while writing to file",  ex);
            }

            // Get list of all the files in form of String Array
            String[] fileNames = dir.list();

            // loop for reading the contents of all the files
            // in the directory GeeksForGeeks
            for (String fileName : fileNames) {
                System.out.println("Exporting  from " + fileName);

                // create instance of file from Name of
                // the file stored in string Array
                File f = new File(dir, fileName);

                // create object of BufferedReader
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(f));
                } catch (FileNotFoundException ex) {
                    throw new FMPersistenceException ("Could not read files from folder" , ex);
                }
                pw.println("Order data of file " + fileName);

                // Read from current file
                String line;
                try {
                    line = br.readLine();
                } catch (IOException ex) {
                    throw new FMPersistenceException ("Could not read files from folder" ,  ex);
                }
                while (line != null) {

                    // write to the output file
                    pw.println(line);
                    try {
                        line = br.readLine();
                    } catch (IOException ex) {
                        throw new FMPersistenceException ("Error!", ex);
                    }
                }
                pw.flush();
            }
            System.out.println("Exported  from all files"
                    + " in folder :  " + dir.getName() );
            
            System.out.println(" ");

            view.orderExportsucessBanner();
        } else {

            System.out.println("Thanks for Visiting! See You Next Time!");

        }

    }
}
