import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DialogContact extends JDialog {

    private boolean sendData;
    private JPanel content;
    private JLabel nomLabel, part1Label, part2Label, typeBoiteLabel;
    private JComboBox part1, part2, typeBoite;
    private JTextField nom;
    private ArrayList<String> list_parts_name;
    private CONTROLLER controller;

    public DialogContact(JFrame parent, String title, boolean modal, ArrayList<String> list_parts_name, CONTROLLER controller) {
        super(parent, title, modal);
        this.controller = controller;
        this.list_parts_name = list_parts_name;
        this.setSize(550, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.initComponent();
    }

    /*public ZDialogInfo showZDialog(){
        this.sendData = false;
        this.setVisible(true);
        return this.zInfo;
    }*/

    private void initComponent() {
        //Icône
        /*icon = new JLabel(new ImageIcon("images/icone.jpg"));
        JPanel panIcon = new JPanel();
        panIcon.setBackground(Color.white);
        panIcon.setLayout(new BorderLayout());
        panIcon.add(icon);*/

        //Le nom
        JPanel panNom = new JPanel();
        panNom.setBackground(Color.white);
        panNom.setPreferredSize(new Dimension(440, 60));
        nom = new JTextField();
        nom.setPreferredSize(new Dimension(100, 25));
        panNom.setBorder(BorderFactory.createTitledBorder("Nom du contact"));
        nomLabel = new JLabel("Saisir un nom :");
        panNom.add(nomLabel);
        panNom.add(nom);

        // Pièce 1
        JPanel panPart1 = new JPanel();
        panPart1.setBackground(Color.white);
        panPart1.setPreferredSize(new Dimension(440, 60));
        panPart1.setBorder(BorderFactory.createTitledBorder("Part 1"));
        part1 = new JComboBox();
        for (int i = 0; i < list_parts_name.size(); i++) {
            part1.addItem(list_parts_name.get(i));
        }
        part1Label = new JLabel("Name : ");
        panPart1.add(part1Label);
        panPart1.add(part1);

        // Pièce 2
        JPanel panPart2 = new JPanel();
        panPart2.setBackground(Color.white);
        panPart2.setPreferredSize(new Dimension(440, 60));
        panPart2.setBorder(BorderFactory.createTitledBorder("Part 2"));
        part2 = new JComboBox();
        for (int i = 0; i < list_parts_name.size(); i++) {
            part2.addItem(list_parts_name.get(i));
        }
        part2Label = new JLabel("Name : ");
        panPart2.add(part2Label);
        panPart2.add(part2);

        // Type boite
        JPanel panTypeBoite = new JPanel();
        panTypeBoite.setBackground(Color.white);
        panTypeBoite.setPreferredSize(new Dimension(440, 60));
        panTypeBoite.setBorder(BorderFactory.createTitledBorder("Type boite"));
        typeBoite = new JComboBox(TypeBoite.values());
        typeBoiteLabel = new JLabel("Type : ");
        panTypeBoite.add(typeBoiteLabel);
        panTypeBoite.add(typeBoite);

        // Boite cylindre struct


        // Ajout dans le contenu
        content = new JPanel();
        content.setBackground(Color.white);
        content.add(panNom);
        content.add(panPart1);
        content.add(panPart2);
        content.add(panTypeBoite);
        //content.add(panTaille);
        //content.add(panCheveux);

        JPanel control = new JPanel();

        JButton okBouton = new JButton("OK");
        /*okBouton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                zInfo = new ZDialogInfo(nom.getText(), (String)sexe.getSelectedItem(), getAge(), (String)cheveux.getSelectedItem() ,getTaille());
                setVisible(false);
            }


            public String getTaille(){
                return (taille.getText().equals("")) ? "180" : taille.getText();
            }
        });*/

        JButton cancelBouton = new JButton("Annuler");
        cancelBouton.addActionListener(new CancelListener());

        typeBoite.addItemListener(new ItemState());
        typeBoite.addActionListener(new TypeBoiteListener());

        control.add(okBouton);
        control.add(cancelBouton);

        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

    public class ItemState implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
        }
    }

    public class TypeBoiteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(typeBoite.getSelectedItem());
            switch (typeBoite.getSelectedItem().toString()) {
                case "CYLINDRE STRUCT":
                    DialogContact.this.setSize(550, 500);
                    JFormattedTextField X_center, Y_center, Zinf, Zsup, Rint, Rext;
                    JLabel xLabel, yLabel, zinfLabel, zsupLabel, rintLabel, rextLabel;

                    JPanel panParameter = new JPanel();
                    panParameter.setLayout(new GridLayout(3, 2));
                    panParameter.setBackground(Color.white);
                    panParameter.setPreferredSize(new Dimension(440, 150));
                    panParameter.setBorder(BorderFactory.createTitledBorder("Parameters"));

                    X_center = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
                    X_center.setPreferredSize(new Dimension(90, 25));
                    Y_center = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
                    Y_center.setPreferredSize(new Dimension(90, 25));
                    Zinf = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
                    Zinf.setPreferredSize(new Dimension(90, 25));
                    Zsup = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
                    Zsup.setPreferredSize(new Dimension(90, 25));
                    Rint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
                    Rint.setPreferredSize(new Dimension(90, 25));
                    Rext = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
                    Rext.setPreferredSize(new Dimension(90, 25));


                    xLabel = new JLabel("X : ");
                    yLabel = new JLabel("Y : ");
                    zinfLabel = new JLabel("Zinf : ");
                    zsupLabel = new JLabel("Zsup : ");
                    rintLabel = new JLabel("Rayon int : ");
                    rextLabel = new JLabel("Rayon ext : ");

                    panParameter.add(xLabel);
                    panParameter.add(X_center);
                    panParameter.add(yLabel);
                    panParameter.add(Y_center);
                    panParameter.add(zinfLabel);
                    panParameter.add(Zinf);
                    panParameter.add(zsupLabel);
                    panParameter.add(Zsup);
                    panParameter.add(rintLabel);
                    panParameter.add(Rint);
                    panParameter.add(rextLabel);
                    panParameter.add(Rext);

                    content.add(panParameter);
                    DialogContact.this.getContentPane().revalidate();
                    break;
                case "CYLINDRE CPOINT":

                    break;
            }
        }
    }

    public class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DialogContact.this.setVisible(false);
        }
    }
}
