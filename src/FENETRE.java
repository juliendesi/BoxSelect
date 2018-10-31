import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FENETRE extends JFrame {
    private CONTROLLER controller;
    private JTree tree_model;
    private JButton add_contact_button;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton import_button;
    private JEditorPane editorPane1;

    public FENETRE(CONTROLLER controller) {
        this.controller = controller;
        this.setTitle("SAMCEF BoxCreator");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add_contact_button = new JButton("Add contact");
        import_button = new JButton("Import File");
        tree_model = new JTree();

        this.setLayout(new BorderLayout());

        //Instanciation d'un objet JPanel
        JPanel graphique = new JPanel();
        JPanel menu = new JPanel();
        JPanel tree = new JPanel();

        //Définition de sa couleur de fond
        graphique.setBackground(Color.BLACK);

        this.getContentPane().add(menu, BorderLayout.NORTH);
        menu.add(import_button);
        menu.add(add_contact_button);

        this.getContentPane().add(graphique, BorderLayout.CENTER);

        this.getContentPane().add(tree, BorderLayout.WEST);
        tree.add(tree_model);

        import_button.addActionListener(e -> {
            // création de la boîte de dialogue
            JFileChooser dialogue = new JFileChooser();

            // affichage
            dialogue.showOpenDialog(null);

            // récupération du fichier sélectionné
            System.out.println("Fichier choisi : " + dialogue.getSelectedFile().getAbsolutePath());
            controller.importData(dialogue.getSelectedFile().getAbsolutePath());
        });

        add_contact_button.addActionListener(e -> {
            ArrayList<String> list_parts_name;
            list_parts_name = controller.getPartsListName();
            controller.createContact();
            DialogContact dialog = new DialogContact(null, "Contact creation", true, list_parts_name, controller);
            dialog.setVisible(true);
        });

        this.setVisible(true);
    }


    /*class ContactListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> list_parts_name;
            list_parts_name = controller.getModel().getParts_list_name();
            DialogContact dialog = new DialogContact(null, "Contact creation",true, list_parts_name);
        }
    }*/
}
