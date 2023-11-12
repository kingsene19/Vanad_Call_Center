package com.dhp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReplayYear {

    private String directory;
    private ArrayList<Customer> allServedCustomers = new ArrayList<Customer>();

    public ReplayYear(String directory) {
        this.directory = directory;
    }

    public void simulateYear() throws IOException {
        String headerRow = "TYPE,ARRIVAL_TIME,SERVING_TIME,WAIT_TIME,WAVGC_LES,LES,AVGLES,AVGCLES,SERVEURS,Q0,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11,Q12,Q13,Q14,Q15,Q16,Q17,Q18,Q19,Q20,Q21,Q22,Q23,Q24,Q25,Q26";
        ArrayList<String> csvList = new ArrayList<String>();
        File directory = new File(this.directory);
        if (directory.isDirectory()) {
            File[] subDirectories = directory.listFiles();
            if (subDirectories != null) {
                for (File subDirectory : subDirectories) {
                    if (subDirectory.isDirectory()) {
                        File[] files = subDirectory.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                if (file.isFile()) {
                                    try {
                                        ReplayOneDay replayOneDay = new ReplayOneDay();
                                        replayOneDay.simulateOneDay(file.getAbsolutePath());
                                        this.allServedCustomers.addAll(replayOneDay.getServedCustomers());
                                    } catch (IllegalArgumentException e) {
                                        System.out.println(file.getAbsolutePath());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Customer c: this.allServedCustomers) {
            csvList.add(c.toString());
        }
        writeArrayListToCSV(csvList, "data/result.csv", headerRow);
    }

    public void writeArrayListToCSV(ArrayList<String> arrayList, String filePath, String headerRow) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(headerRow);
            writer.newLine();

            for (String element : arrayList) {
                writer.write(element);
                writer.newLine();
            }
            System.out.println("ArrayList elements with header row written to CSV successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing ArrayList elements to CSV: " + e.getMessage());
        }
    }
}
