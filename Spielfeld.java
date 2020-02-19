public class Spielfeld {
    private char[][] seefeld = new char[10][10];
        
    public Spielfeld() {
        initSpielfeld(seefeld);
        //zeigeSpielfeld();      
    }

    public void initSpielfeld(char[][] pSpielfeld) {
        for (int i=0;i<pSpielfeld.length;i++) {
            for (int j=0;j<pSpielfeld.length;j++) {
                pSpielfeld[i][j]='~';
            }
        }
    }
    
    public void zeigeSpielfeld() {
        System.out.println(" 12345678910");
        for (int i=0;i<seefeld.length;i++) {
            int  asciiZahl    = 65+i;
            char asciiZeichen = (char)asciiZahl;
            System.out.print( asciiZeichen  );
            for (int j=0;j<seefeld.length;j++) {
                System.out.print(seefeld[i][j]);
            }
            System.out.println();
        }
    }    
    public void schiffSetzen (String pFeld){
        int zahl = (int) pFeld.charAt(0);
        zahl = zahl - 65;
        System.out.println(zahl);
        System.out.println(pFeld.charAt(1)-48);
        seefeld[zahl][pFeld.charAt(1)-48] = '#';        
    }
    public char gibFeld (String pFeld){
        int zahl = (int) pFeld.charAt(0);
        zahl = zahl -65;
        char zeichenAufFeld = (seefeld[zahl][pFeld.charAt(1)-48] );
        return zeichenAufFeld;
    }
    public void setzeFeld (String pFeld, char pZeichen){
        int zahl = (int) pFeld.charAt(0);
        zahl = zahl -65;
        seefeld[zahl][pFeld.charAt(1)-48] = pZeichen;
    }
}

