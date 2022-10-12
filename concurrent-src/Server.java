import java.io.*;
import java.net.*;


public class Server implements Runnable {

    private static int port = 0;
    private Socket sock;

    public Server(Socket sock) {
        this.sock = sock;
    }// end Server constructor

    public static void main(String[] args) {
	    port = Integer.parseInt(args[0]);
	    if(port == 0) {
		    System.out.println("Please enter a port number in the command line");
		    System.exit(0);
	    }

	    System.out.println("(Enter ctrl-c to exit the program)");

        // Creation of a server socket
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Socket successfully created at port " + port);

            // calling accept to start listening for client requests
            while(true) {
                Socket sock = serverSocket.accept();
                System.out.println("Listening");

                new Thread(new Server(sock)).start();

            }
        } catch(IOException e) {
            System.out.println("The server did not connect correctly");
            e.printStackTrace();
        }
    }// end main

    // commandRequest will be able to take in the command given by the switch statement above and will return the message from
    // terminal
    private static String commandRequest(String requestedCommand) {
        StringBuilder message = new StringBuilder();

        try {
            // starting the process for whichever command is chosen by the user
            Process process = Runtime.getRuntime().exec(requestedCommand);

            // reading the output from the process ran
            BufferedReader theReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output;

            while((output = theReader.readLine()) != null) message.append(output + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message.toString();
    }// end commandRequest

    @Override
    public void run() {
        String message = "";
        String clientRequest;


        // used to read data sent from client
        InputStreamReader inputReader = null;
        try {
            inputReader = new InputStreamReader(sock.getInputStream());

            // used to read the input stream at a higher, more legible level
            BufferedReader buffRead = new BufferedReader(inputReader);

            // Sending data to the Client
            // Wrapped in PrintWriter to send data in text format
            PrintWriter writer = new PrintWriter(sock.getOutputStream());

            clientRequest = buffRead.readLine();
            switch (clientRequest) {
                case "1":
                    message = commandRequest("date");
                    break;
                case "2":
                    message = commandRequest("uptime");
                    break;
                case "3":
                    message = commandRequest("free -h");
                    break;
                case "4":
                    message = commandRequest("netstat");
                    break;
                case "5":
                    message = commandRequest("w");
                    break;
                case "6":
                    message = commandRequest("ps -aux");
                    break;
                case "7":
                    message = "Good Bye";
                    break;
                default:
                    message = "Incorrect Entry, Please Try Again";

            }

            writer.println(message);
            writer.flush();
            sock.close();
        } catch(IOException IOe) {
            IOe.printStackTrace();
        }
    }// end run
}// end Server Class
