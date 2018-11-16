import java.util.ArrayList;
import java.util.Collections;

public class GraphicTreatment {

    private MODEL model;
    private ArrayList<int[]> indiceFaces;
    private ArrayList<float[]> nodeCoord;

    public GraphicTreatment(MODEL param_model) {
        long time = System.currentTimeMillis();
        model = param_model;
        indiceFaces = new ArrayList<>();
        nodeCoord = new ArrayList<>();
        for (int i = 0; i < model.numberOfParts(); i++) {
            createVerticesTab(model.getPart(i));
            createFaceIndices(model.getPart(i));
        }
        System.out.println("Creation du module graphique : " + (System.currentTimeMillis() - time) + "ms");
    }

    public void createVerticesTab(PART part) {
        ArrayList<NODE> nodesList = new ArrayList<>();
        ArrayList<FACE> facesList = part.getFace_exterieur();
        for (int i = 0; i < facesList.size(); i++) {
            nodesList.addAll(facesList.get(i).getNodes_list());
        }
        ArrayList<NODE> filteredNodes = filteringDoubleInArray(nodesList);
        nodeCoord.add(new float[3 * filteredNodes.size()]);
        for (int j = 0; j < filteredNodes.size(); j++)
            for (int k = 0; k < 3; k++) {
                nodeCoord.get(nodeCoord.size() - 1)[3 * j + k] = (float) (filteredNodes.get(j).getCoord()[k]);
            }
    }

    public ArrayList<NODE> filteringDoubleInArray(ArrayList<NODE> original) {
        ArrayList<NODE> filteredArray = new ArrayList<>();
        Collections.sort(original);
        int id = -1;
        int compteur = 0;
        for (int i = 0; i < original.size(); i++) {
            if (id != original.get(i).getId()) {
                filteredArray.add(original.get(i));
                filteredArray.get(compteur).setIndiceGraphic(compteur);
                compteur++;
                id = original.get(i).getId();
            } else i++;
        }
        return filteredArray;
    }

    public void createFaceIndices(PART part) {
        ArrayList<Integer> listIndice = new ArrayList<>();
        ArrayList<FACE> facesList = part.getFace_exterieur();
        for (int i = 0; i < facesList.size(); i++) {
            int nbNodeInFace = facesList.get(i).getNbNode();
            if (nbNodeInFace == 3) {
                for (int j = 0; j < 3; j++) {
                    listIndice.add(facesList.get(i).getNodes_list().get(j).getIndiceGraphic());
                    listIndice.add(0);
                }
            } else if (nbNodeInFace == 4) {
                for (int j = 0; j < 3; j++) {
                    listIndice.add(facesList.get(i).getNodes_list().get(j).getIndiceGraphic());
                    listIndice.add(0);
                }
                listIndice.add(facesList.get(i).getNodes_list().get(0).getIndiceGraphic());
                listIndice.add(0);
                listIndice.add(facesList.get(i).getNodes_list().get(2).getIndiceGraphic());
                listIndice.add(0);
                listIndice.add(facesList.get(i).getNodes_list().get(3).getIndiceGraphic());
                listIndice.add(0);
            }
        }
        indiceFaces.add(new int[listIndice.size()]);
        for (int i = 0; i < listIndice.size(); i++) {
            indiceFaces.get(indiceFaces.size() - 1)[i] = listIndice.get(i);
        }
    }

    public int[] getIndicesFacesByPart(int partId) {
        return indiceFaces.get(partId);
    }

    public float[] getVerticesCoordByPart(int partId) {
        return nodeCoord.get(partId);
    }


}
