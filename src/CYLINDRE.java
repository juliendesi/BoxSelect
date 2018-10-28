import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CYLINDRE extends SELECTION {

    protected double rayon_ext;
    protected double rayon_int;
    protected double[] center_inf;
    protected double[] center_sup;
    protected double tolerance;

    public CYLINDRE() {
        super();
        rayon_ext = 5.0;
        rayon_int = 0.0;
        center_inf = new double[3];
        center_sup = new double[3];
        for (int i = 0; i < 3; i++) {
            center_inf[i] = 0.0;
        }
        tolerance = 0.005;
        this.setOrientation();
        this.updateIntersection();
    }

    public void setCenter_inf(int composante, double value) {
        this.center_inf[composante] = value;
        this.setOrientation();
        this.updateIntersection();
    }

    public void setCenter_sup(int composante, double value) {
        this.center_sup[composante] = value;
        this.setOrientation();
        this.updateIntersection();
    }

    public double getCenter_inf(int composante) {
        return center_inf[composante];
    }

    public double getCenter_sup(int composante) {
        return center_sup[composante];
    }

    public double getRayon_ext() {
        return rayon_ext;
    }

    public void setRayon_ext(double rayon_ext) {
        this.rayon_ext = rayon_ext;
        this.updateIntersection();
    }

    public double getRayon_int() {
        return rayon_int;
    }

    public void setRayon_int(double rayon_int) {
        this.rayon_int = rayon_int;
        this.updateIntersection();
    }

    protected void updateIntersection() {
        if (faces_to_treat != null) {
            this.computeIntersection();
        }
    }

    @Override
    protected void computeIntersection() {
        this.nodes_list.clear();

        Set set = new HashSet();
        double[] coord;
        for (int i = 0; i < faces_to_treat.size(); i++) {
            set.addAll(faces_to_treat.get(i).getNodes_list());
        }
        ArrayList<NODE> list_node = new ArrayList<>(set);
        for (int i = 0; i < list_node.size(); i++) {
            coord = this.inLocalFrame(list_node.get(i).getCoord());
            if (isInCylinder(coord)) {
                list_node.get(i).setHighlight(true);
                nodes_list.add(list_node.get(i));
            } else list_node.get(i).setHighlight(false);
        }
    }

    protected void setOrientation() {
        double[] vecteur = new double[3];
        double longueur_vecteur = 0.0;
        for (int i = 0; i < 3; i++) {
            vecteur[i] = center_sup[i] - center_inf[i];
            longueur_vecteur = longueur_vecteur + vecteur[i] * vecteur[i];
        }
        if (longueur_vecteur == 0) {
            theta = 0.0;
            phi = 0.0;
        } else {
            longueur_vecteur = Math.sqrt(longueur_vecteur);
            for (int i = 0; i < 3; i++) {
                vecteur[i] = vecteur[i] / longueur_vecteur;
            }
            theta = Math.acos(vecteur[2]);
            phi = Math.acos(Math.sin(theta) * vecteur[0] + Math.cos(theta) * vecteur[2]);
        }
    }

    protected double[] inLocalFrame(double[] coord_globale) {
        double[] coord_locale = new double[3];
        double[] coord_glob_centered = new double[3];
        for (int i = 0; i < 3; i++) {
            coord_glob_centered[i] = coord_globale[i] - center_inf[i];
        }
        double cth = Math.cos(theta), cph = Math.cos(phi);
        double sth = Math.sin(theta), sph = Math.sin(phi);

        coord_locale[0] = cth * coord_glob_centered[0] + Math.pow(sph, 2.0) * coord_glob_centered[1] - sth * cph * coord_glob_centered[2];
        coord_locale[1] = cph * coord_glob_centered[1] + sph * coord_glob_centered[2];
        coord_locale[2] = sth * coord_glob_centered[0] - sph * cth * coord_glob_centered[1] + cph * cph * coord_glob_centered[2];
        return coord_locale;
    }

    protected boolean isInCylinder(double[] coord_locale) {
        double[] local_center_sup;
        double distance_2;
        local_center_sup = this.inLocalFrame(center_sup);
        // tester la coordonnée Z
        if (coord_locale[2] < local_center_sup[2] + tolerance && coord_locale[2] > 0 - tolerance) {
            distance_2 = coord_locale[0] * coord_locale[0] + coord_locale[1] * coord_locale[1];
            return distance_2 < rayon_ext * rayon_ext && distance_2 > rayon_int * rayon_int;
        }
        return false;
    }
}
