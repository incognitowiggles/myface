package com.colinvipurs.application;

import java.time.Instant;

/**
 * An existing post
 */
public class PostWithBody implements Post {
    private final String body;
    private final Instant time;

    private PostWithBody(String body, Instant time) {
        this.body = body;
        this.time = time;
    }

    public static Post of(String body, Instant time) {
        return new PostWithBody(body, time);
    }

    @Override
    public String describe() {
        return body + " (Just now)";
    }
}
