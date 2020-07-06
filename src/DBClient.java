import java.io.*;
import java.net.*;

@SuppressWarnings("ThrowablePrintedToSystemOut")
public class DBClient
{
    final static char EOT = 4;
    private static long time = 0;

    public static void main(String[] args)
    {
        try {
            BufferedReader commandLine = new BufferedReader(new InputStreamReader(System.in));
            Socket socket = new Socket("127.0.0.1", 8888);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) handleNextCommand(commandLine, out, in);
        } catch(IOException ioe) {
            System.out.println(ioe);
        }
    }

    private static void handleNextCommand(BufferedReader commandLine, BufferedWriter out, BufferedReader in)
    {
        try {
            System.out.print("SQL:> ");
            String command = commandLine.readLine();
            out.write(command + "\n");
            out.flush();
            long startTime = System.nanoTime();
            String incoming = in.readLine();
            while( ! incoming.contains("" + EOT + "")) {
                System.out.println(incoming);
                incoming = in.readLine();
            }
            long stopTime = System.nanoTime();
            time += stopTime - startTime;
            System.out.println(time/1000000);
        } catch(IOException ioe) {
            System.out.println(ioe);
        }
    }

}
