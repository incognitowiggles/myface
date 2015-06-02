package com.colinvipurs.application.usecases;

import com.colinvipurs.application.domain.Timeline;
import com.colinvipurs.application.repositories.TimeLines;

/**
 * Command to view an individual users timeline
 */
public class ViewTimelineUseCase implements UseCase<Timeline> {
    private final TimeLines timelines;
    private final String user;

    public ViewTimelineUseCase(TimeLines timelines, String user) {
        this.timelines = timelines;
        this.user = user;
    }

    @Override
    public Timeline execute() {
        return timelines.postsFor(user);
    }
}
