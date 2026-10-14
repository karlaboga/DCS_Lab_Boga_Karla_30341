package lab.scd.net.calculator;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class SimpleServer {
    public static void main(String[] args) throws IOException{

        ServerSocket ss=null;
        Socket s=null;

        try{
            String line="";
            ss = new ServerSocket(1900); // create a serversocket object
            System.out.println("Server is waiting for connections...");
            s = ss.accept(); // start waiting for connections at port 1900

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));

            PrintWriter out = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(
                            s.getOutputStream())),true);

            InetSocketAddress remoteadr = (InetSocketAddress)s.getRemoteSocketAddress();
            String remotehost = remoteadr.getHostName();
            int remoteport = remoteadr.getPort();

            System.out.println("Client nou conectat: "+remotehost+":"+remoteport);

            // ------------ Lab1 Ex2 -> Calculator ------------
            while((line = in.readLine()) != null && !line.equals("END")){
                System.out.println("Server recieved a request: " + line);

                // Send the calculation
                String response = calculate(line);
                out.println(response); // Send the calculation to the client
            }
            // ----------------------------------------------------------------

            System.out.println("Aplicatie server gata.");

        }catch(Exception e){e.printStackTrace();}
        finally{
            if(ss != null) ss.close();
            if(s != null) s.close();
        }
    }

    // Metoda ajutatoare adaugata pentru efectuarea calculului
    private static String calculate(String expr) {
        try {
            String[] parts = expr.split(" ");
            if (parts.length != 3) {
                return "Eroare: Format invalid (ex: 10 + 5)";
            }
            double n1 = Double.parseDouble(parts[0]);
            String op = parts[1];
            double n2 = Double.parseDouble(parts[2]);
            double res = 0;

            switch (op) {
                case "+": res = n1 + n2; break;
                case "-": res = n1 - n2; break;
                case "*": res = n1 * n2; break;
                case "/":
                    if (n2 == 0) return "Eroare: Impartire la zero";
                    res = n1 / n2;
                    break;
                default: return "Eroare: Operatie necunoscuta";
            }
            return "Rezultat: " + res;
        } catch (Exception e) {
            return "Eroare la procesare";
        }
    }
}