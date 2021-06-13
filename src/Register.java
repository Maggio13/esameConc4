import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Register {
    public static int PORTA = 8080;
    public static String INDIRIZZO = ":"+PORTA;


    public static void main(String[] args) throws IOException {
        System.out.println("REGISTER: starting");

        ServerSocket s = new ServerSocket(PORTA);

            while (true) {
                Socket socket = s.accept();
                try {
                    new Register_Thread(socket);
                } catch (IOException e) {
                    socket.close();
                }
            }

            //s.close();
           // System.out.println("REGISTER: stopping");

    }
}


class RegisterData{
    String nome;
    String addr;
    RegisterData(String n, String a){
        nome = n;
        addr = a;
    }
    public String toString(){
        return nome+" "+addr;
    }

}