public class NODE {

    private int node_id;
    private double[] node_coord;
    private boolean highlight;

    public NODE(){
        node_id = 0;
        node_coord = new double[3];
        highlight = false;
    }

    public NODE(int param_id, double[] param_coord, boolean param_highlight){
        node_id = param_id;
        node_coord = param_coord;
        highlight = param_highlight;
    }

    public int getNode_id() {
        return node_id;
    }
}
