package com.colinvipurs.application;

/**
 * Created by colin on 31/05/15.
 */
public interface TimeLines {
    void push(NewPost newPost);

    Timeline postsFor(String user);
}
