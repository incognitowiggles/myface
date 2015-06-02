package com.colinvipurs.application.cli.outputters;

import com.colinvipurs.application.domain.Post;
import com.colinvipurs.application.domain.Timeline;
import com.colinvipurs.application.usecases.UseCase;
import com.colinvipurs.application.usecases.ViewWallUseCase;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Class to output a wall to the command line writer
 */
public class WallOutputter implements UseCase<Void> {
    private ViewWallUseCase viewWallCommand;
    private BufferedWriter writer;

    public WallOutputter(ViewWallUseCase viewWallCommand, BufferedWriter writer) {
        this.viewWallCommand = viewWallCommand;
        this.writer = writer;
    }

    @Override
    public Void execute() {
        Timeline timeline = viewWallCommand.execute();
        timeline.forEach(post -> writePost(post));
        return null;
    }

    private void writePost(Post post) {
        try {
            writer.write(String.format("%s - %s (%s)", post.user(), post.body(), post.formattedTime()));
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
