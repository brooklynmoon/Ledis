package wtf.ledis.io;

import wtf.ledis.common.CommandData;
import wtf.ledis.common.Protocol;
import wtf.ledis.exception.ReaderException;

import java.io.*;

public class Reader {

    public CommandData read(DataInputStream in) {
        try {
            char c = (char) in.readByte();
            var parserResult = switch (c) {
                case '*' -> readArray(in);
                case '$' -> readBulkString(in);
                case '+' -> readSimpleString(in);
                default -> throw new ReaderException("Unknown character '" + c + "'");
            };

            return CommandData.of(
                    parserResult.getCommand().trim(),
                    parserResult.getCommandLength() + 1
            );

        } catch (EOFException e) {
            throw new ReaderException(e.getMessage());
        } catch (IOException e) {
            throw new ReaderException(e.getMessage());
        }
    }

    public CommandData readArray(DataInputStream in) throws IOException {
        // пришла комманда '*2' – двойку читаем как обычную строку
        CommandData arrayLength = readSimpleString(in);
        int length = arrayLength.convertCommand(Integer::valueOf);
        StringBuilder commandBuilder = new StringBuilder();
        Long totalLength = arrayLength.getCommandLength();

        for (int i = 0; i < length; i++) {
            CommandData parsedCommand = read(in);
            commandBuilder.append(parsedCommand.getCommand()).append(" ");
            totalLength += parsedCommand.getCommandLength();
        }

        return CommandData.of(commandBuilder.toString().trim(), totalLength);
    }

    public CommandData readBulkString(DataInputStream in) throws IOException {
        // пришла комманда '$4'
        CommandData parsedResult = readSimpleString(in);
        int stringLength = parsedResult.convertCommand(Integer::valueOf);
        StringBuilder stringBuilder = new StringBuilder();
        Long commandLength = parsedResult.getCommandLength();

        for (int i = 0; i < stringLength; i++) {
            stringBuilder.append((char) in.readByte());
            commandLength += 1L;
        }

        in.skip(2L); // skip '/r/n'
        commandLength += 2L;

        return CommandData.of(stringBuilder.toString(), commandLength);
    }

    public CommandData readSimpleString(DataInputStream in) throws IOException {
        // читаем строку и записываем в буфер пока не встретим '\r'
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        long commandLength = 1L;

        int b;
        while ((b = in.readByte()) != '\r') {
            buffer.write(b);
            commandLength += 1L;
        }

        // пропускаем '\n'
        in.readByte();
        commandLength += 1L;

        return CommandData.of(buffer.toString().trim(), commandLength);
    }

    public String readFully(DataInputStream in) throws IOException {
        char c = (char) in.readByte();
        if (c != Protocol.BULK_STRING.getSymbol().charAt(0)) {
            throw new ReaderException("Unexpected character '" + c + "'. Expected '" + Protocol.BULK_STRING.getSymbol() + "'");
        }

        int stringLength = readSimpleString(in).convertCommand(Integer::valueOf);
        byte[] buffer = new byte[stringLength];

        // читаем в буфер всю строку и возвращаем его в виде строки
        in.readFully(buffer);
        return new String(buffer);
    }
}
