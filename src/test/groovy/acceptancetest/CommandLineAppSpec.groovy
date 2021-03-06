package acceptancetest

import com.colinvipurs.application.cli.CommandLineApplication
import com.colinvipurs.application.repositories.InMemoryDataStore
import com.colinvipurs.application.repositories.InMemoryFollowers
import groovy.json.internal.Charsets
import spock.lang.Specification

import java.time.Instant

import static CommandLineApplicationDsl.commandLineApplication
import static SubmittableCommandsDsl.timelineFor
import static acceptancetest.SubmittableCommandsDsl.emptyLine
import static acceptancetest.SubmittableCommandsDsl.follow
import static acceptancetest.SubmittableCommandsDsl.wallFor
import static acceptancetest.SubmittableCommandsDsl.writePost

public class CommandLineAppSpec extends Specification {
    private static final String justNow = "(Just now)"

    def application

    def setup() {
        application = commandLineApplication()
    }

    def "ignores empty lines"() {
        when:
            application.receivesCommands(emptyLine())
        then:
            application.receivedOutput()
    }

    def "a user can post to their own timeline"() {
        when:
            application.receivesCommands(writePost("I am alive!").by("Alice"), timelineFor("Alice"))
        then:
            application.receivedOutput("I am alive! $justNow")
    }

    def "multiple timeline posts are returned in descending time order"() {
        when:
            application.receivesCommands(
                    writePost("First post").by("Alice"),
                    writePost("Second post").by("Alice"),
                    timelineFor("Alice"))
        then:
            application.receivedOutput("Second post $justNow", "First post $justNow")
    }


    def "a user can subscribe to another users timeline"() {
        when:
            application.receivesCommands(writePost("I love the weather today").by("Alice"))
            waitForClockTick()
            application.receivesCommands(
                writePost("I'm in New York today! Anyone wants to have a coffee?").by("Charlie"),
                follow("Alice").by("Charlie"),
                wallFor("Charlie"))
        then:
            application.receivedOutput(
                "Charlie - I'm in New York today! Anyone wants to have a coffee? $justNow",
                "Alice - I love the weather today $justNow"
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
    CommandDsl by(String user) { return new CommandDsl(input: "$user $command")}
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