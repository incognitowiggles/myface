package com.colinvipurs.application;

import java.util.*;

/**
 * Class to hold timeline information for all users
 */
public class Timelines {
    private Map<String, List<Post>> posts = new HashMap<>();

    public void push(NewPost newPost) {
        posts.computeIfAbsent(newPost.getUser(), (user) -> new ArrayList<>())
                .add(PostWithBody.of(newPost.getPost(), newPost.getTime()));
    }

    public Timeline postsFor(String user) {
        List<Post> userPosts = posts.getOrDefault(user, Collections.EMPTY_LIST);
        return Timeline.withPosts(userPosts);
    }
}
