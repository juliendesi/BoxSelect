import javax.swing.*;

public class MAIN {
    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                CONTROLLER controller = new CONTROLLER();
                MODEL model = new MODEL(controller);
                model.importData("/Users/Julien/IdeaProjects/maillage_V11.dat");
                //FENETRE fenetre = new FENETRE(controller);
                //controller.setModel(model);
                //controller.setView(fenetre);
            }
        });


    }
}
