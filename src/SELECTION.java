
import java.util.ArrayList;

public abstract class SELECTION {

    protected ArrayList<NODE> nodes_list;
    protected ArrayList<FACE> faces_to_treat;
    protected double theta;
    protected double phi;

    public SELECTION() {
        nodes_list = new ArrayList<>();
        theta = 0.0;
        phi = 0.0;
    }

    public ArrayList<NODE> getNodes_list() {
        return nodes_list;
    }

    public void setFaces_to_treat(ArrayList<FACE> list_interface) {
        this.faces_to_treat = list_interface;
    }

    abstract protected void computeIntersection();


}
