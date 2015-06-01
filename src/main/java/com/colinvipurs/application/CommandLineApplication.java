package com.colinvipurs.application;

import java.io.*;

/**
 * Entry point for the CommandLineApplication
 */
public class CommandLineApplication {
    private final TimeLines timelines;
    private final Followers followers;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public CommandLineApplication(InputStream input, OutputStream output, TimeLines timelines, Followers followers) throws IOException {
        this.timelines = timelines;
        this.followers = followers;
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
            } else if (inputLine.contains("follows")) {
                String[] commandData = inputLine.split(" follows ");
                String user = commandData[0];
                String userToFollow = commandData[1];
                followers.startFollowing(user, userToFollow);
            } else if (inputLine.endsWith("wall")) {
                String user = inputLine.split(" wall")[0];
                for (Post post : timelines.wallFor(user, followers.followees(user))) {
                    writer.write(post.describe() + "\n");
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
