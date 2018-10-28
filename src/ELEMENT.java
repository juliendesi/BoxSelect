import java.util.ArrayList;

public class ELEMENT extends REFERENCE implements Comparable {

    private ArrayList<FACE> faces_list;
    private String type;

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

    public ArrayList<FACE> getFaces_list() {
        return faces_list;
    }

    @Override
    public boolean equals(Object obj) {
        ELEMENT other = (ELEMENT) obj;
        return other.id == this.id;
    }

    public void setType() {
        int nb_faces = faces_list.size();
        int nb_node = 0;
        for (FACE face : faces_list) {
            nb_node = nb_node + face.getNbNode();
        }
        switch (nb_faces) {
            case 1:
                // 1D ou 2D
                if (nb_node == 1) {
                    type = "POINT";
                } else if (nb_node == 2) {
                    type = "BAR";
                } else if (nb_node == 3) {
                    type = "TRIA";
                }  // 6 en degrés 2
                else if (nb_node == 4) {
                    type = "QUAD";
                }  // 8 en degrés 2
                else System.out.println("Degrés 2 non-implémenté");
                break;
            case 2:
                // 3D
                if (nb_node == 4) {
                    type = "TETRA";
                }  // 10 en degrés 2
                else if (nb_node == 6) {
                    type = "PENTA";
                }  // 15 en degrés 2
                else if (nb_node == 8) {
                    type = "HEXA";
                }  // 20 en degrés 2
                else System.out.println("Degrés 2 non-implémenté");
                break;
            default:
                System.out.println("Erreur : Evaluation du type (nombre faces incohérent)");
                break;
        }
    }

    public void construireFaceManquante() {
        if (type == "TETRA") {
            NODE sommet;
            int index_face = 0, index_sommet = 1;
            ArrayList<NODE> face_existante;
            ArrayList<NODE>[] new_faces = new ArrayList[3];

            for (int i = 0; i < faces_list.size(); i++) {
                if (faces_list.get(i).getNbNode() == 1) {
                    index_sommet = i;
                } else index_face = i;
            }
            face_existante = faces_list.get(index_face).getNodes_list();
            sommet = faces_list.get(index_sommet).getNodes_list().get(0);
            faces_list.remove(index_sommet);

            for (int i = 0; i < new_faces.length; i++) {
                new_faces[i] = new ArrayList<>();
            }
            // face 1
            new_faces[0].add(face_existante.get(0));
            new_faces[0].add(face_existante.get(1));
            new_faces[0].add(sommet);
            this.addFace(new FACE(new_faces[0]));

            // face 2
            new_faces[1].add(face_existante.get(1));
            new_faces[1].add(face_existante.get(2));
            new_faces[1].add(sommet);
            this.addFace(new FACE(new_faces[1]));

            // face 3
            new_faces[2].add(face_existante.get(0));
            new_faces[2].add(face_existante.get(2));
            new_faces[2].add(sommet);
            this.addFace(new FACE(new_faces[2]));
        } else if (type == "PENTA") {
            this.constructFacesManquanteFromExtrusion(6);
        } else if (type == "HEXA") {
            this.constructFacesManquanteFromExtrusion(8);
        }
    }

    private void constructFacesManquanteFromExtrusion(int nb_node) {
        ArrayList<NODE> f1 = faces_list.get(0).getNodes_list();
        ArrayList<NODE> f2 = faces_list.get(1).getNodes_list();
        for (int i = 0; i < nb_node / 2; i++) {
            ArrayList<NODE> new_face = new ArrayList<>();
            new_face.add(f1.get(i));
            if (i == nb_node / 2 - 1) {
                new_face.add(f1.get(0));
                new_face.add(f2.get(0));
            } else {
                new_face.add(f1.get(i + 1));
                new_face.add(f2.get(i + 1));
            }
            new_face.add(f2.get(i));
            this.addFace(new FACE(new_face));
        }
    }


    @Override
    public int compareTo(Object o) {
        if (this.id == ((ELEMENT) o).getId()) {
            return 0;
        } else if (this.id < ((ELEMENT) o).getId()) {
            return -1;
        } else return 1;
    }
}
