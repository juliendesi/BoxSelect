import java.util.ArrayList;

public class FACE {

    private ArrayList<NODE> nodes_list;
    private ArrayList<ELEMENT> attached_elements;
    private boolean is_interface;

    public FACE(ArrayList<NODE> param_nodes_list){
        nodes_list = param_nodes_list;
        attached_elements = new ArrayList<>();
        is_interface = false;
        this.attachToNodes();
    }

    private void attachToNodes() {
        for (NODE node : nodes_list) {
            node.addAttachedFaces(this);
        }
    }

    public void addAttachedElement(ELEMENT element) {
        attached_elements.add(element);
    }

    public ArrayList<ELEMENT> getAttached_elements() {
        return attached_elements;
    }
}
