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
    public Object[] getResultFromLine(String ligne){
        try {
            int position = 0;
            position = ignoreSpace(ligne, position);
            position = ignoreWord(ligne,position);
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
            System.out.println("Erreur");
        }


        return result;
    }

    private int ignoreSpace(String ligne, int debut){
        int i;
        if (debut == 0 && ligne.charAt(debut) != ' '){
            return debut;
        }
        for (i = debut; ligne.charAt(i) == ' ';i++);
        return i;
    }

    private int ignoreWord(String ligne, int debut){
        int i;
        for (i = debut; ligne.charAt(i) != ' ';i++);
        return i;
    }

    private int getInteger(String ligne, int debut, int indice_result){
        int i;
        for (i = debut; ligne.charAt(i) != ' ';i++);
        result[indice_result] = Integer.parseInt(ligne.substring(debut,i));
        return i;
    }

    private int getDouble(String ligne, int debut, int indice_result){
        int i;
        for (i = debut; ligne.charAt(i) != ' ' ;i++);
        result[indice_result] = Double.parseDouble(ligne.substring(debut,i));
        return i;
    }

}
