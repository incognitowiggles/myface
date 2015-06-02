package com.colinvipurs.application.usecases;

import com.colinvipurs.application.domain.NewPost;
import com.colinvipurs.application.repositories.TimeLines;

/**
 * Command to push a new post to a timeline
 */
public class PushToTimelineUseCase implements UseCase<Void> {
    private final TimeLines timelines;
    private final String user;
    private final String post;

    public PushToTimelineUseCase(TimeLines timelines, String user, String post) {
        this.timelines = timelines;
        this.user = user;
        this.post = post;
    }

    @Override
    public Void execute() {
        timelines.push(NewPost.of(user, post));
        return null;
    }
}
