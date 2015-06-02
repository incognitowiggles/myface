package com.colinvipurs.application.domain;

import java.time.Instant;

/**
 * Container object for a new post to a users timeline
 */
public class NewPost {
    private String user;
    private final String post;
    private final Instant time;

    private NewPost(String user, String post, Instant time) {
        this.user = user;
        this.post = post;
        this.time = time;
    }

    public static NewPost of(String user, String post) {
        return new NewPost(user, post, Instant.now());
    }

    public String getUser() {
        return user;
    }

    public String getPost() {
        return post;
    }

    public Instant getTime() {
        return time;
    }
}
