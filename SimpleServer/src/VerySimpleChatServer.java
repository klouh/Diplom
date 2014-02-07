import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

//Кетти Сьерра Берт Бейтс "Изучаем JAVA"

public class VerySimpleChatServer {
	private String TAG="ClientLog";
	
	ArrayList<PrintWriter> clientOutputStreams;
	
	public class ClientHandler implements Runnable{
		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket){
			
			try	{
				sock=clientSocket;
				InputStreamReader isReader = new InputStreamReader (sock.getInputStream());
				reader=new BufferedReader(isReader);
				System.out.println("Работает функция clientHandler");
			} catch (Exception ex) {ex.printStackTrace();}
			
		}
		
		public void run(){
			String message;
			try {
				while ((message=reader.readLine()) !=null){
					System.out.println("read"+message);
					tellEveryone(message);
					System.out.println("Работает функция run");
				}
			}catch (Exception ex){ex.printStackTrace();}
		}
	}
	
	public void go()
	{
		clientOutputStreams=new ArrayList<PrintWriter>();
		try
		{
			ServerSocket serverSock=new ServerSocket(2000);
			while (true){
				Socket clientSocket=serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				
				Thread t = new Thread (new ClientHandler(clientSocket));
				t.start();
				System.out.println("соединение установлено! Сервер готов принимать ерунду.");
			}
		}catch (Exception ex){ex.printStackTrace();}
		
	}
	
	public void tellEveryone(String message){
		Iterator<PrintWriter> it=clientOutputStreams.iterator();
		while(it.hasNext())
		{
			try{
			PrintWriter writer = (PrintWriter) it.next();
			writer.println(message);
			writer.flush();
			System.out.println("Работает функция tellEveryone");
			}catch (Exception ex){ex.printStackTrace();}
			
		}
	}
	
		
}