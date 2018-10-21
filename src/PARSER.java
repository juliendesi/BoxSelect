public class PARSER {
    private Object[] result;
    private String pattern;

    public PARSER(String param_pattern){
        pattern = param_pattern;
        result = new Object[getNumberResultFromPattern()];
    }

    private int getNumberResultFromPattern(){
        int nb_result = 0;
        for (int i = 0; i< pattern.length(); i++){
            if (pattern.charAt(i) == '('){nb_result++;}
        }
        return nb_result;
    }

    // traite le cas : " I (int) X (double) Y (double) Z (double)"
    public int parseFromLine(String ligne) {
        try {
            int position = 0;
            if ((position = ignoreSpace(ligne, position)) == -1) {
                return -1;
            }
            if ((position = ignoreWord(ligne, position)) == -1) {
                return -1;
            }
            position = ignoreSpace(ligne, position);
            position = getInteger(ligne,position,0);
            position = ignoreSpace(ligne, position);
            position = ignoreWord(ligne,position);
            position = ignoreSpace(ligne, position);
            position = getDouble(ligne,position,1);
            position = ignoreSpace(ligne, position);
            position = ignoreWord(ligne,position);
            position = ignoreSpace(ligne, position);
            position = getDouble(ligne,position,2);
            position = ignoreSpace(ligne, position);
            position = ignoreWord(ligne,position);
            position = ignoreSpace(ligne, position);
            position = getDouble(ligne,position,3);
        }
        catch (StringIndexOutOfBoundsException erreur){
            System.out.println("Erreur : StringIndexOutOfBoundsException détectée");
            return -1;
        }
        return 1;
    }

    public Object[] getResult() {
        return result;
    }

    private int ignoreSpace(String ligne, int debut){
        int i;
        if (debut == 0 && ligne.length() == 0) {
            return -1;
        }
        if (debut == 0 && ligne.charAt(debut) != ' ') {
            return debut;
        }

        for (i = debut; ligne.charAt(i) == ' ' && i < ligne.length() - 1; i++) ;
        if (debut == 0 && i == ligne.length() - 1) {
            return -1;
        }
        return i;
    }

    private int ignoreWord(String ligne, int debut){
        int i;
        if (ligne.charAt(debut) == '.') {
            return -1;
        }
        for (i = debut; ligne.charAt(i) != ' ' && i < ligne.length() - 1; i++) ;
        return i;
    }

    private int getInteger(String ligne, int debut, int indice_result){
        int i;
        for (i = debut; ligne.charAt(i) != ' ' && i < ligne.length() - 1; i++) ;
        result[indice_result] = Integer.parseInt(ligne.substring(debut,i));
        return i;
    }

    private int getDouble(String ligne, int debut, int indice_result){
        int i;
        for (i = debut; ligne.charAt(i) != ' ' && i < ligne.length() - 1; i++) ;
        if (i == ligne.length() - 1) {
            result[indice_result] = Double.parseDouble(ligne.substring(debut));
        } else result[indice_result] = Double.parseDouble(ligne.substring(debut, i));
        return i;
    }
}
