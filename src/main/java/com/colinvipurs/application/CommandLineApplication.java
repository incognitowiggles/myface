package com.colinvipurs.application;

import java.io.*;

/**
 * Entry point for the CommandLineApplication
 */
public class CommandLineApplication {
    public CommandLineApplication(InputStream input, OutputStream output) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(output));
        String inputLine = null;
        while ((inputLine = reader.readLine()) != null) {
            if (!inputLine.equalsIgnoreCase("Exit")) {
                bufferedWriter.write("Empty\n");
                bufferedWriter.flush();
            }
        }
    }
}
