package wtf.ledis.handler;

import wtf.ledis.command.Command;
import wtf.ledis.common.CommandData;
import wtf.ledis.exception.ProcessException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class CommandHandler extends AbstractHandler implements Runnable {


    private final Socket clientSocket;

    public CommandHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    protected void processResponse(byte[] response, CommandData commandData, DataInputStream in, OutputStream out) throws IOException {
        out.write(response);
        out.flush();
    }

    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            OutputStream output = clientSocket.getOutputStream();
            handle(input, output);
        } catch (IOException e) {
            System.out.println("IOException during reading input stream");
        } catch (ProcessException e) {
            System.out.println("ProcessException during reading input stream");
        } finally {
            try {
                clientSocket.close();
                System.out.println("Socket closed");
            } catch (IOException e) {
                System.out.println("IOException during closing socket");
            }
        }
    }
}
