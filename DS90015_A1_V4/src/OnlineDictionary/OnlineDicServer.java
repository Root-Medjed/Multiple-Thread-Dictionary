package OnlineDictionary;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import javax.net.ServerSocketFactory;

public class OnlineDicServer { //TCP

    private static int port = 6666;
    private static int counter = 0;
    protected ExecutorService threadPool = null;
    //public ServerSocket welcomeSocket;
    //private Object DictionaryServer;

    public static void main(String[] args) {
        OnlineDicServer dictServer = new OnlineDicServer(); //create an object then
        dictServer.awakeServer(); //call server method below
    }

    public void awakeServer() {

        //create server socket for listening
        //ServerSocketFactory factory = ServerSocketFactory.getDefault();

        ServerSocket welcomeSocket = null;
        Socket connectionSocket = null;

        //call JSON class main method

        DictJSON.buildJsonDictionary();

        threadPool = Executors.newFixedThreadPool(10);

        try {//(ServerSocket welcomeSocket = factory.createServerSocket(port)){
            //Create a server socket listening on port 8888

            welcomeSocket = new ServerSocket(port);

            //Listen for incoming connections for ever
            while (true) {

                System.out.println("Server listening on port " + port + " for a connection");
                //Accept an incoming client connection request
                connectionSocket = welcomeSocket.accept(); //This method will block until a connection request is received

                counter++;

                System.out.println("Client connection number " + counter + " accepted:");
                System.out.println("Remote Port: " + connectionSocket.getPort());
                System.out.println("Remote Hostname: " + connectionSocket.getInetAddress().getHostName());
                System.out.println("Local Port: " + connectionSocket.getLocalPort());

                MultithreadServer thread = new MultithreadServer(connectionSocket, counter, this);
                threadPool.execute(thread);
                //Thread t = new Thread(() -> new MultiThreadServer(client, counter, this));
                //t.start();
            }
        } catch (SocketException ex){
            System.out.println("A server socket error has occurred!");
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("server I/O error! ");
        }		finally {
            if(welcomeSocket != null) {
                try {
                    welcomeSocket.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("A server socket error occurred!");
                }
            }
        }
    }
}


//Run Server on mundroo.cs.mu.oz.au????

//reference: http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html