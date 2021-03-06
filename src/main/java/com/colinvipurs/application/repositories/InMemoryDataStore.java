package com.colinvipurs.application.repositories;

import com.colinvipurs.application.domain.NewPost;
import com.colinvipurs.application.domain.Post;
import com.colinvipurs.application.domain.Timeline;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to hold timeline information for all users
 */
public class InMemoryDataStore implements TimeLines {
    private Map<String, List<NewPost>> posts = new HashMap<>();

    @Override
    public void push(NewPost newPost) {
        posts.computeIfAbsent(newPost.getUser(), (user) -> new LinkedList<>()).add(0, newPost);
    }

    @Override
    public Timeline postsFor(String user) {
        List<Post> timelinePosts = rawPostsFor(user).stream()
                .map(post -> postView(post))
                .collect(Collectors.toList());
        return Timeline.withPosts(timelinePosts);
    }

    @Override
    public Timeline wallFor(String user, Set<String> followingUsers) {
        Set<String> allUsers = new HashSet<>(followingUsers);
        allUsers.add(user);

        List<Post> wallPosts = allUsers
                .stream()
                .map(postUser -> rawPostsFor(postUser))
                .flatMap(Collection::stream)
                .sorted((p1, p2) -> p2.getTime().compareTo(p1.getTime()))
                .map(post -> postView(post))
                .collect(Collectors.toList());

        return Timeline.withPosts(wallPosts);
    }

    private Post postView(NewPost post) {
        return Post.of(post.getUser(), post.getPost(), post.getTime());
    }

    private List<NewPost> rawPostsFor(String user) {
        return posts.getOrDefault(user, Collections.EMPTY_LIST);
    }
}
