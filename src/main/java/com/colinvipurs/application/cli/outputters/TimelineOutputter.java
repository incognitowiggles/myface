package com.colinvipurs.application.cli.outputters;

import com.colinvipurs.application.domain.Post;
import com.colinvipurs.application.domain.Timeline;
import com.colinvipurs.application.usecases.UseCase;
import com.colinvipurs.application.usecases.ViewTimelineUseCase;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Class to send the output of viewing a time line to the command line writer
 */
public class TimelineOutputter implements UseCase<Void> {
    private final ViewTimelineUseCase viewTimelineUseCase;
    private final BufferedWriter writer;

    public TimelineOutputter(ViewTimelineUseCase viewTimelineUseCase, BufferedWriter writer) {
        this.viewTimelineUseCase = viewTimelineUseCase;
        this.writer = writer;
    }

    @Override
    public Void execute() {
        Timeline timeline = viewTimelineUseCase.execute();
        timeline.forEach(post -> writePost(post));
        return null;
    }

    private void writePost(Post post) {
        try {
            writer.write(String.format("%s (%s)", post.body(), post.formattedTime()));
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
