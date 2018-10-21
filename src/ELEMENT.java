import java.util.ArrayList;

public class ELEMENT {

    private ArrayList<FACE> faces_list;

    public ELEMENT(FACE param_face){
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
