import java.io.*;
import java.net.*;

    class client_2 {
    public static void main (String arg[]) throws Exception {
    String messaggio = "";
    String parola;
    String carattere = "";
    int stato = 0;
    Socket socket = null;

//Inizializzo la socket
    try{
        socket = new Socket ("localhost", 1025);
    }
    catch (java.net.ConnectException ex){
        System.out.println("Impossibile stabilire una connessione. Server Down?");
        System.exit(0);
    }
//Inizializzo gli stream di input da tastiera e di output e input da rete
    BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    System.out.println("Inserisci la parola che il tuo avversario deve indovinare");
    String parolaChiave = tastiera.readLine();

//invio la parola da indovinare al server per la memorizzazione
    outputStream.writeBytes(parolaChiave + '\n') ;

    while (stato == 0){
        System.out.println("Inserisci una lettera per indovinare la frase nascosta");
        carattere = tastiera.readLine();

//invio la lettera al server
        try{
            outputStream.writeBytes(carattere + '\n');
        }
        catch (java.net.SocketException e){
            System.out.println("Connessione con il server terminata");
            System.exit(0);
        }

//Leggo la risposta del server
        stato = input.read();
        messaggio = input.readLine();
        parola = input.readLine();

//Stampo a video
        System.out.println(messaggio);
        System.out.println(parola);
        System.out.println(); //Spazio di comodità per la formattazione
    }
    System.out.println("Gioco terminato, Hai vinto");
    socket.close();
    }
}
