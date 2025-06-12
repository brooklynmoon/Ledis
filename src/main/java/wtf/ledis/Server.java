package wtf.ledis;

import wtf.ledis.exception.ServerException;
import wtf.ledis.handler.CommandHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try(var serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(Boolean.TRUE);
            System.out.println("Ledis server started on port " + port);

            while(true) {
                var clientSocket = serverSocket.accept();
                new Thread(new CommandHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            throw new ServerException(e.getMessage());
        }
    }
}
