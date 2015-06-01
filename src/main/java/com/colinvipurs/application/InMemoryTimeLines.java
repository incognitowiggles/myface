package com.colinvipurs.application;

import java.util.*;

/**
 * Class to hold timeline information for all users
 */
public class InMemoryTimeLines implements TimeLines {
    private Map<String, List<Post>> posts = new HashMap<>();

    @Override
    public void push(NewPost newPost) {
        posts.computeIfAbsent(newPost.getUser(), (user) -> new LinkedList<>())
                .add(0, PostWithBody.of(newPost.getPost(), newPost.getTime()));
    }

    @Override
    public Timeline postsFor(String user) {
        List<Post> userPosts = posts.getOrDefault(user, Collections.EMPTY_LIST);
        return Timeline.withPosts(userPosts);
    }
}
