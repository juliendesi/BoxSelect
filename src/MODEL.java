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
    }

}
