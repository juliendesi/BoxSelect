import java.util.ArrayList;

public class MODEL {
    private DATA data;
    private GraphicTreatment graphicTreatment;
    private ArrayList<String> parts_list_name;
    private ArrayList<PART> parts_list;
    private ArrayList<CONTACT> contacts_list;
    private CONTROLLER controller;

    public MODEL(CONTROLLER controller) {
        parts_list = new ArrayList<>();
        parts_list_name = new ArrayList<>();
        contacts_list = new ArrayList<>();
        this.controller = controller;
    }

    public void importData(String adresse) {
        data = new DATA(adresse);
        parts_list = data.getParts_list();
        this.finalizeParts();
        System.out.println("File import done");
    }

    private void finalizeParts() {
        for (PART aParts_list : parts_list) {
            aParts_list.finalizePart();
        }
        this.setParts_list_name();
    }

    public PART getPart(int id_part) {
        return parts_list.get(id_part);
    }

    public int numberOfParts() {
        return parts_list.size();
    }

    public ArrayList<String> getParts_list_name() {
        return parts_list_name;
    }

    public void setParts_list_name() {
        for (PART part : parts_list) {
            parts_list_name.add(part.getPart_name());
        }
    }

    public int createContact() {
        contacts_list.add(new CONTACT());
        return contacts_list.size() - 1;
    }

    public CONTACT getContact(int id_contact) {
        return contacts_list.get(id_contact);
    }

    public void removeContact(int id_contact) {
        contacts_list.remove(id_contact);
    }
}
