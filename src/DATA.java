import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DATA {
    private ArrayList<NODE> nodes_list;
    private ArrayList<ELEMENT> elements_list;
    private ArrayList<PART> parts_list;
    private int current_line_index;


    public DATA(String adresse_fichier){
        nodes_list = new ArrayList<>();
        elements_list = new ArrayList<>();
        parts_list = new ArrayList<>();
        long time = System.currentTimeMillis();
        ArrayList<String> recup_fichier = this.getStringFromFile(adresse_fichier);

        this.getNodesFromText(recup_fichier);
        System.out.println("Récupération des noeuds : " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();
        this.getElementsFromText(recup_fichier);
        System.out.println("Récupération des éléments : " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();
        this.getPartsFromText(recup_fichier);
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
            elements_list.add(new ELEMENT(elem_id));

            for (int list = 1; list < result.length; list++) {
                list_node_face = this.castTabObjectInTabInteger(result[list]);
                elements_list.get(elements_list.size() - 1).addFace(new FACE(this.recupNodesFromId(list_node_face)));
            }
            parser.clearResult();
        }
    }

    private void getPartsFromText(ArrayList<String> recup_fichier) {
        PARSER parser_nom = new PARSER(" GROUP /5 NOM \"(String)\" MAILLE", '$');
        PARSER parser_elem = new PARSER(" I (int) J (int)", '$');

        // 1 : tester jusqu'à ".SEL"
        while (current_line_index < recup_fichier.size()) {

            for (int i = current_line_index; recup_fichier.get(i).contains(".SEL") == false; i++) {
                current_line_index = i;
                if (current_line_index == recup_fichier.size()) {
                    System.out.println("Erreur : Pas de selection de groupe définie");
                    return;
                }
            }
            current_line_index++;
            // 3 : tester jusqu'à "GROUP"
            for (int i = current_line_index; recup_fichier.get(i).contains("GROUP") == false; i++) {
                current_line_index = i;
                if (current_line_index == recup_fichier.size()) {
                    System.out.println("Erreur : Pas de groupe définie");
                    return;
                }
            }
            current_line_index++;
            // 3.1 : tester la même ligne avec "MAILLE"
            if (recup_fichier.get(current_line_index).contains("MAILLE") == true) {
                // 3.1.1 : si ok
                // 3.1.1.1 : on recupère le nom de la piece
                while (recup_fichier.get(current_line_index).contains(".") == false && recup_fichier.get(current_line_index).contains("RETURN CLOSE") == false && current_line_index < recup_fichier.size()) {
                    if (recup_fichier.get(current_line_index).contains("!") == true) {
                        current_line_index++;
                    } else {
                        ArrayList<Integer> id_elem_list = new ArrayList<>();
                        if (parser_nom.parseFromLine(recup_fichier.get(current_line_index)) > 0) {
                            String name = (String) parser_nom.getResult()[0].get(0);
                            parts_list.add(new PART(name));
                            parser_nom.clearResult();

                            current_line_index++;
                            parser_elem.setDebut_texte(current_line_index);
                            while (parser_elem.parseFromText(recup_fichier) > 0) {
                                id_elem_list.addAll(this.analysePartData(parser_elem));
                                parser_elem.clearResult();
                                current_line_index++;
                                parser_elem.setDebut_texte(current_line_index);
                            }
                            this.addPartElemFromNodeId(id_elem_list, parts_list.get(parts_list.size() - 1));
                        } else current_line_index++;
                    }
                }
                return;
            }
            // 3.2 : si nok on revient au 1
        }
    }
    
    private ArrayList<NODE> recupNodesFromId(ArrayList<Integer> param_nodes_list){
        ArrayList<NODE> final_nodes_list = new ArrayList<>();

        for (int id : param_nodes_list) {
            final_nodes_list.add(nodes_list.get(searchReferenceFromID(id, nodes_list)));
        }

        return final_nodes_list;
    }

    private int searchReferenceFromID(int searched_id, List<? extends REFERENCE> objet) {
        int borne_test, borne_inf, borne_sup;
        int id_test, id_inf, id_sup;

        // Initialisation
        borne_inf = 0;
        borne_sup = objet.size() - 1;

        id_inf = objet.get(borne_inf).getId();
        if (id_inf == searched_id) {
            return borne_inf;
        }

        id_sup = objet.get(borne_sup).getId();
        if (id_sup == searched_id) {
            return borne_sup;
        }

        borne_test = (borne_inf + borne_sup) / 2;
        while (borne_inf != borne_test) {
            // test de la valeur milieu
            // si valeur cherchée > choisir milieu de la partie sup
            id_test = objet.get(borne_test).getId();
            if (id_test == searched_id) {
                return borne_test;
            }

            if (searched_id > id_test) {
                borne_inf = borne_test;
            } else {
                borne_sup = borne_test;
            }
            borne_test = (borne_inf + borne_sup) / 2;
        }
        System.out.println("Erreur : le noeud " + searched_id + " n'est pas présent dans le modèle");
        return -1;
    }

    private ArrayList<Integer> castTabObjectInTabInteger(ArrayList<Object> tab_objet) {
        ArrayList<Integer> tab_integer = new ArrayList<>();
        for (Object objet : tab_objet) {
            tab_integer.add((int) objet);
        }
        return tab_integer;
    }

    private ArrayList<Integer> analysePartData(PARSER parser) {
        if (parser.getResult()[1].size() == 0) {
            return castTabObjectInTabInteger(parser.getResult()[0]);
        } else {
            ArrayList<Integer> id_elem_list;
            int elem_j, last_elem_i;
            elem_j = (int) parser.getResult()[1].get(0);
            last_elem_i = (int) parser.getResult()[0].get(parser.getResult()[0].size() - 1);
            id_elem_list = castTabObjectInTabInteger(parser.getResult()[0]);
            for (int i = last_elem_i + 1; i < elem_j + 1; i++) {
                id_elem_list.add(i);
            }
            return id_elem_list;
        }
    }

    private void addPartElemFromNodeId(ArrayList<Integer> id_elem_parser, PART part) {
        Collections.sort(elements_list);
        int index_first_elem = this.searchReferenceFromID(id_elem_parser.get(0), elements_list);

        part.addElement(elements_list.get(index_first_elem));

        for (int index_elem_parser = 1; index_elem_parser < id_elem_parser.size(); index_elem_parser++)
            for (int id_elem = index_first_elem; id_elem < elements_list.size(); id_elem++)
                if (id_elem_parser.get(index_elem_parser) == elements_list.get(id_elem).getId()) {
                    part.addElement(elements_list.get(id_elem));
                    index_first_elem = id_elem;
                    break;
                }
    }

    public ArrayList<PART> getParts_list() {
        return parts_list;
    }



}
