package com.colinvipurs.application.usecases;

import com.colinvipurs.application.domain.Timeline;
import com.colinvipurs.application.repositories.Followers;
import com.colinvipurs.application.repositories.TimeLines;

/**
 * Command to view the contents of a users wall, i.e. the aggregated view
 * of their own timeline and those of the users they are following
 */
public class ViewWallUseCase implements UseCase<Timeline> {
    private final TimeLines timelines;
    private Followers followers;
    private final String user;

    public ViewWallUseCase(TimeLines timelines, Followers followers, String user) {
        this.timelines = timelines;
        this.followers = followers;
        this.user = user;
    }

    @Override
    public Timeline execute() {
        return timelines.wallFor(user, followers.followees(user));
    }
}
