public class MAIN {
    public static void main(String[] args){
        //DATA data = new DATA("/Users/Julien/IdeaProjects/maillage_V11.dat");
        //String chaine = "1.56E-06";
        PARSER parser = new PARSER(" I (int) X (double) Y (double) Z (double)", '$');
        parser.setPattern(" I (int) N [(int) ] /0 [(int) ]");
        String ligne = "I 3   N 2 4 2 0 3 2";
        if (parser.parseFromLine(ligne) == -1) {
            System.out.println("fin de la ligne");
        }

    }
}
