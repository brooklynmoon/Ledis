package wtf.ledis.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.DataInputStream;

public interface Handler {
    void handle(DataInputStream in, OutputStream out) throws IOException;
}
