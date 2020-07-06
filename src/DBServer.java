import exception.SQLException;
import schema.DBModel;
import sqlparser.*;
import statement.Statement;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

/**
 * This database stores data in csv files to be human-readable (no serialisation) in exchange for speed.
 * However, running test scripts is still relatively fast (average of ~140ms).
 * This server accepts new client on a new thread to be able to handle both multiple clients
 * and unexpected client disconnections without crashing the main server.
 * Each SQL query must end with a newline character \n, and the server can only handle single queries per line.
 * New table class is loaded from file for every query.
 */

public class DBServer
{
    final static char EOT = 4;

    public static void main(String[] args)
    {
        start(8888);
    }

    public static void start(int portNumber)
    {
        try(ServerSocket ss = new ServerSocket(portNumber)) {
            System.out.println("Server is listening on port " + portNumber);
            while(true) new DBClientHandler(ss.accept()).start();
        } catch(IOException ioe) {
            System.out.println("Server exception: " + ioe.getMessage());
        }
    }

    // DBClientHandler thread class to handle client commands without crashing server if client disconnects unexpectedly
    private static class DBClientHandler extends Thread {
        private final Socket clientSocket;
        private BufferedReader in;
        private BufferedWriter out;
        private final DBModel db;
        private final SQLParser parser;

        public DBClientHandler(Socket socket)
        {
            this.clientSocket = socket;
            db = new DBModel();
            parser = new SQLParser();
        }

        public void run() {
            try {
                System.out.println("New client connected");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                while(true) processNextCommand(in, out);
            } catch(IOException ioe) {
                System.out.println("Server exception: " + ioe.getMessage());
                // tries to close the client connection if unexpected disconnection, otherwise server error
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
        {
            String userInput = in.readLine();
            try {
                Statement query = parser.parse(userInput);
                query.execute(db);
                out.write(query.getResult());
            } catch(SQLException e) {
                out.write(e.getMessage());
            } catch(InvocationTargetException | NoSuchMethodException | InstantiationException
                    | IllegalAccessException e) {
                e.printStackTrace();
            }
            out.write("\n" + EOT + "\n");
            out.flush();
        }
    }
}
