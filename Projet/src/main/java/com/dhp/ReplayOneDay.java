package com.dhp;

import umontreal.ssj.simevents.Event;
import umontreal.ssj.simevents.Sim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ReplayOneDay {
    static final double HOUR = 3600;
    static final int PERIODE = 13;
    static final double TIME = PERIODE * HOUR;
    private LinkedList<Customer> waitList = new LinkedList<Customer>();
    private ArrayList<Customer> servedCustomers = new ArrayList<Customer>();
    private ArrayList<Customer> abandonedCustomers = new ArrayList<Customer>();
    private int nbServeurs;
    private int[] lenghtsQtype = new int[27];
    private double[] arrayLES = new double[27];
    private LinkedList[] arrayAvgLES = new LinkedList[27];
    private LinkedList[][] arrayAvgCLES = new LinkedList[27][100];
    private double[][] arrayWAvgCLES = new double[27][100];

    public ReplayOneDay() {
        for (int i=0;i<27;i++) {
            arrayAvgLES[i] = new LinkedList<Double>();
            for (int j=0;j<100;j++) {
                arrayAvgCLES[i][j] = new LinkedList<Double>();
                arrayWAvgCLES[i][j] = -1.0;
            }
        }
    }

    public void createCustomerOfDay(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        String line = br.readLine();
        while (line != null) {
            Customer c = new Customer();
            String[] tab = line.split(",");
            c.setArrivalTime(getTime(tab[0]));
            c.setType(Integer.parseInt(tab[1]));
            c.setWaitTime(getWaitingTime(tab[0], tab[3], tab[6]));
            if (!tab[3].equals("NULL")) {
                c.setIsServed(true);
                try {
                    c.setServingTime(getTime(tab[6])-getTime(tab[3]));
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    c.setServingTime(getTime(tab[3])-getTime(tab[0]));
                }
            }
            new Arrival(c).schedule(c.getArrivalTime());
            line=br.readLine();
        }
        br.close();
    }

    public double getTime(String s) {
        String s1 = s.split(" ")[1];
        String[] time = s1.split(":");
        return (Integer.parseInt(time[0])-7)*3600+Integer.parseInt(time[1])*60+Integer.parseInt(time[2]);
    }

    public double getWaitingTime(String arrival, String answered, String hangup) {
        if (!answered.equals("NULL")) {
            return getTime(answered) - getTime(arrival);
        }
        return getTime(hangup) - getTime(arrival);
    }

    public class Arrival extends Event {
        private Customer c;

        public Arrival(Customer cust) {
            this.c  = cust;
        }

        public void actions() {
            c.setLES(arrayLES[c.getType()]);
            c.setAvgLES(getAvgLES(arrayAvgLES[c.getType()]));
            c.setAvgCLES(getAvgCLES(arrayAvgCLES[c.getType()], lenghtsQtype[c.getType()]));
            c.setWAvgCLES(getWAvgCLES(c.getType(), lenghtsQtype[c.getType()]));
            c.setServeurs(nbServeurs);
            c.setQueueLengths(lenghtsQtype);
            waitList.push(c);
            lenghtsQtype[c.getType()] += 1;
            new Departure(c).schedule(c.getWaitTime());
        }
    }

    public class Departure extends Event {
        private Customer c;

        public Departure(Customer cust) {
            this.c = cust;
        }

        public void actions() {
            waitList.remove(c);
            lenghtsQtype[c.getType()] -= 1;
            arrayLES[c.getType()] = c.getWaitTime();
            setAvgLES(c.getWaitTime(), c.getType());
            setAvgCLES(c.getWaitTime(), c.getType(), c.getQueueLengths()[c.getType()]);
            setWAvgCLES(c.getType(), c.getQueueLengths()[c.getType()], c.getWaitTime());
            if (!c.isServed()) {
                abandonedCustomers.add(c);
            } else {
                nbServeurs += 1;
                new EndOfSim(c).schedule(c.getServingTime());
            }
        }
    }

    public class EndOfSim extends Event {
        private Customer c;

        public EndOfSim(Customer cust) {
            this.c  = cust;
        }

        public void actions() {
            servedCustomers.add(c);
            nbServeurs -= 1;
        }
    }

    public class EndOfDaySim extends Event {
        public void actions() {
            Sim.stop();
        }
    }

    public void simulateOneDay(String file) throws IOException {
        Sim.init();
        createCustomerOfDay(file);
        new EndOfDaySim().schedule(TIME);
        Sim.start();
    }


    public double getAvgLES(LinkedList<Double> arrayAvgLES) {
        double sum = 0;
        for (double val: arrayAvgLES) {
            sum += val;
        }
        return sum/arrayAvgLES.size();
    }

    public double getAvgCLES(LinkedList[] arrayAvgCLES, int queueLength) {
        LinkedList<Double> array_avgCLES = arrayAvgCLES[queueLength];
        return getAvgLES(array_avgCLES);
    }

    public double getWAvgCLES(int type, int queueLength) {
        return arrayWAvgCLES[type][queueLength];
    }

    public void setAvgLES(double waitTime, int type) {
        if (arrayAvgLES[type].size() < 10) {
            arrayAvgLES[type].addLast(waitTime);
        } else {
            arrayAvgLES[type].removeFirst();
            arrayAvgLES[type].addLast(waitTime);
        }
    }

    public void setAvgCLES(double waitTime, int type, int queueLength) {
        if (arrayAvgCLES[type][queueLength].size() < 100) {
            this.arrayAvgCLES[type][queueLength].addLast(waitTime);
        } else {
            this.arrayAvgCLES[type][queueLength].removeFirst();
            this.arrayAvgCLES[type][queueLength].addLast(waitTime);
        }
    }

    public void setWAvgCLES(int type, int queueLength, double waitTime) {
        double alpha = 0.2;
        if (this.arrayWAvgCLES[type][queueLength] == -1.0) {
            arrayWAvgCLES[type][queueLength] = waitTime;
        } else {
            double wavgCLES = (1-alpha)*arrayWAvgCLES[type][queueLength] + alpha*waitTime;
            this.arrayWAvgCLES[type][queueLength] = wavgCLES;
        }
    }

    public ArrayList<Customer> getServedCustomers() {
        return servedCustomers;
    }

}
