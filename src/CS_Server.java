import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

class CS_Server extends Thread {
    public int error = 0;

    public int      port = 9000;
    public String   addr;

    ServerSocket s;
    private volatile boolean listening = true;

    CS_Server(){
        findFreePort();
    }

    @Override
    public void run() {
        System.out.println("SERVER: starting as "+addr+":"+port);
        listen();
        System.out.println("SERVER: closing");
    }

    public void findFreePort() {
        //questo metodo prova ad aprire una porta (9000). Se la trova occupata, prova ad aprire quella successiva, finche non ne trova una libera.
        boolean found = false;

        while (!found) {
            try {
                System.out.print("SERVER: testing port " + port + ": ");
                s = new ServerSocket(port);
                found=true;
            } catch (IOException e) {
                System.out.println("ERROR (busy)");
                port++;
            }
        }

        try {
            addr = InetAddress.getByName(null).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("SUCCESS!");
    }
    public void listen(){
        //questo metodo gestisce le richieste che arrivano al server
        System.out.println("SERVER: listening");

        while (listening) {
            try {
                Socket socket = s.accept();
                new CS_Server_Thread(socket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        listening = false;
        System.out.println("SERVEadsadsR: closing");

    }
}


class CS_Server_Thread {
    public CS_Server_Thread(Socket socket)  throws IOException{
    }
}




