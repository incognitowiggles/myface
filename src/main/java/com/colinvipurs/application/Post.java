package com.colinvipurs.application;

/**
 * Interface for differnet kind of posts
 */
public interface Post {
    String describe();

    static Post empty() {
        return () -> "Empty";
    }
}
