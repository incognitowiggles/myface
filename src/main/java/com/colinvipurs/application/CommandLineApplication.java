package com.colinvipurs.application;

import java.io.*;

/**
 * Entry point for the CommandLineApplication
 */
public class CommandLineApplication {
    private final TimeLines timelines;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public CommandLineApplication(InputStream input, OutputStream output, TimeLines timelines) throws IOException {
        this.timelines = timelines;
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new BufferedWriter(new OutputStreamWriter(output));
    }

    public void runEventLoop() throws IOException {
        writer.write("> ");
        writer.flush();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            if (inputLine.equalsIgnoreCase("exit")) {
                return;
            }
            if (inputLine.contains("->")) {
                String[] commandData = inputLine.split(" -> ");
                String user = commandData[0];
                String post = commandData[1];
                timelines.push(NewPost.of(user, post));
            } else if (inputLine.contains("follow")) {
                // do nothing for now, but avoid the "Empty" message in output
            } else if (inputLine.endsWith("wall")) {
                for (Post post : timelines.postsFor("Charlie")) {
                    writer.write("Charlie - " + post.describe() + "\n");
                }
                for (Post post : timelines.postsFor("Alice")) {
                    writer.write("Alice - " + post.describe() + "\n");
                }
            } else {
                for (Post post : timelines.postsFor(inputLine)) {
                    writer.write(post.describe() + "\n");
                }
            }
            writer.write("> ");
            writer.flush();
        }
    }
}
