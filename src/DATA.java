import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DATA {
    private ArrayList<NODE> nodes_list;
    private ArrayList<ELEMENT> elements_list;
    private int current_line_index;


    public DATA(String adresse_fichier){
        nodes_list = new ArrayList<>();
        elements_list = new ArrayList<>();
        long time = System.currentTimeMillis();
        ArrayList<String> recup_fichier = this.getStringFromFile(adresse_fichier);

        int node_id;
        double[] node_coord = new double[3];

        PARSER parser = new PARSER(" I (int) X (double) Y (double) Z (double)", '$');

        // Tant que la ligne ne contient pas ".NOE", on avance
        for (current_line_index = 0; recup_fichier.get(current_line_index).contentEquals(".NOE") == false; current_line_index++)
            ;
        while (true){

            // Boucle jusqu'à ".xxx" ou si la ligne est vide ou ne contient que des spaces
            current_line_index++;

            // Utilisation d'un parser pour récupérer les données
            if (parser.parseFromLine(recup_fichier.get(current_line_index)) == -1) {
                if (recup_fichier.get(current_line_index).contains(".") == true) {
                    break;
                } else continue;

            }
            Object[] result = parser.getResult();
            node_id = (int) result[0];
            for (int i = 0; i < 3; i++) {
                node_coord[i] = (double) result[i + 1];
            }
            nodes_list.add(new NODE(node_id, node_coord, false));
        }

        for (int i = current_line_index; recup_fichier.get(i).contentEquals(".MAI") == false; i++) {
            current_line_index = i;
        }
        parser.setPattern("I (int) N [(int) ]/ 0 [(int) ]");
        while (true) {
            current_line_index++;
            if (parser.parseFromLine(recup_fichier.get(current_line_index)) == -1) {
                if (recup_fichier.get(current_line_index).contains(".") == true) {
                    break;
                } else continue;

            }
            Object[] result = parser.getResult();
            node_id = (int) result[0];
            for (int i = 0; i < 3; i++) {
                node_coord[i] = (double) result[i + 1];
            }
            nodes_list.add(new NODE(node_id, node_coord, false));
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

    /*private void getNodesAndElementsFromFile(Scanner scanner){
        String commande = "";
        int node_id, elem_id;

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

    }*/
    
    private ArrayList<NODE> recupNodesFromId(ArrayList<Integer> param_nodes_list){
        ArrayList<NODE> final_nodes_list = new ArrayList<>();

        for (int id : param_nodes_list) {
            final_nodes_list.add(searchNodeFromID(id));
        }

        return final_nodes_list;
    }

    private NODE searchNodeFromID(int searched_id) {
        int borne_test, borne_inf, borne_sup;
        int id_test, id_inf, id_sup;

        // Initialisation
        borne_inf = 0;
        borne_sup = nodes_list.size() - 1;

        id_inf = nodes_list.get(borne_inf).getNode_id();
        if (id_inf == searched_id) {
            return nodes_list.get(borne_inf);
        }

        id_sup = nodes_list.get(borne_sup).getNode_id();
        if (id_sup == searched_id) {
            return nodes_list.get(borne_sup);
        }

        borne_test = (borne_inf + borne_sup) / 2;
        while (borne_inf != borne_test) {
            // test de la valeur milieu
            // si valeur cherchée > choisir milieu de la partie sup
            id_test = nodes_list.get(borne_test).getNode_id();
            if (id_test == searched_id) {
                return nodes_list.get(borne_test);
            }

            if (searched_id > id_test) {
                borne_sup = borne_test;
            } else {
                borne_inf = borne_test;
            }
            borne_test = (borne_inf + borne_sup) / 2;
        }
        System.out.println("Erreur : le noeud " + searched_id + " n'est pas présent dans le modèle");
        return null;
    }
}
