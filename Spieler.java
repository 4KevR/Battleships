public class Spieler {
     
     private String playername = "";
     Spielfeld meinSpielfeld = new Spielfeld();
     char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
     int schiffeGetroffen = 0;
     int anzahlSchiffPunkte = 0;
    
     public Spieler(String pPlayerName, int anzahlSchiffPunkte) {
         playername = pPlayerName;
         this.anzahlSchiffPunkte = anzahlSchiffPunkte;
     } 
     
     public int beschossen(String pSchussZiel) {
        char zeichenAufFeld = meinSpielfeld.gibFeld(pSchussZiel);
        if ( zeichenAufFeld == '#' ) {
            meinSpielfeld.setzeFeld(pSchussZiel,'X');
            schiffeGetroffen += 1;
            if (schiffeGetroffen == anzahlSchiffPunkte) {
                return 4;  // Spiel gewonnen
            } else if (this.countSchiffe(pSchussZiel, "") == 0) {
                return 3;  // Schiff zerstört
            } else {
                return 1;  // Schiff getroffen
            }
        } else if ( zeichenAufFeld == '~' ) {
            meinSpielfeld.setzeFeld(pSchussZiel,'O');  // Wasser getroffen
            return 2;
        } else {
            return 0;  // Feld wurde bereits beschossen - wiederhole Eingabe
        }
     }
     
     public boolean setzeSchiff(String pStartkoordinate,int pAusrichtung, int pLaenge ) {
        // 0 = oben; 1 = rechts; 2 = unten; 3 = links;
        int[] intStart = {
            letterToInt(String.valueOf(pStartkoordinate.charAt(0))),
            Integer.parseInt(String.valueOf(pStartkoordinate.charAt(1)))
        };
        String finaleKoordinaten = "--";
        boolean valid = true;
        switch(pAusrichtung) {
            case 0:
                for(int i = intStart[0]; i > intStart[0] - pLaenge; i--) {
                    valid = invalid(i, intStart[1], finaleKoordinaten.substring(finaleKoordinaten.length()-2, finaleKoordinaten.length()));
                    if (valid) {
                        finaleKoordinaten += intToLetter(i) + String.valueOf(intStart[1]);
                    } else {
                        break;
                    }
                }
                break;

            case 1:
                for(int i = intStart[1]; i < intStart[1] + pLaenge; i++) {
                    valid = invalid(intStart[0], i, finaleKoordinaten.substring(finaleKoordinaten.length()-2, finaleKoordinaten.length()));
                    if (valid) {
                        finaleKoordinaten += intToLetter(intStart[0]) + String.valueOf(i);
                    } else {
                        break;
                    }
                }
                break;

            case 2:
                for(int i = intStart[0]; i < intStart[0] + pLaenge; i++) {
                    valid = invalid(i, intStart[1], finaleKoordinaten.substring(finaleKoordinaten.length()-2, finaleKoordinaten.length()));
                    if (valid) {
                        finaleKoordinaten += intToLetter(i) + String.valueOf(intStart[1]);
                    } else {
                        break;
                    }
                }
                break;

            case 3:
                for(int i = intStart[1]; i > intStart[1] - pLaenge; i--) {
                    valid = invalid(intStart[0], i, finaleKoordinaten.substring(finaleKoordinaten.length()-2, finaleKoordinaten.length()));
                    if (valid) {
                        finaleKoordinaten += intToLetter(intStart[0]) + String.valueOf(i);
                    } else {
                        break;
                    }
                }
                break;
        }
        if (valid) {
            for(int counter = 2; counter < finaleKoordinaten.length(); counter=counter+2) {
                meinSpielfeld.schiffSetzen(finaleKoordinaten.substring(counter, counter+2));
            }
        }
        return valid;
     } 
     
     private boolean invalid(int letter, int number, String letztesFeld) {
         if(letter >= 0 && letter <= 9 && number >= 0 && number <= 9) { 
             String feldPrüfung = intToLetter(letter)+String.valueOf(number);
             int anzahlSchiff = this.countSchiffe(feldPrüfung, letztesFeld);
             if (anzahlSchiff == 0) {
                 return true;
             }
         }
         return false;
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
         return String.valueOf(letters[position]);
     }
     
     public int countSchiffe(String startFeld, String letztesFeld) {
         int counterSchiff = 0;
         String feld = "";
         int letter = this.letterToInt(String.valueOf(startFeld.charAt(0)));
         int number = Integer.parseInt(String.valueOf(startFeld.charAt(1)));
         for (int y = letter-1; y < letter+2; y++) {
             for (int x = number-1; x < number+2; x++) {
                 if (y >= 0 && y <= 9 && x >= 0 && x <= 9) {
                     feld = intToLetter(y) + String.valueOf(x);
                     if (meinSpielfeld.gibFeld(feld) == '#' && !feld.equals(letztesFeld)) {
                         counterSchiff += 1;
                     }
                 }
             }
         }
         return counterSchiff;
     }
}

