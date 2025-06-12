package wtf.ledis.command;

import java.util.Arrays;

public enum CommandType {
    PING("ping");

    private final String command;

    CommandType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static CommandType fromCommand(String curCommand) {
        return Arrays.stream(values())
                .filter(
                        it -> it.command.equalsIgnoreCase(curCommand)
                )
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Command not found: " + curCommand)
                );
    }
}
