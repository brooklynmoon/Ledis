package wtf.ledis.common;

import org.apache.commons.lang3.tuple.Pair;
import wtf.ledis.command.CommandType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class CommandData {
    private final String command;
    private final Long commandLength;
    private List<String> parts;

    public CommandData(String command, Long commandLength) {
        this.command = command;
        this.commandLength = commandLength;
    }

    public static CommandData of(String command, Long commandLength) {
        return new CommandData(command, commandLength);
    }

    public String getCommand() {
        return command;
    }

    public Long getCommandLength() {
        return commandLength;
    }

    public List<String> getParts() {
        if(parts == null)
            parts = Arrays.stream(command.split(" ")).toList();

        return parts;
    }

    public String getPart(int index) {
        return parts.get(index);
    }

    public String getAllParts(int from) {
        return String.join(" ", getParts().subList(from, getParts().size()));
    }

    public <T> T getPart(int index, Function<String, T> converter) {
        try {
            converter.apply(getPart(index));
        } catch (Exception e) {
            throw new IllegalArgumentException("Converting error: " + e.getMessage());
        }
    }

    public Pair<String, Integer> getPartByName(String name) {
        return getPartByName(name, String::valueOf);
    }

    public <T> Pair<T, Integer> getPartByName(String name, Function<String, T> converter) {
        for (int i = 1; i < parts.size(); i++) {
            var part = parts.get(i);
            if (part.equalsIgnoreCase(name)) {
                return Pair.of(converter.apply(part), i);
            }
        }

        return Pair.of(null, -1);
    }

    public Integer partsSize() {
        return parts.size();
    }

    public CommandType extractCommandType() {
        return CommandType.valueOf(getPart(0));
    }

    public <T> T convertCommand(Function<String, T> converter) {
        return converter.apply(command);
    }
}
