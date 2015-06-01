package acceptancetest

import com.colinvipurs.application.CommandLineApplication
import com.colinvipurs.application.InMemoryDataStore
import com.colinvipurs.application.InMemoryFollowers
import groovy.json.internal.Charsets
import spock.lang.Specification

import java.time.Instant

import static CommandLineApplicationDsl.commandLineApplication
import static SubmittableCommandsDsl.timelineFor
import static acceptancetest.SubmittableCommandsDsl.emptyLine
import static acceptancetest.SubmittableCommandsDsl.exit
import static acceptancetest.SubmittableCommandsDsl.follow
import static acceptancetest.SubmittableCommandsDsl.wallFor
import static acceptancetest.SubmittableCommandsDsl.writePost

public class CommandLineAppSpec extends Specification {
    def application

    def setup() {
        application = commandLineApplication()
    }

    def "accepts commands until exiting"() {
        when:
            application.receivesCommands(timelineFor("Alice"), timelineFor("Bob"), exit(), timelineFor("Alice"))
        then:
            application.receivedOutput("Empty", "Empty")
    }

    def "ignores empty lines"() {
        when:
            application.receivesCommands(emptyLine())
        then:
            application.receivedOutput()
    }

    def "a user can post to their own timeline"() {
        when:
            application.receivesCommands(writePost("I am alive!").to("Alice"), timelineFor("Alice"))
        then:
            application.receivedOutput("I am alive! (Just now)")
    }

    def "multiple timeline posts are returned in descending time order"() {
        when:
            application.receivesCommands(
                    writePost("First post").to("Alice"),
                    writePost("Second post").to("Alice"),
                    timelineFor("Alice"))
        then:
            application.receivedOutput("Second post (Just now)", "First post (Just now)")
    }


    def "a user can subscribe to another users timeline"() {
        when:
            application.receivesCommands(writePost("I love the weather today").to("Alice"))
            waitForClockTick()
            application.receivesCommands(
                writePost("I'm in New York today! Anyone wants to have a coffee?").to("Charlie"),
                follow("Alice").by("Charlie"),
                wallFor("Charlie"))
        then:
            application.receivedOutput(
                "Charlie - I'm in New York today! Anyone wants to have a coffee? (Just now)",
                "Alice - I love the weather today (Just now)"
            )
    }

    // because we're testing from the outside in we need to wait for the smallest amount of host OS clock
    // tick to happen- we do that by waiting until 2 Instant.now() calls do not equal each other
    // in this way we are guaranteed that posts sent after this will appear earlier in a time line
    private waitForClockTick() {
        def start = Instant.now()
        while (start.compareTo(Instant.now()) != 0)  {
            // do nothing
        }
    }
}

class SubmittableCommandsDsl {
    static CommandDsl timelineFor(String name) { return new CommandDsl(input: name) }
    static CommandDsl exit() { return new CommandDsl(input: 'exit') }
    static CommandDsl emptyLine() { return new CommandDsl() }
    static UserCommandDsl writePost(String postText) { return new UserCommandDsl(command: "-> $postText") }
    static FollowCommandDsl follow(String userToFollow) { return new FollowCommandDsl(targetUser: userToFollow) }
    static CommandDsl wallFor(String name) { return new CommandDsl(input: "$name wall") }
}

class UserCommandDsl {
    def String command
    CommandDsl to(String user) { return new CommandDsl(input: "$user $command")}
}

class CommandDsl {
    def String input = ""
}

class FollowCommandDsl {
    def String targetUser
    def by(String userFollowing) { return new CommandDsl(input: "$userFollowing follows $targetUser")}
}

class CommandLineApplicationDsl {
    ByteArrayOutputStream output
    private InMemoryDataStore store = new InMemoryDataStore()
    private InMemoryFollowers followers = new InMemoryFollowers()

    def receivesCommands(CommandDsl ... commands) {
        ByteArrayInputStream commandInput =
                new ByteArrayInputStream(commands*.input.join(System.lineSeparator()).getBytes(Charsets.UTF_8))
        output = new ByteArrayOutputStream()
        new CommandLineApplication(commandInput, output, store, followers).runEventLoop()
    }

    void receivedOutput(String ... lines) {
        assert output.toString()
                .split(System.lineSeparator())
                .collect { it.replaceAll("> ", "") }
                .findAll { !it.equals("")} == lines
    }

    static CommandLineApplicationDsl commandLineApplication() {
        return new CommandLineApplicationDsl()
    }
}