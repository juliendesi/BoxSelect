
import java.util.ArrayList;

import static java.lang.Character.isDigit;

public class PARSER {
    private ArrayList<Object>[] result;

    private String pattern;
    private int position_pattern;
    private String pattern_temp;
    private String ligne_courante;
    private int position_ligne_courante;

    private char carac_return;
    private char carac_arret;

    private int debut_texte;
    private ArrayList<String> texte;

    private boolean detected_error;
    private boolean fin_line_detected;
    private boolean pattern_unrecognized;
    private boolean carac_return_detected;


    public PARSER(String param_pattern, char param_carac_return) {
        pattern = param_pattern;
        position_pattern = 0;
        pattern_temp = "";
        ligne_courante = "";
        position_ligne_courante = 0;

        result = new ArrayList[getNumberResultFromPattern()];
        for (int i = 0; i < getNumberResultFromPattern(); i++) {
            result[i] = new ArrayList<>(20);
        }

        carac_return = param_carac_return;
        carac_arret = '\0';

        debut_texte = -1;
        texte = null;

        detected_error = false;
        carac_return_detected = false;
        fin_line_detected = false;
        pattern_unrecognized = false;
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
        int sortie;
        this.resetError();
        try {
            int debut = 0;
            sortie = recognizePattern(pattern, debut, ligne, debut, 0);
        } catch (StringIndexOutOfBoundsException erreur){
            System.out.println("Erreur : StringIndexOutOfBoundsException détectée");
            return -1;
        }
        return sortie;
    }

    public int parseFromText(ArrayList<String> param_texte) {
        if (debut_texte == -1) {
            System.out.println("L'indice du début de la lecture n'est pas définit");
            return -1;
        }
        int debut = 0;
        this.resetError();
        texte = param_texte;
        String ligne = "";
        int sortie;

        try {
            sortie = recognizePattern(pattern, debut, ligne, debut, 0);
        } catch (StringIndexOutOfBoundsException erreur) {
            System.out.println("Erreur : StringIndexOutOfBoundsException détectée");
            return -1;
        }
        return sortie;
    }

