import java.util.ArrayList;

public class ELEMENT extends REFERENCE {

    //private int id_elem;
    private ArrayList<FACE> faces_list;

    public ELEMENT(int param_id) {
        this.id = param_id;
        faces_list = new ArrayList<FACE>();
    }

    public ELEMENT() {
        faces_list = new ArrayList<FACE>();
    }

    public void addFace(FACE param_face){
        faces_list.add(param_face);
        param_face.addAttachedElement(this);
    }


    @Override
    public boolean equals(Object obj) {
        ELEMENT other = (ELEMENT) obj;
        return other.id == this.id;
    }
}
