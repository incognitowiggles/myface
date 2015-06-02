package com.colinvipurs.application.cli;

import com.colinvipurs.application.cli.outputters.TimelineOutputter;
import com.colinvipurs.application.cli.outputters.WallOutputter;
import com.colinvipurs.application.repositories.Followers;
import com.colinvipurs.application.repositories.TimeLines;
import com.colinvipurs.application.usecases.*;

import java.io.BufferedWriter;

/**
 * Class to handle all the nitty gritty of parsing command line input
 */
public class CommandLineParser {
    private final TimeLines timelines;
    private final Followers followers;
    private BufferedWriter writer;

    public CommandLineParser(TimeLines timelines, Followers followers, BufferedWriter writer) {
        this.timelines = timelines;
        this.followers = followers;
        this.writer = writer;
    }

    public UseCase commandFor(String inputLine) {
        if (inputLine.contains("->")) {
            String[] commandData = inputLine.split(" -> ");
            return new PushToTimelineUseCase(timelines, commandData[0], commandData[1]);
        }
        if (inputLine.contains("follows")) {
            String[] commandData = inputLine.split(" follows ");
            String user = commandData[0];
            String userToFollow = commandData[1];
            return new FollowingUseCase(followers, user, userToFollow);
        }
        if (inputLine.endsWith("wall")) {
            String user = inputLine.split(" wall")[0];
            return new WallOutputter(new ViewWallUseCase(timelines, followers, user), writer);
        }
        return new TimelineOutputter(new ViewTimelineUseCase(timelines, inputLine), writer);
    }
}
