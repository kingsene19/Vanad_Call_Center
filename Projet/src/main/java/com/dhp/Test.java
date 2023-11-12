package com.dhp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import umontreal.ssj.simevents.Event;
import umontreal.ssj.simevents.Sim;

public class Test {

    public static void main(String[] args) throws IOException {
        ReplayYear replayYear = new ReplayYear("data");
        replayYear.simulateYear();
    }
}
