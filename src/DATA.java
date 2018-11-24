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
    private int nbLigneFichier;


    public DATA() {
        current_line_index = 0;
        nodes_list = new ArrayList<>();
        elements_list = new ArrayList<>();
        parts_list = new ArrayList<>();
    }

    public void setAdresse(String adresse_fichier) {

        ArrayList<String> recup_fichier = this.getStringFromFile(adresse_fichier);
        nbLigneFichier = recup_fichier.size();

        long time = System.currentTimeMillis();
        this.getNodesFromText(recup_fichier);
        System.out.println("Récupération des noeuds : " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();
        this.getElementsFromText(recup_fichier);
        System.out.println(elements_list.size());
        System.out.println("Récupération des éléments : " + (System.currentTimeMillis() - time) + "ms");

        time = System.currentTimeMillis();
        this.getPartsFromText(recup_fichier);
        System.out.println("Récupération des pièces : " + (System.currentTimeMillis() - time) + "ms");
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
        parallelizeNodesImport(recup_fichier, 4);
    }

    private void getElementsFromText(ArrayList<String> recup_fichier) {
        int elem_id;
        ArrayList<Object>[] result;
        ArrayList<Integer> list_node_face;
        PARSER parser = new PARSER("I (int) N [(int) ] */0 [(int) ]", '$');
        System.out.println(countNbElementsInFile(recup_fichier));

        for (int i = current_line_index; recup_fichier.get(i).contentEquals(".MAI") == false; i++) {
            current_line_index = i;
        }
        current_line_index++;
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
                if (result[list].size() != 0) {
                    list_node_face = this.castTabObjectInTabInteger(result[list]);
                    elements_list.get(elements_list.size() - 1).addFace(new FACE(this.recupNodesFromId(list_node_face)));
                }
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
                while (recup_fichier.get(current_line_index).contains(".") == false && recup_fichier.get(current_line_index).contains("RETURN CLOSE") == false && recup_fichier.get(current_line_index).contains("EXIT") == false && current_line_index < recup_fichier.size()) {
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

    public int getCurrentLine() {
        return current_line_index;
    }

    public int getNbLigneFichier() {
        return nbLigneFichier;
    }

    private int countNbNodesInFile(ArrayList<String> recup_fichier) {
        int currentLine, nbNodes = 0;
        // Tant que la ligne ne contient pas ".NOE", on avance
        for (currentLine = 0; recup_fichier.get(currentLine).contentEquals(".NOE") == false; currentLine++) ;
        currentLine++;
        while (recup_fichier.get(currentLine).contains(".MAI") == false) {
            int nbCaracInLine = recup_fichier.get(currentLine).length();
            if (recup_fichier.get(currentLine).contains("!") == true) {
                recup_fichier.remove(currentLine);
            } else if (nbCaracInLine != 0) {
                // cas ligne contient espace au début
                int currentCaracPosition = 0;
                while (recup_fichier.get(currentLine).charAt(currentCaracPosition) == ' ') {
                    if (currentCaracPosition == nbCaracInLine - 1) {
                        break;
                    }
                    currentCaracPosition++;
                }
                if (recup_fichier.get(currentLine).charAt(currentCaracPosition) == 'I') {
                    nbNodes++;
                    currentLine++;
                } else recup_fichier.remove(currentLine);
            } else recup_fichier.remove(currentLine);
        }
        return nbNodes;
    }

    private void parallelizeNodesImport(ArrayList<String> recup_fichier, int nbThread) {
        int nbNodes = countNbNodesInFile(recup_fichier);

        int[] borne = new int[nbThread * 2];
        for (int i = 0; i < nbThread; i++) {
            borne[2 * i] = i * nbNodes / nbThread + 1;
            borne[2 * i + 1] = (i + 1) * nbNodes / nbThread + 1;
        }

        ArrayList<NODE>[] list = new ArrayList[nbThread];
        Thread[] threadTab = new Thread[nbThread];

        for (int numThread = 0; numThread < nbThread; numThread++) {
            list[numThread] = new ArrayList<>(1000);
            threadTab[numThread] = new Thread(new NodeThread(borne[2 * numThread], borne[2 * numThread + 1], recup_fichier, list[numThread]));
            threadTab[numThread].start();
        }

        while (true) {
            int ready = 1;
            for (int numThread = 0; numThread < nbThread; numThread++) {
                if (threadTab[numThread].getState() != Thread.State.TERMINATED) {
                    ready *= 0;
                }
            }
            if (ready == 1) {
                for (int numThread = 0; numThread < nbThread; numThread++) {
                    nodes_list.addAll(list[numThread]);
                }
                break;
            }
        }

    }

    private int countNbElementsInFile(ArrayList<String> recup_fichier) {
        int currentLine, nbElements = 0;
        // Tant que la ligne ne contient pas ".NOE", on avance
        for (currentLine = 0; recup_fichier.get(currentLine).contentEquals(".MAI") == false; currentLine++) ;
        currentLine++;
        while (recup_fichier.get(currentLine).contains(".") == false) {
            int nbCaracInLine = recup_fichier.get(currentLine).length();
            if (recup_fichier.get(currentLine).contains("!") == true) {
                recup_fichier.remove(currentLine);
            } else if (nbCaracInLine != 0) {
                // cas ligne contient espace au début
                int currentCaracPosition = 0;
                while (recup_fichier.get(currentLine).charAt(currentCaracPosition) == ' ') {
                    if (currentCaracPosition == nbCaracInLine - 1) {
                        break;
                    }
                    currentCaracPosition++;
                }
                if (recup_fichier.get(currentLine).charAt(currentCaracPosition) == 'I') {
                    nbElements++;
                }
                currentLine++;
            } else recup_fichier.remove(currentLine);
        }
        return nbElements;
    }

    private class NodeThread implements Runnable {

        private int borneInf;
        private int borneSup;
        private ArrayList<NODE> nodeList;
        private ArrayList<String> recupFichier;

        public NodeThread(int paramBorneInf, int paramBorneSup, ArrayList<String> paramRecupFichier, ArrayList<NODE> paramNodeList) {
            borneInf = paramBorneInf;
            borneSup = paramBorneSup;
            recupFichier = paramRecupFichier;
            nodeList = paramNodeList;
        }

        @Override
        public void run() {
            double[] node_coord = new double[3];
            int node_id;
            PARSER parser = new PARSER(" I (int) X (double) Y (double) Z (double)", '$');
            for (int i = borneInf; i < borneSup; i++) {
                // Utilisation d'un parser pour récupérer les données
                if (parser.parseFromLine(recupFichier.get(i)) == -1) {
                    if (recupFichier.get(i).contains(".") == true) {
                        break;
                    } else continue;

                }
                ArrayList<Object>[] result = parser.getResult();
                node_id = (int) result[0].get(0);
                for (int j = 0; j < 3; j++) {
                    node_coord[j] = (double) result[j + 1].get(0);
                }
                nodeList.add(new NODE(node_id, node_coord, false));
                parser.clearResult();
            }
        }
    }

}
