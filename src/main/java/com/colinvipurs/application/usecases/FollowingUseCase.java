package com.colinvipurs.application.usecases;

import com.colinvipurs.application.repositories.Followers;

/**
 * Command to follow another user
 */
public class FollowingUseCase implements UseCase<Void> {
    private final Followers followers;
    private final String user;
    private final String userToFollow;

    public FollowingUseCase(Followers followers, String user, String userToFollow) {
        this.followers = followers;
        this.user = user;
        this.userToFollow = userToFollow;
    }

    @Override
    public Void execute() {
        followers.startFollowing(user, userToFollow);
        return null;
    }
}
