public class Spieler {
     
     private String playername = "";
     Spielfeld meinSpielfeld = new Spielfeld();
    
     public Spieler(String pPlayerName) {
         playername = pPlayerName;
     } 
     
     public void beschossen(String pSchussZiel) {
        char zeichenAufFeld = meinSpielfeld.gibFeld(pSchussZiel);
        if ( zeichenAufFeld == '#' ) {
            meinSpielfeld.setzeFeld(pSchussZiel,'X'); //Schiff getroffen
        }
        if ( zeichenAufFeld == '~' ) {
            meinSpielfeld.setzeFeld(pSchussZiel,'O'); //Wasser getroffen
        }        
     }
     
     public boolean setzeSchiff(String pStartkoordinate,int pAusrichtung, int pLaenge ) {
         // 0 = oben; 1 = rechts; 2 = unten; 3 = links;
        int[] intStart = {
            letterToInt(String.valueOf(pStartkoordinate.charAt(0))),
            Integer.parseInt(String.valueOf(pStartkoordinate.charAt(1)))
        };
        String finaleKoordinaten = "";
        System.out.println(intStart[0]);
        System.out.println(intStart[1]);
        boolean valid = true;
        switch(pAusrichtung) {
            case 0:
                for(int i = intStart[0]; i > intStart[0] - pLaenge; i--) {
                    finaleKoordinaten += intToLetter(i) + String.valueOf(intStart[1]);
                    valid = invalid(i, intStart[1]);
                }
                break;

            case 1:
                for(int i = intStart[1]; i < intStart[1] + pLaenge; i++) {
                    finaleKoordinaten += intToLetter(intStart[0]) + String.valueOf(i);
                    valid = invalid(intStart[0], i);
                }
                break;

            case 2:
                for(int i = intStart[0]; i < intStart[0] + pLaenge; i++) {
                    finaleKoordinaten += intToLetter(i) + String.valueOf(intStart[1]);
                    valid = invalid(i, intStart[1]);
                }
                break;

            case 3:
                for(int i = intStart[1]; i < intStart[1] - pLaenge; i--) {
                    finaleKoordinaten += intToLetter(intStart[0]) + String.valueOf(i);
                    valid = invalid(intStart[1], i);
                }
                break;
        }
        if (valid) {
            for(int counter = 0; counter < finaleKoordinaten.length(); counter=counter+2) {
                meinSpielfeld.schiffSetzen(finaleKoordinaten.substring(counter, counter+2));
            }
        }
        return valid;
     } 
     
     private boolean invalid(int letter, int number) {
         if(letter >= 0 && letter <= 10 && number >= 0 && number <= 10) { return true; } else { return false; }
     }
     public int letterToInt(String letter) { 
         char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
         int index = 0;
         for( int i = 0; i < letters.length; i++) {
                if(letters[i] == letter.charAt(0)) {
                    index = i;
                }
         }
         
         return index;
     }
     
     public String intToLetter(int position) {
         char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
         return String.valueOf(letters[position]);
     }
     
 }

