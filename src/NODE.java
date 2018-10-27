import java.util.ArrayList;

public class NODE extends REFERENCE {

    //private int node_id;
    private double[] node_coord;
    private ArrayList<FACE> attached_faces;
    private boolean highlight;

    public NODE(){
        this.id = 0;
        node_coord = new double[3];
        highlight = false;
    }

    public NODE(int param_id, double[] param_coord, boolean param_highlight){
        this.id = param_id;
        node_coord = param_coord;
        highlight = param_highlight;
        attached_faces = new ArrayList<>();
    }

    public void addAttachedFaces(FACE attached_face) {
        attached_faces.add(attached_face);
    }

    public ArrayList<FACE> getAttached_faces() {
        return attached_faces;
    }
}
