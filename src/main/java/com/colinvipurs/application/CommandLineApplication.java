package com.colinvipurs.application;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entry point for the CommandLineApplication
 */
public class CommandLineApplication {
    private boolean aliceHasTimeline = false;

    public CommandLineApplication(InputStream input, OutputStream output) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(output));
        String inputLine = null;
        while ((inputLine = reader.readLine()) != null) {
            if (inputLine.contains("Alice ->")) {
                aliceHasTimeline = true;
            } else if (inputLine.equals("Alice") && aliceHasTimeline) {
                bufferedWriter.write("I am alive! (Just now)\n");
            } else if (!inputLine.equalsIgnoreCase("Exit")) {
                bufferedWriter.write("Empty\n");
            }
            bufferedWriter.flush();
        }
    }
}
