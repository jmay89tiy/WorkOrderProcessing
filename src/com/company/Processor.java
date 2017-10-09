package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.company.Status.IN_PROGRESS;

public class Processor {

    public Map<Status, HashSet> orderMap = new HashMap<>();

    public ObjectMapper mapper = new ObjectMapper();

    public void processWorkOrders() {
        moveIt();
        readIt();

        //
        // below is the 5 second interval in which the program waits before triggering method below
        //

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processWorkOrders();
    }



    private void moveIt() {
        //
        // move work orders in map from one state to another
        //

        System.out.println("The Map: " + orderMap);
        Set<WorkOrder> tempSet = orderMap.get(IN_PROGRESS);
        Set<WorkOrder> completo = new HashSet<>();

        if (tempSet != null) {
            if (orderMap.get(Status.DONE) != null) {
                completo = orderMap.get(Status.DONE);
            }

            for (WorkOrder item : tempSet) {
                item.setStatus(Status.DONE);
                completo.add(item);
            }

            orderMap.put(Status.DONE, (HashSet) completo);
            orderMap.remove(IN_PROGRESS, tempSet);
        }

        Set<WorkOrder> tempSet2 = orderMap.get(Status.ASSIGNED);

        if (tempSet2 != null) {
            for (WorkOrder item : tempSet2) {
                item.setStatus(IN_PROGRESS);
            }

            orderMap.put(IN_PROGRESS, (HashSet) tempSet2);
            orderMap.remove(Status.ASSIGNED, tempSet2);
        }

        Set<WorkOrder> tempSet3 = orderMap.get(Status.INITIAL);
        if (tempSet3 != null) {
            for (WorkOrder item : tempSet3) {
                item.setStatus(Status.ASSIGNED);
            }

            orderMap.put(Status.ASSIGNED, (HashSet) tempSet3);
            orderMap.remove(Status.INITIAL, tempSet3);
        }
    }




    //
    // this block of code reads json below
    //

    private void readIt() {
        File currentDirectory = new File(".");
        File files[] = currentDirectory.listFiles();
        List<String> fileContents = new ArrayList<>();
        Set<WorkOrder> orderSet = new HashSet<>();

        for (File f : files) {
            if (f.getName().endsWith(".json")) {
                try {
                    Scanner fileScanner = new Scanner(f);
                    while (fileScanner.hasNext()) {
                        fileContents.add(fileScanner.nextLine());
                    }

                } catch (FileNotFoundException ex) {
                    System.out.println("Could not locate file *" + f + "*");
                    ex.printStackTrace();
                }
                f.delete();
            }
        }
        String [] orders = fileContents.toArray(new String[0]);
        for (int i = 0; i < orders.length; i++)
            try {
                WorkOrder readOrder = mapper.readValue(orders[i], WorkOrder.class);
                System.out.println("New Work Order has arrived " + readOrder.toString());
                orderSet.add(readOrder);
                orderMap.put(Status.INITIAL, (HashSet) orderSet);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

    public static void main(String args[]) {
        Processor processor = new Processor();
        processor.processWorkOrders();
    }
}
