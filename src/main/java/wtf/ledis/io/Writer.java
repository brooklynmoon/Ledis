package wtf.ledis.io;

import wtf.ledis.command.Command;
import wtf.ledis.common.Protocol;

import static wtf.ledis.command.Command.CRLF;

/**
 * Отвечает пользователю.
 * Реализует вывод ответов разных типов.
 */
public class Writer {
    public byte[] bulkString(String value) {
        if (value == null)
            return (Protocol.BULK_STRING.getSymbol() + "-1" + CRLF).getBytes();

        return (Protocol.BULK_STRING.getSymbol() + value.length() + CRLF
                + value + CRLF).getBytes();
    }

    public byte[] simpleString(String value) {
        return (Protocol.SIMPLE_STRING.getSymbol() + value + CRLF).getBytes();
    }

    public byte[] simpleError(String value) {
        return (Protocol.ERROR.getSymbol() + value + CRLF).getBytes();
    }

    public byte[] integer(Integer value) {
        return (Protocol.INTEGER.getSymbol() + value + CRLF).getBytes();
    }
}
