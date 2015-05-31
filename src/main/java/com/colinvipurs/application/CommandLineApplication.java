package com.colinvipurs.application;

import java.io.*;

/**
 * Entry point for the CommandLineApplication
 */
public class CommandLineApplication {
    private Timelines timelines = new Timelines();

    public CommandLineApplication(InputStream input, OutputStream output) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(output));
        String inputLine = null;
        while ((inputLine = reader.readLine()) != null) {
            if (inputLine.contains("->")) {
                String[] commandData = inputLine.split(" -> ");
                String user = commandData[0];
                String post = commandData[1];
                timelines.push(NewPost.of(user, post));
            } else if (!inputLine.equalsIgnoreCase("Exit")) {
                for (Post post : timelines.postsFor(inputLine)) {
                    bufferedWriter.write(post.describe() + "\n");
                }
            }
            bufferedWriter.flush();
        }
    }
}
