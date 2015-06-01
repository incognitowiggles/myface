package com.colinvipurs.application;

import java.util.Set;

/**
 * User to store and gather information on who follows who
 */
public interface Followers {
    void startFollowing(String userFollowing, String userToFollow);
    Set<String> followees(String user);
}
