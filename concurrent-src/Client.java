import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Client extends Thread {

    // User input
    private static int portNumber;
    private static String IPAddress;
    public static String command = "";	// Command chosen by user

    // Time tracking
    static int allRequestTimes = 0;
    static int recordedTimeTotal = 0;
    static int clientValue;

    static int amountOfThreads;
    static int threadNumber = 1;
    // Constructor
    public Client(int value) {
        this.clientValue = value;
    }

    public static void main(String[] args) {

	    IPAddress = args[0];
	    portNumber = Integer.parseInt(args[1]);

        Scanner userInput = new Scanner(System.in);
        Queue<Thread> threadQueue = new LinkedList<Thread>();

        // menu for the user
        menu(userInput);

	    if(command.equals("7")) {
		    System.out.println("Good Bye");
		    System.exit(0);
	    }


        // CREATING THREADS
        System.out.print("Enter in the amount of times you'd like to run this command: ");
        amountOfThreads = userInput.nextInt();
	    testingAmountOfThreads(userInput);


        // Threads being created
        for(int i = 0; i < amountOfThreads; i++) {
            Client newClient = new Client(i);
            threadQueue.add(newClient);
        }

        for(int j = 0; j < amountOfThreads; j++) {
            threadQueue.poll().start();
        }

	    userInput.close();
	    return;
    }// end main

    public static void testingAmountOfThreads(Scanner userInput) {
	    int number = amountOfThreads;
	    // Testing to see if the amount of threads is over 25
	    if(number > 100) {
	    	System.out.print("Invalid Entry, Please Try Again: ");
		    amountOfThreads = userInput.nextInt();
    		testingAmountOfThreads(userInput);
	    }
	    while(number > 0) {
		    number = number - 5;
	    }
	    // Testing to see if the amount of threads is a multiple of 5 or if it was a single thread
	    if(number != 0 && amountOfThreads != 1) {
		    System.out.print("Invalid Entry, Please Try Again: ");
		    amountOfThreads = userInput.nextInt();
		    testingAmountOfThreads(userInput);
	    }
    }// end testingAmountOfThreads

    public void run() {
        long time = 0;
        StringBuilder builtString = new StringBuilder();

        try(Socket sock = new Socket(IPAddress, portNumber)) {
            long start = System.currentTimeMillis();

            PrintWriter send = new PrintWriter(sock.getOutputStream());
            send.println(command);
            send.flush();

            InputStream input = sock.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String serverMessage;

            while((serverMessage = reader.readLine()) != null) {
                builtString.append(serverMessage + "\n");
            }

            System.out.println(builtString);

	        long stop = System.currentTimeMillis();
	        time = stop - start;
	        allRequestTimes += time;

	        ++recordedTimeTotal;

	        if(amountOfThreads <= recordedTimeTotal) {
		        printTime();
	        }
	        threadNumber++;
	        sock.close();
        } catch(UnknownHostException UHE) {
            System.out.println("Server cannot be found at this time");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// end run()

    private static void  menu(Scanner userInput) {
	    System.out.println("Choose from menu options");
        System.out.println("1. Date and time");
        System.out.println("2. Uptime");
        System.out.println("3. Memory use");
        System.out.println("4. Netstat");
        System.out.println("5. Current Users");
        System.out.println("6. Running Processes");
        System.out.println("7. Exit");
        System.out.print("> ");
        command = userInput.nextLine();
    }// end menu



    // this function will print the times
    private static void printTime() {
	    long averageTime = allRequestTimes / amountOfThreads;
	    System.out.println("Clients: " + amountOfThreads + "\n");
	    System.out.println("Total Runtime: " + allRequestTimes + " milliseconds");
	    System.out.println("Average Runtime: " + averageTime + " milliseconds");
    }// end printTime
}// End Client Class
