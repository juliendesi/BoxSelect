import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class DialogContact extends JDialog {

    String name;
    JFormattedTextField X_centerStruct, Y_centerStruct, ZinfStruct, ZsupStruct, RintStruct, RextStruct;
    JLabel xLabelStruct, yLabelStruct, zinfLabelStruct, zsupLabelStruct, rintLabelStruct, rextLabelStruct;
    JFormattedTextField X_infCpoint, Y_infCpoint, Z_infCpoint, X_supCpoint, Y_supCpoint, Z_supCpoint, RintCpoint, RextCpoint;
    JLabel X_infLabelCpoint, Y_infLabelCpoint, Z_infLabelCpoint, X_supLabelCpoint, Y_supLabelCpoint, Z_supLabelCpoint, rintLabelCpoint, rextLabelCpoint;
    private boolean ok_pressed;
    private JPanel content, panStructParameter, panCpointParameter;
    private JLabel nomLabel, part1Label, part2Label, typeBoiteLabel;
    private JComboBox part1, part2, typeBoite;
    private JTextField nom;
    private ArrayList<String> list_parts_name;
    private CONTROLLER controller;
    private int idContact;

    public DialogContact(JFrame parent, String title, boolean modal, ArrayList<String> list_parts_name, CONTROLLER controller, int idContact) {
        super(parent, title, modal);
        this.ok_pressed = false;
        this.controller = controller;
        this.idContact = idContact;
        this.list_parts_name = list_parts_name;
        this.setSize(550, 340);
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
        panStructParameter = new JPanel();
        panStructParameter.setLayout(new GridLayout(3, 2));
        panStructParameter.setBackground(Color.white);
        panStructParameter.setPreferredSize(new Dimension(440, 100));
        panStructParameter.setBorder(BorderFactory.createTitledBorder("Parameters"));

        X_centerStruct = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        X_centerStruct.setPreferredSize(new Dimension(90, 25));
        Y_centerStruct = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        Y_centerStruct.setPreferredSize(new Dimension(90, 25));
        ZinfStruct = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        ZinfStruct.setPreferredSize(new Dimension(90, 25));
        ZsupStruct = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        ZsupStruct.setPreferredSize(new Dimension(90, 25));
        RintStruct = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        RintStruct.setPreferredSize(new Dimension(90, 25));
        RextStruct = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        RextStruct.setPreferredSize(new Dimension(90, 25));

        xLabelStruct = new JLabel("X : ");
        yLabelStruct = new JLabel("Y : ");
        zinfLabelStruct = new JLabel("Zinf : ");
        zsupLabelStruct = new JLabel("Zsup : ");
        rintLabelStruct = new JLabel("Rayon int : ");
        rextLabelStruct = new JLabel("Rayon ext : ");

        panStructParameter.add(xLabelStruct);
        panStructParameter.add(X_centerStruct);
        panStructParameter.add(yLabelStruct);
        panStructParameter.add(Y_centerStruct);
        panStructParameter.add(zinfLabelStruct);
        panStructParameter.add(ZinfStruct);
        panStructParameter.add(zsupLabelStruct);
        panStructParameter.add(ZsupStruct);
        panStructParameter.add(rintLabelStruct);
        panStructParameter.add(RintStruct);
        panStructParameter.add(rextLabelStruct);
        panStructParameter.add(RextStruct);
        panStructParameter.setVisible(false);

        // Boite cylindre Cpoint
        panCpointParameter = new JPanel();
        panCpointParameter.setLayout(new GridLayout(3, 3));
        panCpointParameter.setBackground(Color.white);
        panCpointParameter.setPreferredSize(new Dimension(440, 100));
        panCpointParameter.setBorder(BorderFactory.createTitledBorder("Parameters"));

        X_infCpoint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        X_infCpoint.setPreferredSize(new Dimension(90, 25));
        Y_infCpoint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        Y_infCpoint.setPreferredSize(new Dimension(90, 25));
        Z_infCpoint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        Z_infCpoint.setPreferredSize(new Dimension(90, 25));
        X_supCpoint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        X_supCpoint.setPreferredSize(new Dimension(90, 25));
        Y_supCpoint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        Y_supCpoint.setPreferredSize(new Dimension(90, 25));
        Z_supCpoint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        Z_supCpoint.setPreferredSize(new Dimension(90, 25));
        RintCpoint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        RintCpoint.setPreferredSize(new Dimension(90, 25));
        RextCpoint = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.US));
        RextCpoint.setPreferredSize(new Dimension(90, 25));

        X_infLabelCpoint = new JLabel("X inf : ");
        Y_infLabelCpoint = new JLabel("Y inf : ");
        Z_infLabelCpoint = new JLabel("Z inf : ");
        X_supLabelCpoint = new JLabel("X sup : ");
        Y_supLabelCpoint = new JLabel("Y sup : ");
        Z_supLabelCpoint = new JLabel("Z sup : ");
        rintLabelCpoint = new JLabel("Rayon int : ");
        rextLabelCpoint = new JLabel("Rayon ext : ");

        panCpointParameter.add(X_infLabelCpoint);
        panCpointParameter.add(X_infCpoint);
        panCpointParameter.add(Y_infLabelCpoint);
        panCpointParameter.add(Y_infCpoint);
        panCpointParameter.add(Z_infLabelCpoint);
        panCpointParameter.add(Z_infCpoint);
        panCpointParameter.add(X_supLabelCpoint);
        panCpointParameter.add(X_supCpoint);
        panCpointParameter.add(Y_supLabelCpoint);
        panCpointParameter.add(Y_supCpoint);
        panCpointParameter.add(Z_supLabelCpoint);
        panCpointParameter.add(Z_supCpoint);
        panCpointParameter.add(rintLabelCpoint);
        panCpointParameter.add(RintCpoint);
        panCpointParameter.add(rextLabelCpoint);
        panCpointParameter.add(RextCpoint);
        panCpointParameter.setVisible(false);
        
        // Ajout dans le contenu
        content = new JPanel();
        content.setBackground(Color.white);
        content.add(panNom);
        content.add(panPart1);
        content.add(panPart2);
        content.add(panTypeBoite);
        content.add(panStructParameter);
        content.add(panCpointParameter);
        //content.add(panTaille);
        //content.add(panCheveux);

        JPanel control = new JPanel();

        JButton okBouton = new JButton("OK");
        okBouton.addActionListener(new OKListener());

        JButton cancelBouton = new JButton("Annuler");
        cancelBouton.addActionListener(new CancelListener());

        part1.addItemListener(new ItemState());
        part1.addActionListener(new Part1Listener());

        part2.addItemListener(new ItemState());
        part2.addActionListener(new Part2Listener());

        typeBoite.addItemListener(new ItemState());
        typeBoite.addActionListener(new TypeBoiteListener());

        X_centerStruct.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        X_centerStruct.addKeyListener(new StructParameterListener());
        X_centerStruct.addMouseListener(new StructParameterListener());

        Y_centerStruct.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        Y_centerStruct.addKeyListener(new StructParameterListener());
        Y_centerStruct.addMouseListener(new StructParameterListener());

        ZinfStruct.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        ZinfStruct.addKeyListener(new StructParameterListener());
        ZinfStruct.addMouseListener(new StructParameterListener());

        ZsupStruct.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        ZsupStruct.addKeyListener(new StructParameterListener());
        ZsupStruct.addMouseListener(new StructParameterListener());

        RintStruct.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        RintStruct.addKeyListener(new StructParameterListener());
        RintStruct.addMouseListener(new StructParameterListener());

        RextStruct.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        RextStruct.addKeyListener(new StructParameterListener());
        RextStruct.addMouseListener(new StructParameterListener());


        X_infCpoint.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        X_infCpoint.addKeyListener(new CpointParameterListener());
        X_infCpoint.addMouseListener(new CpointParameterListener());

        Y_infCpoint.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        Y_infCpoint.addKeyListener(new CpointParameterListener());
        Y_infCpoint.addMouseListener(new CpointParameterListener());

        Z_infCpoint.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        Z_infCpoint.addKeyListener(new CpointParameterListener());
        Z_infCpoint.addMouseListener(new CpointParameterListener());

        X_supCpoint.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        X_supCpoint.addKeyListener(new CpointParameterListener());
        X_supCpoint.addMouseListener(new CpointParameterListener());

        Y_supCpoint.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        Y_supCpoint.addKeyListener(new CpointParameterListener());
        Y_supCpoint.addMouseListener(new CpointParameterListener());

        Z_supCpoint.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        Z_supCpoint.addKeyListener(new CpointParameterListener());
        Z_supCpoint.addMouseListener(new CpointParameterListener());

        RintCpoint.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        RintCpoint.addKeyListener(new CpointParameterListener());
        RintCpoint.addMouseListener(new CpointParameterListener());

        RextCpoint.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        RextCpoint.addKeyListener(new CpointParameterListener());
        RextCpoint.addMouseListener(new CpointParameterListener());

        control.add(okBouton);
        control.add(cancelBouton);

        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

    public ArrayList<Double> getCylindreStructValues() {
        ArrayList<Double> list_parameters = new ArrayList<>();
        list_parameters.add(((Number) X_centerStruct.getValue()).doubleValue());
        list_parameters.add(((Number) Y_centerStruct.getValue()).doubleValue());
        list_parameters.add(((Number) ZinfStruct.getValue()).doubleValue());
        list_parameters.add(((Number) ZsupStruct.getValue()).doubleValue());
        list_parameters.add(((Number) RintStruct.getValue()).doubleValue());
        list_parameters.add(((Number) RextStruct.getValue()).doubleValue());
        return list_parameters;
    }

    public ArrayList<Double> getCylindreCpointValues() {
        ArrayList<Double> list_parameters = new ArrayList<>();
        list_parameters.add(((Number) X_infCpoint.getValue()).doubleValue());
        list_parameters.add(((Number) Y_infCpoint.getValue()).doubleValue());
        list_parameters.add(((Number) Z_infCpoint.getValue()).doubleValue());
        list_parameters.add(((Number) X_supCpoint.getValue()).doubleValue());
        list_parameters.add(((Number) Y_supCpoint.getValue()).doubleValue());
        list_parameters.add(((Number) Z_supCpoint.getValue()).doubleValue());
        list_parameters.add(((Number) RintCpoint.getValue()).doubleValue());
        list_parameters.add(((Number) RextCpoint.getValue()).doubleValue());
        return list_parameters;
    }

    public boolean getOK() {
        return this.ok_pressed;
    }

    public String getLastName() {
        return this.name;
    }

    public class Part1Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.setPart1(idContact, part1.getSelectedIndex());
        }
    }

    public class Part2Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.setPart2(idContact, part2.getSelectedIndex());
        }
    }

    public class ItemState implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
        }
    }

    public class TypeBoiteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Double> initial_value;
            switch (typeBoite.getSelectedItem().toString()) {
                case "CYLINDRE STRUCT":
                    controller.setType(idContact, typeBoite.getSelectedItem().toString());
                    DialogContact.this.setSize(550, 500);
                    panCpointParameter.setVisible(false);
                    panStructParameter.setVisible(true);
                    initial_value = controller.getBoxInitialParameters(idContact);
                    X_centerStruct.setValue(initial_value.get(0));
                    Y_centerStruct.setValue(initial_value.get(1));
                    ZinfStruct.setValue(initial_value.get(2));
                    ZsupStruct.setValue(initial_value.get(3));
                    RintStruct.setValue(initial_value.get(4));
                    RextStruct.setValue(initial_value.get(5));
                    break;

                case "CYLINDRE CPOINT":
                    controller.setType(idContact, typeBoite.getSelectedItem().toString());
                    DialogContact.this.setSize(550, 500);
                    panStructParameter.setVisible(false);
                    panCpointParameter.setVisible(true);
                    initial_value = controller.getBoxInitialParameters(idContact);
                    X_infCpoint.setValue(initial_value.get(0));
                    Y_infCpoint.setValue(initial_value.get(1));
                    Z_infCpoint.setValue(initial_value.get(2));
                    X_supCpoint.setValue(initial_value.get(3));
                    Y_supCpoint.setValue(initial_value.get(4));
                    Z_supCpoint.setValue(initial_value.get(5));
                    RintCpoint.setValue(initial_value.get(6));
                    RextCpoint.setValue(initial_value.get(7));

                    break;

                case "":
                    DialogContact.this.setSize(550, 340);
                    panStructParameter.setVisible(false);
                    panCpointParameter.setVisible(false);
                    break;
            }
        }
    }

    public class OKListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ok_pressed = true;
            name = nom.getText();
            DialogContact.this.setVisible(false);
        }
    }

    public class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controller.removeContact(idContact);
            DialogContact.this.setVisible(false);
        }
    }

    public class StructParameterListener implements MouseListener, KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER) {
                // on récupère toutes les valeurs des champs et on les envoie au controller
                controller.sendParametersToContact(idContact, DialogContact.this.getCylindreStructValues());
                controller.getView().addNodeIntersection(idContact);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // on récupère toutes les valeurs des champs et on les envoie au controller
            controller.sendParametersToContact(idContact, DialogContact.this.getCylindreStructValues());
            controller.getView().addNodeIntersection(idContact);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    public class CpointParameterListener implements MouseListener, KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER) {
                // on récupère toutes les valeurs des champs et on les envoie au controller
                controller.sendParametersToContact(idContact, DialogContact.this.getCylindreCpointValues());
                controller.getView().addNodeIntersection(idContact);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // on récupère toutes les valeurs des champs et on les envoie au controller
            controller.sendParametersToContact(idContact, DialogContact.this.getCylindreCpointValues());
            controller.getView().addNodeIntersection(idContact);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    //TODO Ajouter checkbox ORIENT avec champ TOL
    //todo Ajouter option PEAU 0
}
