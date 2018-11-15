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

    public int createContact() {
        return this.model.createContact();
    }

    public void removeContact(int idContact) {
        this.model.removeContact(idContact);
    }

    public void setPart1(int idContact, int indexPart1) {
        this.model.getContact(idContact).setPart1(this.model.getPart(indexPart1));
    }

    public void setPart2(int idContact, int indexPart2) {
        this.model.getContact(idContact).setPart2(this.model.getPart(indexPart2));
    }

    public void setType(int idContact, String type) {
        this.model.getContact(idContact).setType(type);
    }

    public ArrayList<Double> getBoxInitialParameters(int idContact) {
        return this.model.getContact(idContact).getParameters();
    }

    public void setParameters(int idContact, ArrayList<Double> list_parameters) {
        this.model.getContact(idContact).setParameters(list_parameters);
    }

    public GraphicTreatment setGraphicTreatment() {
        GraphicTreatment modelGraphic = new GraphicTreatment(model);
        return modelGraphic;
    }


}
