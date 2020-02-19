import java.util.Scanner;

public class Spiel {
    int intAktuellerSpieler = 0;
    Scanner sc = new Scanner(System.in);
    
    String [] names = new String[2];
    Spieler [] spieler = new Spieler[2];
   
    public Spiel() {
        System.out.println("Spiel Schiffeversenken!\n");
        
        System.out.print("Gebe Name von Spieler 1 ein: ");
        names[0] = sc.nextLine();
        
        System.out.print("Gebe Name von Spieler 2 ein: ");
        names[1] = sc.nextLine();
        
        spieler[0] = new Spieler(names[0]);
        spieler[1] = new Spieler(names[1]);
        this.startSpiel();
    }

    public void startSpiel() {
        for(int counter = 1; counter >= 0; counter--) {
            this.setzeSchiffe();
        }
        this.step();
    }
    
    private void setzeSchiffe (){
        System.out.println("\n"+names[intAktuellerSpieler]+": Setze deine Schiffe!");
        int [] laengeSchiffe = {5, 4};//4, 3, 3, 3, 2, 2, 2, 2};
        for (int schiff: laengeSchiffe) {
            boolean validSchiff = false;
            while (validSchiff == false) {
                System.out.println("\nSchiff der Länge: "+schiff);
                String ausrichtung = "";
                String feld = "";
                int tester = 0;
                while (tester == 0) {
                    System.out.print("Gebe Ausrichtung des Schiffs an: ");
                    ausrichtung = sc.nextLine().strip();
                    if (ausrichtung.equals("1") || ausrichtung.equals("0") || ausrichtung.equals("2") || ausrichtung.equals("3")) {
                        tester = 1;
                    } else {
                        System.out.println("Ungültige Eingabe!");
                    }
                }
                tester = 0;
                while (tester == 0) {
                    System.out.print("Gebe Startfeld des Schiffs an: ");
                    feld = sc.nextLine().strip();
                    if(this.pruefeEingabe(feld) == 1) {
                        tester = 1;
                    } else {
                        System.out.println("Ungültige Eingabe!");
                    }
                }
                
                feld = feld.charAt(0) + String.valueOf(Integer.parseInt(feld.substring(1, feld.length()))-1);
                //Schiff überprüfen
                System.out.println(feld + " " + ausrichtung + " " + String.valueOf(schiff));
                validSchiff = spieler[intAktuellerSpieler].setzeSchiff(feld, Integer.parseInt(ausrichtung), schiff);
            }
        }
        System.out.println("\nSchiffe setzen beendet! Das ist dein Spielfeld:\n");
        //Ausgabe des eigenen Felds
        spieler[intAktuellerSpieler].meinSpielfeld.zeigeSpielfeld();
        
        System.out.print("Positionierung beendet! Bestätige durch ENTER!");
        sc.nextLine();
        
        this.waitForNext();
    }
    
    private int pruefeEingabe (String pInput){
        if(pInput.length() == 2 || pInput.length() == 3) {
            int feldPos1 = (int) pInput.charAt(0);
            int feldPos2 = Character.getNumericValue(pInput.charAt(1));
            if (feldPos2 == 1 && pInput.length() == 3 && pInput.charAt(2) == '0') {
                feldPos2 = 10;
            }
            if(feldPos1 >= 65 && feldPos1 <= 74 && ((feldPos2 >= 1 && feldPos2 < 10 && pInput.length() == 2) || (feldPos2 == 10 && pInput.length() == 3))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
    
    private void step () {
        System.out.println(names[intAktuellerSpieler]+": Greife an!\n");
        
        System.out.println("Spielfelder:\n");
        //Ausgabe des eigenen Felds und des gegnerischen Felds
        //
        
        int tester = 0;
        String feld = "";
        while (tester == 0) {
            System.out.print("Gebe Feld zum Schießen an: ");
            feld = sc.nextLine().strip();
            if(this.pruefeEingabe(feld) == 1) {
                tester = 1;
            } else {
                System.out.println("Ungültige Eingabe!");
            }
        }
        
        //Anweisung zum Schießen
        //
        
        System.out.println("\nErgebnis: ");
        System.out.println("Spielfelder:\n");
        //Ausgabe des eigenen Felds und des gegnerischen Felds
        //
        
        //Wenn Spiel noch nicht beendet
        System.out.print("Zug beendet! Bestätige durch ENTER!");
        sc.nextLine();
        
        this.waitForNext();
        this.step();
        
        //Sonst
    }
    
    private void waitForNext() {
        intAktuellerSpieler = (intAktuellerSpieler + 1)%2;
        for (int counter = 0; counter < 50; counter++) {
            System.out.println("\n");
        }
        System.out.print("Bist du "+names[intAktuellerSpieler]+"? Bestätige durch ENTER!");
        sc.nextLine();
        for (int counter = 0; counter < 50; counter++) {
            System.out.println("\n");
        }
    }
}
