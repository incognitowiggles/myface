package acceptancetest

import com.colinvipurs.application.CommandLineApplication
import com.colinvipurs.application.InMemoryTimeLines
import groovy.json.internal.Charsets
import spock.lang.Specification

import static CommandLineApplicationDsl.commandLineApplication
import static SubmittableCommandsDsl.timelineFor
import static acceptancetest.SubmittableCommandsDsl.emptyLine
import static acceptancetest.SubmittableCommandsDsl.exit
import static acceptancetest.SubmittableCommandsDsl.writePost

public class CommandLineAppSpec extends Specification {
    def "accepts commands until exiting"() {
        given:
            def application = commandLineApplication()
        when:
            application.receivesCommands(timelineFor("Alice"), timelineFor("Bob"), exit())
        then:
            application.receivedOutput("Empty", "Empty")
    }

    def "ignores empty lines"() {
        given:
            def application = commandLineApplication()
        when:
            application.receivesCommands(emptyLine())
        then:
            application.receivedOutput()
    }

    def "a user can post to their own timeline"() {
        given:
            def application = commandLineApplication()
        when:
            application.receivesCommands(writePost("I am alive!").to("Alice"), timelineFor("Alice"))
        then:
            application.receivedOutput("I am alive! (Just now)")
    }

    def "multiple posts can be pushed to a users timeline"() {
        given:
            def application = commandLineApplication()
        when:
            application.receivesCommands(
                    writePost("First post").to("Alice"),
                    writePost("Second post").to("Alice"),
                    timelineFor("Alice"))
        then:
            application.receivedOutput("First post (Just now)", "Second post (Just now)")
    }
}

class SubmittableCommandsDsl {
    static CommandDsl timelineFor(String name) { return new CommandDsl(input: name) }
    static CommandDsl exit() { return new CommandDsl(input: 'exit') }
    static CommandDsl emptyLine() { return new CommandDsl() }
    static UserCommandDsl writePost(String postText) { return new UserCommandDsl(command: "-> $postText") }
}

class UserCommandDsl {
    def String command
    CommandDsl to(String user) { return new CommandDsl(input: "$user $command")}
}

class CommandDsl {
    def String input = ""
}

class CommandLineApplicationDsl {
    ByteArrayOutputStream output;

    def receivesCommands(CommandDsl ... commands) {
        ByteArrayInputStream commandInput =
                new ByteArrayInputStream(commands*.input.join(System.lineSeparator()).getBytes(Charsets.UTF_8))
        output = new ByteArrayOutputStream()
        new CommandLineApplication(commandInput, output, new InMemoryTimeLines()).runEventLoop()
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