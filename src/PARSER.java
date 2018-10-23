
import java.util.ArrayList;

import static java.lang.Character.isDigit;

public class PARSER {
    private ArrayList<Object>[] result;
    private String pattern;
    private String pattern_temp;
    private char carac_return;
    private int debut_texte;
    private boolean detected_error;
    private char carac_arret;

    public PARSER(String param_pattern, char param_carac_return) {
        pattern = param_pattern;
        pattern_temp = "";
        result = new ArrayList[getNumberResultFromPattern()];
        for (int i = 0; i < getNumberResultFromPattern(); i++) {
            result[i] = new ArrayList<>();
        }
        carac_return = param_carac_return;
        debut_texte = -1;
        detected_error = false;
        carac_arret = '\0';
    }

    private int getNumberResultFromPattern(){
        int nb_result = 0;
        for (int i = 0; i< pattern.length(); i++){
            if (pattern.charAt(i) == '[') {
                nb_result++;
                i++;
                continue;
            }
            if (pattern.charAt(i) == '('){nb_result++;}
        }
        return nb_result;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        result = new ArrayList[getNumberResultFromPattern()];
        for (int i = 0; i < getNumberResultFromPattern(); i++) {
            result[i] = new ArrayList<>();
        }
    }

    public int parseFromLine(String ligne) {
        try {
            int debut = 0;
            recognizePattern(pattern, debut, ligne, debut, 0);
            if (detected_error == true) {
                return -1;
            }
        } catch (StringIndexOutOfBoundsException erreur){
            System.out.println("Erreur : StringIndexOutOfBoundsException détectée");
            return -1;
        }
        return 1;
    }

    public int parseFromText(String[] texte) {
        if (debut_texte == -1) {
            System.out.println("L'indice du début de la lecture n'est pas définit");
            return -1;
        }
        int debut = 0;
        String ligne = "";

        try {
            recognizePattern(pattern, debut, ligne, debut, 0);
        } catch (StringIndexOutOfBoundsException erreur) {
            System.out.println("Erreur : StringIndexOutOfBoundsException détectée");
            return -1;
        }
        return 1;
    }

    // traite le cas : " I (int) X (double) Y (double) Z (double)"
    // traite le cas : "I (int) N [(int) ] /0 [(int) ]" avec gestion des retours de ligne "$"
    // [] implique un motif répétable (motif entre crochets) dont le critère d'arrêt est
    //      soit un caractère non récupéré
    //      soit une fin de ligne atteinte sans retour chariot
    //      les valeurs sont stockées dans une ArrayList<Object>
    private int recognizePattern(String param_pattern, int debut_pattern, String ligne, int debut_line, int param_index_result) {
        int position = debut_line, index_result = param_index_result;
        char carac;
        String commande = "";
        for (int i = debut_pattern; i < param_pattern.length(); i++) {
            carac = param_pattern.charAt(i);
            if (detected_error == true) {
                return position;
            }
            if (carac == ' ') {
                position = ignoreSpace(ligne, position);
            } else if (carac == '[') {
                i++;
                while (param_pattern.charAt(i) != ']') {
                    pattern_temp = pattern_temp + param_pattern.charAt(i);
                    i++;
                }
                for (int a = i; a < param_pattern.length(); a++)
                    if (param_pattern.charAt(a) == '/') {
                        carac_arret = param_pattern.charAt(a + 1);
                        break;
                    }

                while (detected_error != true) {
                    position = recognizePattern(pattern_temp, 0, ligne, position, index_result);
                }
                if (ligne.charAt(position) == carac_arret) {
                    detected_error = false;
                }
                pattern_temp = "";
                index_result++;
            } else if (carac == '(') {
                i++;
                while (param_pattern.charAt(i) != ')') {
                    commande = commande + param_pattern.charAt(i);
                    i++;
                }
                switch (commande) {
                    case "int":
                        position = getInteger(ligne, position, index_result);
                        break;
                    case "double":
                        position = getDouble(ligne, position, index_result);
                        break;
                }
                index_result++;
                commande = "";
            } else if (carac == '/') {
                i++;
                carac = param_pattern.charAt(i);
                position = ignoreWord(ligne, position, carac);
            } else if (isDigit(carac) == false) {
                position = ignoreWord(ligne, position, carac);
            }
        }
        return position;
    }

    public Object[] getResult() {
        return result;
    }

    private int ignoreSpace(String ligne, int debut){
        int i;
        boolean carac_detected = false;

        if (debut == 0 && ligne.length() == 0 || debut == ligne.length()) {
            detected_error = true;
            return debut;
        } else if (ligne.charAt(debut) == carac_return) {
            // gestion retour à la ligne dans cas recognizeFromText
        } else if (debut == 0 && ligne.charAt(debut) != ' ') {
            return debut;
        }

        for (i = debut; i < ligne.length(); i++)
            if (ligne.charAt(i) != ' ') {
                carac_detected = true;
                break;
            }

        if (debut == 0 && carac_detected == false) {
            detected_error = true;
            return debut;
        } else if (carac_detected == false) {
            detected_error = true;
            return i;
        }
        return i;
    }

    private int ignoreWord(String ligne, int debut, char c) {
        if (debut == ligne.length()) {
            detected_error = true;
            return debut;
        }
        if (ligne.charAt(debut) == c) {
            return debut + 1;
        }
        detected_error = true;
        return debut;
    }

    private int getInteger(String ligne, int debut, int indice_result){
        int i;
        boolean char_detected = false;
        if (debut == ligne.length() || ligne.charAt(debut) == carac_arret) {
            detected_error = true;
            return debut;
        }
        for (i = debut; i < ligne.length(); i++) {
            if (ligne.charAt(i) == ' ') {
                char_detected = true;
                break;
            }
            if (isDigit(ligne.charAt(i)) == false) {
                detected_error = true;
                return debut;
            }
        }


        if (char_detected == false) {
            result[indice_result].add(Integer.parseInt(ligne.substring(debut)));
        } else result[indice_result].add(Integer.parseInt(ligne.substring(debut, i)));

        return i;
    }

    private int getDouble(String ligne, int debut, int indice_result){
        int i;
        boolean char_detected = false;
        if (debut == ligne.length() || ligne.charAt(debut) == carac_arret) {
            detected_error = true;
            return debut;
        }
        for (i = debut; i < ligne.length(); i++) {
            if (isDigit(ligne.charAt(i)) == false)
                if (ligne.charAt(i) != '.') {
                    detected_error = true;
                    return debut;
                }

            if (ligne.charAt(i) == ' ') {
                char_detected = true;
                break;
            }
        }

        if (char_detected == false) {
            result[indice_result].add(Double.parseDouble(ligne.substring(debut)));
        } else result[indice_result].add(Double.parseDouble(ligne.substring(debut, i)));

        return i;
    }
}
