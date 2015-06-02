package com.colinvipurs.application.cli;

import com.colinvipurs.application.repositories.Followers;
import com.colinvipurs.application.repositories.TimeLines;

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
        CommandLineParser parser = new CommandLineParser(timelines, followers, writer);
        writer.write("> ");
        writer.flush();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            parser.commandFor(inputLine).execute();
            writer.write("> ");
            writer.flush();
        }
    }
}
