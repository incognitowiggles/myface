package com.colinvipurs.application.cli;

import com.colinvipurs.application.cli.CommandLineApplication;
import com.colinvipurs.application.repositories.InMemoryDataStore;
import com.colinvipurs.application.repositories.InMemoryFollowers;

public class Start {
    public static void main(String[] args) throws Exception {
        InMemoryDataStore inMemoryDataStore = new InMemoryDataStore();
        InMemoryFollowers inMemoryFollowers = new InMemoryFollowers();
        new CommandLineApplication(System.in, System.out, inMemoryDataStore, inMemoryFollowers).runEventLoop();
    }
}
