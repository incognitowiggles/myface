package com.colinvipurs.application;

public class Start {
    public static void main(String[] args) throws Exception {
        InMemoryDataStore inMemoryDataStore = new InMemoryDataStore();
        InMemoryFollowers inMemoryFollowers = new InMemoryFollowers();
        new CommandLineApplication(System.in, System.out, inMemoryDataStore, inMemoryFollowers).runEventLoop();
    }
}
