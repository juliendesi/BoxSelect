import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DATA {
    private ArrayList<NODE> nodes_list;
    private ArrayList<ELEMENT> elements_list;
    private ArrayList<PART> parts_list;
    private int current_line_index;


    public DATA(String adresse_fichier){
        nodes_list = new ArrayList<>();
        elements_list = new ArrayList<>();
        long time = System.currentTimeMillis();
        ArrayList<String> recup_fichier = this.getStringFromFile(adresse_fichier);

        this.getNodesFromText(recup_fichier);
        System.out.println("Récupération des noeuds : " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();
        this.getElementsFromText(recup_fichier);
        System.out.println("Récupération des éléments : " + (System.currentTimeMillis() - time) + "ms");
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

    private void getNodesFromText(ArrayList<String> recup_fichier) {
        int node_id;
        double[] node_coord = new double[3];


        PARSER parser = new PARSER(" I (int) X (double) Y (double) Z (double)", '$');

        // Tant que la ligne ne contient pas ".NOE", on avance
        for (current_line_index = 0; recup_fichier.get(current_line_index).contentEquals(".NOE") == false; current_line_index++)
            ;
        while (true) {
            // Boucle jusqu'à ".xxx" ou si la ligne est vide ou ne contient que des spaces
            current_line_index++;

            // Utilisation d'un parser pour récupérer les données
            if (parser.parseFromLine(recup_fichier.get(current_line_index)) == -1) {
                if (recup_fichier.get(current_line_index).contains(".") == true) {
                    break;
                } else continue;

            }
            ArrayList<Object>[] result = parser.getResult();
            node_id = (int) result[0].get(0);
            for (int i = 0; i < 3; i++) {
                node_coord[i] = (double) result[i + 1].get(0);
            }
            nodes_list.add(new NODE(node_id, node_coord, false));
            parser.clearResult();
        }
    }

    private void getElementsFromText(ArrayList<String> recup_fichier) {
        int elem_id;
        ArrayList<Object>[] result;
        ArrayList<Integer> list_node_face;
        PARSER parser = new PARSER("I (int) N [(int) ] */0 [(int) ]", '$');

        for (int i = current_line_index; recup_fichier.get(i).contentEquals(".MAI") == false; i++) {
            current_line_index = i;
        }
        while (true) {
            current_line_index++;
            parser.setDebut_texte(current_line_index);
            if (parser.parseFromText(recup_fichier) == -1) {
                if (recup_fichier.get(current_line_index).contains("!") == true) {
                    continue;
                }
                if (recup_fichier.get(current_line_index).contains(".") == true) {
                    break;
                } else continue;
            }
            current_line_index = parser.getDebut_texte();

            result = parser.getResult();
            elem_id = (int) result[0].get(0);

            for (int list = 1; list < result.length; list++) {
                list_node_face = this.castTabObjectInTabInteger(result[list]);
                elements_list.add(new ELEMENT(elem_id, new FACE(this.recupNodesFromId(list_node_face))));
            }
            parser.clearResult();
        }
    }

    private void getPartsFromText(ArrayList<String> recup_fichier) {
        PARSER parser_nom = new PARSER(" GROUP /5 NOM \"(String)\" MAILLE", '$');
        PARSER parser_elem = new PARSER(" I (int) J (int)", '$');

        // 1 : tester jusqu'à ".SEL"
        while (current_line_index < recup_fichier.size()) {
            for (int i = current_line_index; recup_fichier.get(i).contentEquals(".SEL") == false; i++) {
                current_line_index = i;
                if (current_line_index == recup_fichier.size()) {
                    System.out.println("Erreur : Pas de selection de groupe définie");
                    return;
                }
            }
            // 3 : tester jusqu'à "GROUP"
            for (int i = current_line_index; recup_fichier.get(i).contains("GROUP") == false; i++) {
                current_line_index = i;
                if (current_line_index == recup_fichier.size()) {
                    System.out.println("Erreur : Pas de groupe définie");
                    return;
                }
            }
            // 3.1 : tester la même ligne avec "MAILLE"
            if (recup_fichier.get(current_line_index).contains("MAILLE") == true) {
                // 3.1.1 : si ok
                // 3.1.1.1 : on recupère le nom de la piece
            }
            // 3.2 : si nok on revient au 1
        }


    }
    
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
                borne_inf = borne_test;
            } else {
                borne_sup = borne_test;
            }
            borne_test = (borne_inf + borne_sup) / 2;
        }
        System.out.println("Erreur : le noeud " + searched_id + " n'est pas présent dans le modèle");
        return null;
    }

    private ArrayList<Integer> castTabObjectInTabInteger(ArrayList<Object> tab_objet) {
        ArrayList<Integer> tab_integer = new ArrayList<>();
        for (Object objet : tab_objet) {
            tab_integer.add((int) objet);
        }
        return tab_integer;
    }
}
