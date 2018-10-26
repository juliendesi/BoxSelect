import java.util.ArrayList;

public class ELEMENT {

    private int id_elem;
    private ArrayList<FACE> faces_list;

    public ELEMENT(int param_id, FACE param_face) {
        id_elem = param_id;
        faces_list = new ArrayList<FACE>();
        faces_list.add(param_face);
    }

    public ELEMENT(){
        faces_list = new ArrayList<FACE>();
    }

    public void addFace(FACE param_face){
        faces_list.add(param_face);
    }
}
