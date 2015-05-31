package acceptancetest

import com.colinvipurs.application.CommandLineApplication
import groovy.json.internal.Charsets
import spock.lang.Specification

import static CommandLineApplicationDsl.commandLineApplication
import static SubmittableCommandsDsl.timelineFor
import static acceptancetest.SubmittableCommandsDsl.exit

class CommandLineAppSpec extends Specification {
    def "accepts commands until exiting"() {
        given:
            def application = commandLineApplication()
        when:
            application.receivesCommands(timelineFor("Alice"), timelineFor("Bob"), exit())
        then:
            application.receivedOutput("Empty", "Empty")
    }
}

class SubmittableCommandsDsl {
    static CommandDsl timelineFor(String name) { return new CommandDsl(input: name) }
    static CommandDsl exit() { return new CommandDsl(input: 'exit') }
}

class CommandDsl {
    def String input
}

class CommandLineApplicationDsl {
    ByteArrayOutputStream output;

    def receivesCommands(CommandDsl ... commands) {
        ByteArrayInputStream commandInput =
                new ByteArrayInputStream(commands*.input.join(System.lineSeparator()).getBytes(Charsets.UTF_8))
        output = new ByteArrayOutputStream()
        new CommandLineApplication(commandInput, output)
    }

    void receivedOutput(String ... lines) {
        assert output.toString().split(System.lineSeparator()) == lines
    }

    static CommandLineApplicationDsl commandLineApplication() {
        return new CommandLineApplicationDsl()
    }
}