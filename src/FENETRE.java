import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FENETRE extends JFrame {
    private CONTROLLER controller;
    private GraphicTreatment moduleGraphic;
    private InterfaceVue vue;
    private JTree tree;
    private DefaultTreeModel tree_model;
    private DefaultMutableTreeNode racine;
    private JButton add_contact_button;
    private JButton import_button;
    private ArrayList<Integer> id_contact_list;

    public FENETRE(CONTROLLER controller) {
        this.controller = controller;
        id_contact_list = new ArrayList<>();
        this.setTitle("SAMCEF BoxCreator");
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        //Instanciation d'un objet JPanel
        JPanel contenu_graphique = new JPanel();
        final JFXPanel graphique = new JFXPanel();

        JPanel menu = new JPanel();
        add_contact_button = new JButton("Add contact");
        import_button = new JButton("Import File");
        menu.add(import_button);
        menu.add(add_contact_button);

        //Définition de sa couleur de fond
        graphique.setBackground(Color.BLACK);
        contenu_graphique.add(graphique);

        this.getContentPane().add(menu, BorderLayout.NORTH);
        this.getContentPane().add(contenu_graphique, BorderLayout.CENTER);
        this.buildTree();

        import_button.addActionListener(new AddImportListener());
        add_contact_button.addActionListener(new AddContactListener());

        this.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(graphique);
            }
        });
    }

    private void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        vue = new InterfaceVue();
        vue.createEmptyScene(900, 700);
        //vue.addPyramid();
        fxPanel.setScene(vue.getScene());
    }

    private void buildTree() {
        //Création d'une racine
        racine = new DefaultMutableTreeNode("Modele");
        DefaultMutableTreeNode parts = new DefaultMutableTreeNode("Parts");
        DefaultMutableTreeNode contacts = new DefaultMutableTreeNode("Contacts");

        racine.add(parts);
        racine.add(contacts);
        //Nous allons ajouter des branches et des feuilles à notre racine

        tree_model = new DefaultTreeModel(this.racine);
        //Nous créons, avec notre hiérarchie, un arbre
        tree = new JTree();
        tree.setModel(tree_model);
        tree.setEditable(true);

        //Que nous plaçons sur le ContentPane de notre JFrame à l'aide d'un scroll
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        this.getContentPane().add(scrollPane, BorderLayout.WEST);
    }

    public void addContactToTree(String name) {
        DefaultMutableTreeNode newContact = new DefaultMutableTreeNode(name);
        ((DefaultMutableTreeNode) racine.getChildAt(1)).add(newContact);
        tree_model.insertNodeInto(newContact, (DefaultMutableTreeNode) racine.getChildAt(1), racine.getChildAt(1).getChildCount() - 1);
    }

    public void removeLastContactFromTree() {
        int last_indice = racine.getChildAt(1).getChildCount() - 1;
        ((DefaultMutableTreeNode) racine.getChildAt(1)).remove(last_indice);
    }

    public void addPartToTree(String name) {
        DefaultMutableTreeNode newContact = new DefaultMutableTreeNode(name);
        ((DefaultMutableTreeNode) racine.getChildAt(0)).add(newContact);
        tree_model.insertNodeInto(newContact, (DefaultMutableTreeNode) racine.getChildAt(0), racine.getChildAt(0).getChildCount() - 1);
    }

    public class AddContactListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> list_parts_name;
            list_parts_name = controller.getPartsListName();
            int id_contact = controller.createContact();
            id_contact_list.add(id_contact);
            FENETRE.this.addContactToTree("Contact " + (id_contact + 1));
            DialogContact dialog = new DialogContact(null, "Contact creation", true, list_parts_name, controller, id_contact);
            dialog.setVisible(true);
            // TODO prévoir arraylist de DialogContact. Chaque indice contient les données d'un contact crée.
            // Moyen facile pour identifier une modification d'un contact.
        }
    }

    public class AddImportListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> name_list;
            // création de la boîte de dialogue
            JFileChooser dialogue = new JFileChooser();

            // affichage
            dialogue.showOpenDialog(null);

            // récupération du fichier sélectionné
            System.out.println("Fichier choisi : " + dialogue.getSelectedFile().getAbsolutePath());
            controller.importData(dialogue.getSelectedFile().getAbsolutePath());
            moduleGraphic = controller.setGraphicTreatment();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < controller.getNumberOfPart(); i++) {
                        vue.addPart(moduleGraphic.getIndicesFacesByPart(i), moduleGraphic.getVerticesCoordByPart(i));
                    }
                    System.out.println("Pieces affichées");
                }
            });

            name_list = controller.getPartsListName();
            for (String name : name_list) {
                FENETRE.this.addPartToTree(name);
            }
        }
    }
}
