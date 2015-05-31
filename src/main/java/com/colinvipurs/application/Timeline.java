package com.colinvipurs.application;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Timeline for a single user
 */
public class Timeline implements Iterable<Post> {
    private final ArrayList<Post> posts = new ArrayList<>();

    private Timeline(Post post) {
        posts.add(post);
    }

    private Timeline() {
        // nothing to do here
    }

    public static Timeline withPost(Post post) {
        return new Timeline(post);
    }

    @Override
    public Iterator<Post> iterator() {
        return posts.iterator();
    }

    public static Timeline empty() {
        return new Timeline(Post.empty());
    }
}
