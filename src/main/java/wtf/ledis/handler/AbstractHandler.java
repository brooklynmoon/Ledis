package wtf.ledis.handler;

import wtf.ledis.command.CommandType;
import wtf.ledis.command.context.CommandContext;
import wtf.ledis.command.processor.CommandProcessor;
import wtf.ledis.common.CommandData;
import wtf.ledis.common.ObjectHolder;
import wtf.ledis.exception.ProcessException;
import wtf.ledis.io.Reader;
import wtf.ledis.io.Writer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractHandler implements Handler {
    protected final CommandProcessor processor;

    protected final Reader reader;

    protected final Writer writer;

    protected final Map<CommandType, Function<CommandData, CommandContext>> contexts = new HashMap<>();

    public AbstractHandler() {
        this.processor = new CommandProcessor();
        this.reader = ObjectHolder.getInstance().get(Reader.class);
        this.writer = ObjectHolder.getInstance().get(Writer.class);
    }

    @Override
    public void handle(DataInputStream in, OutputStream out) throws IOException {
        while (Boolean.TRUE) {
            CommandData commandData = reader.read(in);
            var commandType = commandData.extractCommandType();
            var context = contexts.get(commandType).apply(commandData);

            Supplier<byte[]> command = () -> {
                try {
                    return processor.process(context, commandType);
                } catch (IOException e) {
                    throw new ProcessException(e);
                }
            };

            processResponse(command.get(), commandData, in, out);
        }
    }

    protected abstract void processResponse(byte[] bytes, CommandData commandData, DataInputStream in, OutputStream out) throws IOException;
}
