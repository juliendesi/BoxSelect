import java.util.ArrayList;

public class MODEL {
    private DATA data;
    private ArrayList<PART> parts_list;
    private ArrayList<CONTACT> contacts_list;

    public MODEL() {
        parts_list = new ArrayList<>();
        contacts_list = new ArrayList<>();
    }

    public void importData(String adresse) {
        data = new DATA(adresse);
        parts_list = data.getParts_list();
        this.finalizeParts();
    }

    private void finalizeParts() {
        for (int i = 0; i < parts_list.size(); i++) {
            parts_list.get(i).finalizePart();
        }
    }

    public PART getPart(int id_part) {
        return parts_list.get(id_part);
    }

    public void createContact() {
        contacts_list.add(new CONTACT());
    }

    public CONTACT getContact(int id_contact) {
        return contacts_list.get(id_contact);
    }

    public void removeContact(int id_contact) {
        contacts_list.remove(id_contact);
    }
}
