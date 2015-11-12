import java.net.*;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.nio.file.Files;


public class ClipServer extends Thread
{
    private ServerSocket serverSocket;
    
    public ClipServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(0);
    }

    public void run()
    {
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

                Socket server = serverSocket.accept();
                System.out.println("Connected to " + server.getRemoteSocketAddress());

                int size = 5000;
                char[] bytes = new char[size];
                int n;
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(server.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String aux = "";

                while ((n = bufReader.read(bytes, 0, size)) != -1) {
                    sb.append(new String(bytes, 0, n));
                }
                
                bufReader.close();
                System.out.println(sb.toString());
                copyToClipboard(sb.toString());
                server.close();
            } catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void copyToClipboard(String contents) {
        try {
            StringSelection stringSelection = new StringSelection(contents);
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to write to clipboard");
        }
    }

    public static void main(String [] args)
    {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run()
            {
                System.out.println("Killed");
            }
        });

        if (args.length != 1) {
            System.out.println("Usage: java <program> <port>");
            return;
        }

        String arg0 = args[0];
        if (!isNumeric(arg0)) {
            System.out.println("port needs to be an integer");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try
        {
            Thread t = new ClipServer(port);
            t.start();
            t.join();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}
