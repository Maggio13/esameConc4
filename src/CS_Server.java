import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

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
    }
}


class CS_Server_Thread extends Thread{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public CS_Server_Thread(Socket s)  throws IOException {
        socket = s;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();

    }
    public void run() {
        System.out.println("SERVER: accept connection");
        try {
            String incomingText = "";
            while (incomingText != null && !incomingText.equals("disconnect")) {
                incomingText = in.readLine();
                parseComando(incomingText);
            }

        } catch (IOException e) {
            System.out.println("SERVER: client error");
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Socket not closed");
        }
        System.out.println("SERVER: closing connection");
    }




    public void parseComando(String riga) {
        String[] linea;

        if (riga != null && riga.length() > 2) {
            linea = riga.split(" ");

            if (linea.length >= 2) {
                System.out.println("> " + linea[0] + ", " + linea[1]);

                switch (linea[0]){
                    case "copiaDato":
                        copiaDato(linea[1]);
                        break;
                }
            }
        }
    }


    public void copiaDato(String name){
        String dataContent = "";
        dataContent = CS.dati.get(0).data;
        out.println(dataContent);

    }
}




