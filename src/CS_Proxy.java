import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CS_Proxy {

    ServerSocket serverSocket;
    //Socket s;
    Socket c;
    private BufferedReader in;
    private PrintWriter out;

    CS_Proxy(InetAddress addr, int portNum) throws IOException{

            c = new Socket(addr, portNum);
            in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(c.getOutputStream())), true);

    }

    public String trovaDato(String nome) {
        out.println("trovaDato " + nome);  //il registro quando legge questa stringa deve trovare il cs con il dato punto 3
        String risultato = "";
        try {
            risultato = in.readLine();
        } catch (IOException e) {
            System.out.println("errore di IO");
        }
        return risultato;
    }

    public void notificaServer(String nome){
        out.println("memorizzaDato " + nome);
    }

    public void connetti(String addr) {
        out.println("connetti " + addr);
    }

    public void disconnetti() {
        out.println("disconnetti " + 0);
        try {
            c.close();
        } catch (IOException e) { }
    }
}
