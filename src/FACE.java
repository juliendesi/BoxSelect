import java.util.ArrayList;

public class FACE extends REFERENCE {

    private ArrayList<NODE> nodes_list;
    private ArrayList<ELEMENT> attached_elements;
    private boolean is_interface;

    public FACE(ArrayList<NODE> param_nodes_list){
        nodes_list = param_nodes_list;
        attached_elements = new ArrayList<>();
        is_interface = true;
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

    public ArrayList<NODE> getNodes_list() {
        return nodes_list;
    }

    public boolean contains(NODE node) {
        for (NODE node_of_face : nodes_list) {
            if (node_of_face.getId() == node.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean getInterface() {
        return is_interface;
    }

    public void setInterface(boolean is_interface) {
        this.is_interface = is_interface;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((FACE) obj).getId();
    }

    public int getNbNode() {
        return nodes_list.size();
    }

    @Override
    public String toString() {
        String texte = "";
        for (int i = 0; i < nodes_list.size(); i++) {
            texte = texte + nodes_list.get(i).getId() + " ";
        }
        texte = texte + "\n";
        return texte;
    }
}


