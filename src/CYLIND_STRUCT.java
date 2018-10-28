public class CYLIND_STRUCT extends CYLINDRE {

    protected double z_sup;
    protected double z_inf;
    protected double[] center;

    public CYLIND_STRUCT() {
        super();
        z_inf = -1.0;
        z_sup = 1.0;
        center = new double[2];
        for (int i = 0; i < 2; i++) {
            center[i] = 0.0;
            center_inf[i] = center[i];
            center_sup[i] = center[i];
        }
        center_inf[2] = z_inf;
        center_sup[2] = z_sup;
        this.setOrientation();
        this.updateIntersection();
    }

    public void setCenter(int composante, double value) {
        this.center[composante] = value;
        this.center_inf[composante] = value;
        this.center_sup[composante] = value;
        this.setOrientation();
        this.updateIntersection();
    }

    public double getCenter(int composante) {
        return center[composante];
    }

    public double getZ_inf() {
        return z_inf;
    }

    public void setZ_inf(double z_inf) {
        this.z_inf = z_inf;
        this.center_inf[2] = z_inf;
        this.setOrientation();
        this.updateIntersection();
    }

    public double getZ_sup() {
        return z_sup;
    }

    public void setZ_sup(double z_sup) {
        this.z_sup = z_sup;
        this.center_sup[2] = z_sup;
        this.setOrientation();
        this.updateIntersection();
    }
}
