package com.dhp;

public class Customer {

    private int type;
    private double arrivalTime;
    private double servingTime;
    private double waitTime;
    private double LES;
    private double AvgLES;
    private double AvgCLES;
    private double WAvgCLES;
    private boolean isServed = false;

    private int[] queueLengths = new int[27];
    private int serveurs;

    public Customer() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public double getLES() {
        return LES;
    }

    public void setLES(double LES) {
        this.LES = LES;
    }

    public double getAvgLES() {
        return AvgLES;
    }

    public void setAvgLES(double avgLES) {
        AvgLES = avgLES;
    }

    public double getAvgCLES() {
        return AvgCLES;
    }

    public void setAvgCLES(double avgCLES) {
        AvgCLES = avgCLES;
    }

    public double getWAvgCLES() {
        return WAvgCLES;
    }

    public void setWAvgCLES(double WAvgCLES) {
        this.WAvgCLES = WAvgCLES;
    }

    public boolean isServed() {
        return isServed;
    }

    public void setIsServed(boolean isServed) {
        this.isServed = isServed;
    }

    public int[] getQueueLengths() {
        return queueLengths;
    }

    public void setQueueLengths(int[] queueLengths) {
        this.queueLengths = queueLengths;
    }

    public int getServeurs() {
        return serveurs;
    }

    public void setServeurs(int serveurs) {
        this.serveurs = serveurs;
    }

    public double getServingTime() {
        return servingTime;
    }

    public void setServingTime(double servingTime) {
        this.servingTime = servingTime;
    }

    @Override
    public String toString() {
        return type+
                ","+ arrivalTime +
                "," + servingTime +
                "," + waitTime +
                "," + WAvgCLES +
                "," + LES +
                "," + AvgLES +
                "," + AvgCLES +
                "," + serveurs +
                "," + queueLengths[0] +
                "," + queueLengths[1] +
                "," + queueLengths[2] +
                "," + queueLengths[3] +
                "," + queueLengths[4] +
                "," + queueLengths[5] +
                "," + queueLengths[6] +
                "," + queueLengths[7] +
                "," + queueLengths[8] +
                "," + queueLengths[9] +
                "," + queueLengths[10] +
                "," + queueLengths[11] +
                "," + queueLengths[12] +
                "," + queueLengths[13] +
                "," + queueLengths[14] +
                "," + queueLengths[15] +
                "," + queueLengths[16] +
                "," + queueLengths[17] +
                "," + queueLengths[18] +
                "," + queueLengths[19] +
                "," + queueLengths[20] +
                "," + queueLengths[21] +
                "," + queueLengths[22] +
                "," + queueLengths[23] +
                "," + queueLengths[24] +
                "," + queueLengths[25] +
                "," + queueLengths[26];
    }
}