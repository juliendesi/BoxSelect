public class MAIN {
    public static void main(String[] args){
        /*MODEL modele = new MODEL();
        modele.importData("/Users/Julien/IdeaProjects/maillage_V11.dat");

        modele.createContact();
        modele.getContact(0).setType("CYLINDRE STRUCT");
        modele.getContact(0).setPart1(modele.getPart(3));
        modele.getContact(0).setPart2(modele.getPart(4));
        ((CYLIND_STRUCT) modele.getContact(0).getSelection()).setZ_inf(10.2);
        ((CYLIND_STRUCT) modele.getContact(0).getSelection()).setZ_sup(20.2);
        ((CYLIND_STRUCT) modele.getContact(0).getSelection()).setCenter(0, 0.0);
        ((CYLIND_STRUCT) modele.getContact(0).getSelection()).setCenter(1, 0.0);
        ((CYLIND_STRUCT) modele.getContact(0).getSelection()).setRayon_ext(3.0);*/

        CONTROLLER controller = new CONTROLLER();
        MODEL model = new MODEL(controller);
        //model.importData("/Users/Julien/IdeaProjects/maillage_V11.dat");
        FENETRE fenetre = new FENETRE(controller);

        controller.setModel(model);
        controller.setView(fenetre);

    }
}
