import java.io.*;
import java.net.*;

class Server {
    public static void main (String arg[]) throws Exception {

    //variabili

    String parolaChiave1; //la parola chiave che il client1 deve indovinare
    String parolaChiave2; //la parola chiave che il client2 deve indovinare
    int lunghezzaParola1; //dimensione della prima parola chiave
    int lunghezzaParola2; //dimensione della seconda parola chiave
    char[] parolaChiaveChar1; //Array che contiene, per ogni cella, il carattere della parola chiave 1
    char[] parolaChiaveChar2; //Array che contiene, per ogni cella, il carattere della parola chiave 2
    char carattere1; //Singolo carattere inviato dal client1
    char carattere2; //Singolo carattere inviato dal client2
    char[] risposta1Char; //Array che contiene, nelle relative celle, le lettere indovinate
    char[] risposta2Char; //Array che contiene, nelle relative celle, le lettere indovinate
    int lettereIndovinate1 = 0; //Contatore
    int lettereIndovinate2 = 0; //Contatore
    String risposta1;
    String risposta2;
    boolean vittoria = false;
    int i;


    try (//Inizializzo la socket
    ServerSocket serverSocket = new ServerSocket (2804)) {
        Socket socket1 = serverSocket.accept();
        Socket socket2 = serverSocket.accept();

        //Flussi di lettura da rete
        BufferedReader BR1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        BufferedReader BR2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        //Flussi di scrittura
        DataOutputStream outputToClient1 = new DataOutputStream (socket1.getOutputStream());
        DataOutputStream outputToClient2 = new DataOutputStream (socket2.getOutputStream());

        //Leggo le parole chiavi(invertite), ottengo gli array di char e salvo la grandezza della parole
        parolaChiave1 = BR2.readLine();
        parolaChiave2 = BR1.readLine();

        parolaChiaveChar1 = parolaChiave1.toCharArray();
        parolaChiaveChar2 = parolaChiave2.toCharArray();

        lunghezzaParola1 = parolaChiave1.length();
        lunghezzaParola2 = parolaChiave2.length();

        //inizializzo i vettori di char delle risposte
        risposta1Char = new char[lunghezzaParola1];
        risposta2Char = new char[lunghezzaParola2];

        for (i=0; i<lunghezzaParola1;i++)
        risposta1Char[i]='_';
        for (i=0; i<lunghezzaParola2;i++)
        risposta2Char[i]='_';

        //Algoritmo
        while (vittoria == false){
            //Leggo i caratteri dai client
            carattere1 = BR1.readLine().charAt(0);
            carattere2 = BR2.readLine().charAt(0);

            //Controllo le corrispondenze del client1 sulla parola segreta del client2
            for (i=0; i<lunghezzaParola1;i++){
                if (carattere1 == parolaChiaveChar1[i]){
                    risposta1Char[i] = carattere1;
                    lettereIndovinate1++;
                }
            }
//Controllo le corrispondenze del client2 sulla parola segreta del client1
            for (i=0; i<lunghezzaParola2;i++){
                if (carattere2 == parolaChiaveChar2[i]){
                    risposta2Char[i] = carattere2;
                    lettereIndovinate2++;
                }
            }

//Compongo la parola con gli eventuali caratteri indovinati
            risposta1 = "";
            risposta2 = "";
            for (i=0; i<lunghezzaParola1; i++){
                risposta1 = risposta1 + risposta1Char[i];
            }
            for (i=0; i<lunghezzaParola2; i++){
                risposta2 = risposta2 + risposta2Char[i];
            }

//Invio i dati

//------------------ client1 ------------------
            if (lettereIndovinate1 == parolaChiave1.length()){
                outputToClient1.write(1);
                outputToClient1.writeBytes("Complimenti, hai indovinato"+'\n');
                vittoria = true;
            }
            else{
                outputToClient1.write(0);
                outputToClient1.writeBytes("Prova ancora, hai indovinato "+lettereIndovinate1+" di " +parolaChiave1.length()+'\n');
            }
            outputToClient1.writeBytes(risposta1+'\n');

//------------------ client2 ------------------
            if (lettereIndovinate2 == parolaChiave2.length()){
                outputToClient2.write(1);
                outputToClient2.writeBytes("Complimenti, hai indovinato"+'\n');
                vittoria = true;
            }
            else{
                outputToClient2.write(0);
                outputToClient2.writeBytes("Prova ancora, hai indovinato "+lettereIndovinate2+" di " +parolaChiave2.length()+'\n');
            }
            outputToClient2.writeBytes(risposta2+'\n');
            }
    }
    }
}