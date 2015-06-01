package com.colinvipurs.application;

import java.util.Set;

/**
 * Interface for classes wishing to implement data storage for
 * Timelines.
 */
public interface TimeLines {
    void push(NewPost newPost);
    Timeline postsFor(String user);
    Timeline wallFor(String user, Set<String> followingUsers);

}
