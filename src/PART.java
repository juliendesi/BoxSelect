import java.util.ArrayList;

public class PART {

    private String part_name;
    private ArrayList<ELEMENT> elements_list;
    private ArrayList<FACE> face_exterieur;
    private long time_face_manquante;
    private long time_detect_interface;

    public PART(String param_name) {
        part_name = param_name;
        elements_list = new ArrayList<>();
        face_exterieur = new ArrayList<>();
    }

    public String getPart_name() {
        return part_name;
    }

    public void addElement(ELEMENT param_elem) {
        elements_list.add(param_elem);
    }

    public void numberingFaces() {
        int compteur_id = 1;
        for (ELEMENT elem : elements_list)
            for (FACE face : elem.getFaces_list()) {
                face.setId(compteur_id);
                compteur_id++;
            }
    }

    public void detectInterfaceFromFaces() {
        ArrayList<FACE> faces = new ArrayList<>();

        // on récupère toutes les faces
        for (final ELEMENT elem : elements_list) {
            faces.addAll(elem.getFaces_list());
        }

        for (final FACE face : faces) {
            // on récupère les faces attachées au premier noeud
            ArrayList<FACE> face_attached = face.getNodes_list().get(0).getAttached_faces();

            // Pour les 2 noeuds, on teste si les faces contiennent ces deux noeuds
            for (FACE f1 : face_attached)
                if (f1.equals(face) == false && f1.contains(face.getNodes_list().get(1)) && f1.contains(face.getNodes_list().get(2))) {
                    //face = f1;
                    face.setInterface(false);
                }
            if (face.getInterface()) {
                face_exterieur.add(face);
            }
        }
    }

    public void finalizePart() {
        //System.out.println("Traitement de " + part_name);
        long time = System.currentTimeMillis();
        for (int i = 0; i < elements_list.size(); i++) {
            //System.out.println("Element " + i + " : ID " + elements_list.get(i).getId());
            elements_list.get(i).setType();
            elements_list.get(i).construireFaceManquante();
        }
        time_face_manquante = System.currentTimeMillis() - time;
        System.out.println("Face manquante : " + time_face_manquante);
        this.numberingFaces();
        this.detectInterfaceFromFaces();
        System.out.println("Finalisation de " + part_name + " : " + (System.currentTimeMillis() - time_face_manquante - time) + "ms");
    }

    public ArrayList<FACE> getFace_exterieur() {
        return face_exterieur;
    }
}
