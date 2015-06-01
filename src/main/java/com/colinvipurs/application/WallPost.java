package com.colinvipurs.application;

/**
 * A post that goes on to a users wall.
 *
 * This differs for a normal Post in that it carries the originating users name first
 */
public class WallPost implements Post {
    private final String name;
    private final Post targetPost;

    private WallPost(String name, Post targetPost) {
        this.name = name;
        this.targetPost = targetPost;
    }

    public static WallPost of(String name, Post targetPost) {
        return new WallPost(name, targetPost);
    }

    @Override
    public String describe() {
        return String.format("%s - %s", name, targetPost.describe());
    }
}
