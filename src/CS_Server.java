import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class CS_Server extends Thread {
    public int error = 0;

    public int      port = 9000;
    public String   addr;

    ServerSocket s;

    CS_Server(){
        findFreePort();
    }

    public void findFreePort() {
        //questo metodo prova ad aprire una porta (9000). Se la trova occupata, prova ad aprire quella successiva, finche non ne trova una libera.
        System.out.println("SERVER: starting");

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
        System.out.println("SUCCESS!");
    }

    @Override
    public void run() {
        //questo metodo gestisce le richieste che arrivano al server
        System.out.println("SERVER: listening");

        while (true) {
            try {
                Socket socket = s.accept();
                new CS_Server_Thread(socket);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

}


class CS_Server_Thread {
    public CS_Server_Thread(Socket socket)  throws IOException{
    }
}




