import java.util.ArrayList;

public class PART {

    private String part_name;
    private ArrayList<ELEMENT> elements_list;

    public PART(String param_name) {
        part_name = param_name;
        elements_list = new ArrayList<>();
    }

    public void addElement(ELEMENT param_elem) {
        elements_list.add(param_elem);
    }
}
