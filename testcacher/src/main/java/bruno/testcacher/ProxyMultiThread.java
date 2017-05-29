package bruno.testcacher;

import java.net.ServerSocket;

public class ProxyMultiThread {

    public static void main(String[] args) {
        try {
            if (args.length != 3)
                throw new IllegalArgumentException("insuficient arguments");
            // and the local port that we listen for connections on
            String host = args[0];
            int remoteport = Integer.parseInt(args[1]);
            int localport = Integer.parseInt(args[2]);
            // Print a start-up message
            System.out.println("Starting proxy for " + host + ":" + remoteport
                    + " on port " + localport);
            ServerSocket server = new ServerSocket(localport);
            while (true) {
                new ThreadProxy(host, remoteport);
            }
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java ProxyMultiThread "
                    + "<host> <remoteport> <localport>");
        }
    }
}
