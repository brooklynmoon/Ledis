package wtf.ledis.command;

import wtf.ledis.command.context.CommandContext;

import java.io.IOException;

public interface Command<T extends CommandContext> {
    String CRLF = "\r\n";

    byte[] execute(T context) throws IOException;
}
