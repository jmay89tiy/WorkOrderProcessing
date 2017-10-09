package com.company;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;




    public class Creator {
        public void createWorkOrders() {    //why doesn't it let me throw my exception?!
            Main.orderCount += 1;

            WorkOrder newOrder = new WorkOrder();

            Scanner scanner = new Scanner(System.in);

            System.out.println("Create a new Work Order:");

            System.out.println("Enter description of work requested:");

            newOrder.setDescription(scanner.nextLine());

            System.out.println("Enter your full name for our records:");

            newOrder.setSenderName(scanner.nextLine());

            newOrder.setStatus(Status.INITIAL);

            newOrder.setId(Main.orderCount);

            //
            // mapper below
            //

            String workOrder = "";
            ObjectMapper mapper = new ObjectMapper();
            try {
                workOrder = mapper.writeValueAsString(newOrder);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            String fileTitle = newOrder.getId() + ".json";

            //
            // try catch block below for filewriting
            //

            try {
                File file = new File(fileTitle);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(workOrder);
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            createWorkOrders();
        }

        //
        //creates the workOrders portion below
        //

        public static void main(String args[]) {
            com.company.Creator creator = new com.company.Creator();
            creator.createWorkOrders();
        }
    }
