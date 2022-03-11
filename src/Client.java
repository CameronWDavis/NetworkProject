/*
* @author Cameron Davis 
* @version october/14/2021
*/

import java.io.*; 
import java.net.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner; 

public class Client 
{ 
	final static int ServerPort = 1234; 
	public static void main(String args[]) throws UnknownHostException, IOException 
	{
		Thread Client_Thread = new Thread(new Runnable() 
		{ int Total_TAT = 0;
			@Override
			public void run() {
				
				Scanner scn = new Scanner(System.in); 
				
				System.out.print("Enter Number Of Clients : ");
				int clients = scn.nextInt();

				for (int i=0;i<clients;i++){	
				try {

	
					System.out.println("\nClient No : "+(i+1)+ " In Sequence\n");
					InetAddress ip = InetAddress.getByName("localhost"); 
					Socket s = new Socket(ip, ServerPort);
					DataInputStream dis = new DataInputStream(s.getInputStream()); 
					DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 		

				Scanner scanner = new Scanner(System.in); 	
				String Menu = "1 -	Date and Time\n2 -	Uptime\n3 -	Memory Use\n4 -	Netstat\n5 -	Current Users\n6 -	Running Processes\nEnter Your Choice for Client "+(i+1)+" : ";	
				System.out.print(Menu);
				String choice = scanner.nextLine();
				String command = "";
				Instant start = Instant.now();
				if (choice.equals("1"))
				{
					command ="Date";
					dos.writeUTF(command);
					String responce = dis.readUTF(); 
					System.out.println(responce); 
				}
				else if (choice.equals("2"))
				{
					command ="Uptime";
					dos.writeUTF(command);
					String responce = dis.readUTF(); 
					System.out.println(responce); 
				}
				else if (choice.equals("3"))
				{
					command ="Memory";
					dos.writeUTF(command);
					String responce = dis.readUTF(); 
					System.out.println(responce); 
				}
				else if (choice.equals("4"))
				{
					command ="Netstat";
					dos.writeUTF(command);
					String responce = dis.readUTF(); 
					System.out.println(responce); 
				}
				else if (choice.equals("5"))
				{
					command ="User";
					dos.writeUTF(command);
					String responce = dis.readUTF(); 
					System.out.println(responce); 
				}
				else if (choice.equals("6"))
				{
					command ="Process";
					dos.writeUTF(command);
					String responce = dis.readUTF(); 
					System.out.println(responce); 
				}
				else 
				{	command ="Invalid";
				dos.writeUTF(command);
					System.out.print("\nInvalid Command\n");
				}
				
		        Instant end = Instant.now();
		        Duration interval = Duration.between(start, end);		 
		        System.out.println("Turnaround Time Of Current Command : " + interval.getSeconds()+" seconds");
		        Total_TAT = Total_TAT +	(int) (interval.getSeconds());

			}
				catch (IOException e) {
					System.out.print(e);					
				}
				
			}
				System.out.println("Total Turnaround Time : " + Total_TAT +" seconds");
				System.out.println("Average Turnaround Time : " + (Total_TAT/clients) +" seconds");
			} 
		});
		Client_Thread.start();
		
	
	}
} 
