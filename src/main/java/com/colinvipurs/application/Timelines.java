package com.colinvipurs.application;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold timeline information for all users
 */
public class Timelines {
    private Map<String, Post> posts = new HashMap<>();

    public void push(NewPost newPost) {
        posts.put(newPost.getUser(), PostWithBody.of(newPost.getPost(), newPost.getTime()));
    }

    public Timeline postsFor(String user) {
        Post post = posts.get(user);
        if (post != null) {
            return Timeline.withPost(post);
        } else {
            return Timeline.empty();
        }
    }
}
