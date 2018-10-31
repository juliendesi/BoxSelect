import java.util.ArrayList;

public class CONTROLLER {

    private MODEL model;
    private FENETRE view;

    public CONTROLLER() {
        this.model = model;
        this.view = view;
    }

    public void setModel(MODEL model) {
        this.model = model;
    }

    public void setView(FENETRE view) {
        this.view = view;
    }

    public void importData(String adresse) {
        model.importData(adresse);
    }

    public ArrayList<String> getPartsListName() {
        return this.model.getParts_list_name();
    }

    public void createContact() {
        this.model.createContact();
    }


}
