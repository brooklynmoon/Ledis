package wtf.ledis.command.processor;

import wtf.ledis.command.Command;
import wtf.ledis.command.CommandType;
import wtf.ledis.command.context.CommandContext;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandProcessor {
    private final Map<CommandType, Command<?>> commands;

    public CommandProcessor() {
        this.commands = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends CommandContext> byte[] process(T context, CommandType commandType) throws IOException {
        Command<T> command = (Command<T>) commands.get(commandType);
        if(command != null) {
            return command.execute(context);
        } else {
            throw new IllegalArgumentException("Unknown command: " + commandType);
        }
    }
}
