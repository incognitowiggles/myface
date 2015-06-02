package com.colinvipurs.application.repositories;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryFollowers implements Followers {
    private Map<String, Set<String>> followers = new HashMap<>();

    @Override
    public void startFollowing(String userFollowing, String userToFollow) {
        followers.computeIfAbsent(userFollowing, (user) -> emptyFollowers())
                .add(userToFollow);
    }

    @Override
    public Set<String> followees(String user) {
        return followers.getOrDefault(user, emptyFollowers());
    }

    private Set<String> emptyFollowers() {
        return new HashSet<>();
    }
}
