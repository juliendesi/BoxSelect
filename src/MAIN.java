public class MAIN {
    public static void main(String[] args){
        DATA data = new DATA("/Users/Julien/IdeaProjects/maillage_V11.dat");
        String chaine = "1.56E-06";
        double a = Double.parseDouble(chaine);
        System.out.println(a);
    }
}