    // traite le cas : " I (int) X (double) Y (double) Z (double)"
    // traite le cas : "I (int) N [(int) ] */0 [(int) ]" avec gestion des retours de ligne "$" et suite facultative "*"
    // [] implique un motif répétable (motif entre crochets) dont le critère d'arrêt est
    //      soit un caractère non récupéré
    //      soit une fin de ligne atteinte sans retour chariot
    //      les valeurs sont stockées dans une ArrayList<Object>
    private int recognizePattern(String param_pattern, int debut_pattern, String param_ligne, int debut_line, int param_index_result) {
        int position = debut_line, index_result = param_index_result;
        char carac;
        String commande = "", ligne = param_ligne;
        if (ligne == "") {
            ligne = texte.get(debut_texte);
            carac_return_detected = false;
        }

        for (int i = debut_pattern; i < param_pattern.length(); i++) {
            carac = param_pattern.charAt(i);
            if (fin_line_detected == true) { //
                //tester si le prochain carac autre que space est facultatif (*)
                int find_option = i;
                find_option = ignoreSpace(param_pattern, find_option);
                if (param_pattern.charAt(find_option) == '*') {
                    return position;
                } else return -1;
            }
            if (pattern_unrecognized == true) {
                return -1;
            }
            if (detected_error == true) {
                return position;
            }
            if (carac_return_detected == true) {
                ligne = texte.get(debut_texte);
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
                while (detected_error != true && pattern_unrecognized != true) {
                    position = recognizePattern(pattern_temp, 0, ligne, position, index_result);
                }
                if (carac_return_detected == true && pattern_unrecognized == false) {
                    ligne = texte.get(debut_texte);
                    carac_return_detected = false;
                }

                if (fin_line_detected == false && pattern_unrecognized == false) {
                    if (ligne.charAt(position) == carac_arret) {
                        detected_error = false;
                    }
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
            } else if (isDigit(carac) == false && carac != '*') {
                position = ignoreWord(ligne, position, carac);
            }
        }
        return position;
    }

    public ArrayList<Object>[] getResult() {
        return result;
    }

    private int ignoreSpace(String ligne, int debut){
        int i;
        boolean carac_detected = false;

        if (debut == 0 && ligne.length() == 0 || debut == ligne.length()) { // si ligne sans carac ou debut ligne hors bornes ligne
            detected_error = true;
            fin_line_detected = true;
            return debut;
        } else if (debut == 0 && ligne.charAt(debut) != ' ') {  // si pas d'espace en debut de ligne
            return debut;
        }

        if (ligne.charAt(debut) == carac_return) {  // gestion retour à la ligne dans cas recognizeFromText
            carac_return_detected = true;
            debut_texte++;
            if (debut_texte == texte.size()) {
                pattern_unrecognized = true;
                return debut;
            }
            debut = 0;
            ligne = texte.get(debut_texte);
        }
        for (i = debut; i < ligne.length(); i++)    // on avance dans la ligne tant que carac différent de space
            if (ligne.charAt(i) != ' ') {
                carac_detected = true;
                break;
            }

        if (carac_detected == false) {  // fin de ligne atteinte sans detection de caractere
            detected_error = true;
            fin_line_detected = true;
            return i;
        }
        return i;
    }

    private int ignoreWord(String ligne, int debut, char c) {
        if (ligne.charAt(debut) == carac_return) {
            // gestion retour à la ligne dans cas recognizeFromText
            carac_return_detected = true;
            debut_texte++;
            if (debut_texte == texte.size()) {
                pattern_unrecognized = true;
                return debut;
            }
            debut = 0;
            ligne = texte.get(debut_texte);
            debut = ignoreSpace(ligne, debut);
        }

        if (debut == ligne.length()) {
            detected_error = true;
            fin_line_detected = true;
            return debut;
        } else if (ligne.charAt(debut) == c) {
            return debut + 1;
        }
        detected_error = true;
        pattern_unrecognized = true;
        return debut;
    }

    private int getInteger(String ligne, int debut, int indice_result){
        int i;
        boolean char_detected = false;

        if (ligne.charAt(debut) == carac_return) {
            // gestion retour à la ligne dans cas recognizeFromText
            carac_return_detected = true;
            debut_texte++;
            if (debut_texte == texte.size()) {
                pattern_unrecognized = true;
                return debut;
            }
            debut = 0;
            ligne = texte.get(debut_texte);
            debut = ignoreSpace(ligne, debut);
        }

        if (debut == ligne.length()) {
            detected_error = true;
            fin_line_detected = true;
            return debut;
        } else if (ligne.charAt(debut) == carac_arret) {
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
                pattern_unrecognized = true;
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

        if (ligne.charAt(debut) == carac_return) {
            // gestion retour à la ligne dans cas recognizeFromText
            carac_return_detected = true;
            debut_texte++;
            if (debut_texte == texte.size()) {
                pattern_unrecognized = true;
                return debut;
            }
            debut = 0;
            ligne = texte.get(debut_texte);
            debut = ignoreSpace(ligne, debut);
        }

        if (debut == ligne.length()) {
            detected_error = true;
            fin_line_detected = true;
            return debut;
        } else if (ligne.charAt(debut) == carac_arret) {
            detected_error = true;
            return debut;
        }

        for (i = debut; i < ligne.length(); i++) {
            if (ligne.charAt(i) == ' ') {
                char_detected = true;
                break;
            }
            if (isDigit(ligne.charAt(i)) == false)
                if (ligne.charAt(i) != '.' && ligne.charAt(i) != '+' && ligne.charAt(i) != '-' && ligne.charAt(i) != 'E') {
                    detected_error = true;
                    pattern_unrecognized = true;
                    return debut;
                }
        }

        if (char_detected == false) {
            result[indice_result].add(Double.parseDouble(ligne.substring(debut)));
        } else result[indice_result].add(Double.parseDouble(ligne.substring(debut, i)));

        return i;
    }

    private int gestionCrochet() {

    }

    public int getDebut_texte() {
        return debut_texte;
    }

    public void setDebut_texte(int debut_texte) {
        this.debut_texte = debut_texte;
    }

    public void clearResult() {
        for (int i = 0; i < result.length; i++) {
            result[i].clear();
        }
    }

    private void resetError() {
        detected_error = false;
        carac_return_detected = false;
        fin_line_detected = false;
        pattern_unrecognized = false;
    }

    private void SAMCEF_recognizeGroupOfElem(String ligne) {
        // avancer jusqu'à "I"
        // ignore spaces
        // si "I"
        // recuperer jusqu'à "J" ou fin de ligne
        // si "J"
        // recuperer le dernier resultat --> X
        // recuperer valeur après "J" --> Y
        // ajouter de X à Y par increment de 1
    }
}
