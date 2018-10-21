import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DATA {
    private Object[] nodes_list;
    private ArrayList<ELEMENT> elements_list;
    private int current_line_index;


    public DATA(String adresse_fichier){

        elements_list = new ArrayList<>();
        long time = System.currentTimeMillis();
        ArrayList<String> recup_fichier = this.getStringFromFile(adresse_fichier);

        int node_id;
        double[] node_coord = new double[3];

        ArrayList<NODE> array_nodes_list = new ArrayList<>();
        PARSER parser = new PARSER(" I (int) X (double) Y (double) Z (double)");

        // Tant que la ligne ne contient pas ".NOE", on avance
        for (current_line_index = 0; recup_fichier.get(current_line_index).contentEquals(".NOE") == false; current_line_index++) {
        }
        while (true){

            // Boucle jusqu'à ".xxx" ou si la ligne est vide ou ne contient que des spaces
            current_line_index++;

            // Utilisation d'un parser pour récupérer les données
            if (parser.parseFromLine(recup_fichier.get(current_line_index)) == -1) {
                break;
            }
            Object[] result = parser.getResult();
            node_id = (int) result[0];
            for (int i = 0; i < 3; i++) {
                node_coord[i] = (double) result[i + 1];
            }
            array_nodes_list.add(new NODE(node_id, node_coord, false));
        }
        System.out.println("Temps d'exécution : " + (System.currentTimeMillis() - time) + "ms");
    }

    private ArrayList<String> getStringFromFile(String adresse_fichier) {
        ArrayList<String> recup_fichier = new ArrayList<>();
        String ligne;
        int nb_ligne = 0;
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(adresse_fichier)));
            try {
                while ((ligne = br.readLine()) != null) {
                    recup_fichier.add(ligne);
                    nb_ligne++;
                }
            } finally {
                br.close();
            }
        } catch (IOException exception) {
            System.out.println("Le fichier n'a pas été trouvé");
        }
        System.out.println("Nombre de ligne à traiter : " + nb_ligne);

        return recup_fichier;
    }

    private void getNodesAndElementsFromFile(Scanner scanner){
        String commande = "";
        int node_id, elem_id;
        ArrayList<NODE> array_nodes_list = new ArrayList<NODE>();
        double[] node_coord = new double[3];
        ArrayList<Integer> node_id_list = new ArrayList<Integer>();

        while (commande.contentEquals(".NOE") == false ){
            commande = scanner.next();
        }
        commande = scanner.next();
        while (commande.charAt(0) != '.') {
            try {
                node_id = scanner.nextInt();
                for (int i = 0; i<3; i++){
                    scanner.next();
                    node_coord[i] = scanner.nextDouble();
                }
                array_nodes_list.add(new NODE(node_id, node_coord, false));
                commande = scanner.next();
            }
            catch (NoSuchElementException exception) {
                break;
            }
        }
        this.nodes_list = array_nodes_list.toArray();

        // Si commande = ".MAI", on skip jusqu'à "I"
        if (commande.contentEquals(".MAI")) {
            commande = scanner.next();
            while (commande.contentEquals("I") == false){
                commande = scanner.next();
            }
        }
        // sinon on skip jusqu'à ".MAI" puis on skip jusqu'à "I"
        else {
            while (commande.contentEquals(".MAI") == false ){
                commande = scanner.next();
            }
            commande = scanner.next();
            while (commande.contentEquals("I")){
                commande = scanner.next();
            }
        }
        // on boucle jusqu'à la prochaine commande ".xxx" ou la fin du fichier
        while (commande.contentEquals(".") == false){
            try {
                // on récupère l'ID de l'élément courant
                elem_id = scanner.nextInt();
                scanner.next();
                scanner.next();

                // Tant qu'il y a des noeuds, on les recupere
                while (true){
                    try {
                        node_id = scanner.nextInt();
                    }
                    // si la prochaine occurrence est "$" on incremente de nouveau
                    catch (Exception isDollarOrEndElement){
                        commande = scanner.next();
                        if (commande.contentEquals("$")){
                            node_id = scanner.nextInt();
                        }
                        // sinon il s'agit d'un nouvel élément ou "!" auquel cas on sort du while
                        else break;
                    }

                    // l'ID = 0 implique un changement de face dans un même élément
                    if (node_id == 0){
                        // on ajoute la face creee avec node_id_list à l'élément
                        elements_list.add(new ELEMENT(new FACE(this.recupNodesFromId(node_id_list))));
                        // on vide la liste pour la face suivante
                        node_id_list.clear();
                    }
                    else node_id_list.add(node_id);
                }
                // si on est sorti de la boucle car "!", on skip jusqu'à "I"
                if (commande.contentEquals("!")){
                    while (commande.contentEquals("I") == false){
                        commande = scanner.next();
                    }
                }
                // on ajoute la dernière face puis on boucle
                elements_list.get(elements_list.size()-1).addFace(new FACE(this.recupNodesFromId(node_id_list)));
            }
            catch (NoSuchElementException exception) {
                break;
            }
        }

    }
    
    private ArrayList<NODE> recupNodesFromId(ArrayList<Integer> param_nodes_list){
        ArrayList<NODE> final_nodes_list = new ArrayList<NODE>();

        /*for (int id : param_nodes_list)
            for (Object node_in_list : this.nodes_list)
                if (node_in_list.getNode_id() == id){
                    final_nodes_list.add(node_in_list);
                    break;
                }*/
        return final_nodes_list;
    }

    private int convertInteger(String ligne, int position){
        int i = 0;
        int debut, fin;
        while (ligne.charAt(i) != 'I') {
            i++;
        }
        i++;
        while (ligne.charAt(i) == ' '){
            i++;
        }
        debut = i;
        while (ligne.charAt(i) != ' '){
            i++;
        }
        fin = i;

        return Integer.parseInt(ligne.substring(debut,fin));
    }
}
