package com.colinvipurs.application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Timeline for a single user
 */
public class Timeline implements Iterable<Post> {
    private final List<Post> posts;

    private Timeline(List<Post> posts) {
        this.posts = posts;
    }

    private Timeline() {
        this.posts = new ArrayList<>();
        posts.add(Post.empty());
    }

    @Override
    public Iterator<Post> iterator() {
        return posts.iterator();
    }

    public static Timeline empty() {
        return new Timeline();
    }

    public static Timeline withPosts(List<Post> userPosts) {
        return userPosts.size() > 0 ? new Timeline(userPosts) : empty();
    }
}
