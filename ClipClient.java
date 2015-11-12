import java.net.*;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ClipClient
{
    public static void main(String [] args)
    {
        if (args.length != 2) {
            System.out.println("Usage: java <program> <port>");
            return;
        }

        String serverName = args[0];
        if (!isNumeric(args[1])) {
            System.out.println("port needs to be an integer");
            return;
        }

        int port = Integer.parseInt(args[1]);
        try {
            String contents = readFromSystemIo();
            Socket client = new Socket(serverName, port);
            System.out.println("Connected to " + client.getRemoteSocketAddress());

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeBytes(contents);
            client.close();
        } catch(Exception e) {
            System.out.println("Failed to send message");
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

    public static String readFromSystemIo() throws Exception {
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));

        StringBuilder sb = new StringBuilder();
        String aux = "";
        int size = 1048;
        int n = 0;
        char[] buf = new char[size];

        while ((n = bufReader.read(buf, 0, size)) != -1)  {
            sb.append(new String(buf, 0, n));
        }

        bufReader.close();
        return sb.toString();
    }
}
