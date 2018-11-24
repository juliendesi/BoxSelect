import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.util.ArrayList;

public class InterfaceVue {

    private static final double CAMERA_INITIAL_DISTANCE = -1500;
    private static final double CAMERA_NEAR_CLIP = 0.01;
    private static final double CAMERA_FAR_CLIP = 100000.0;
    private static final double AXIS_LENGTH = 50.0;
    final Xform modelGroup = new Xform();
    final Group nodeSelected = new Group();
    final Group root = new Group();
    final Xform axisGroup = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final XformWorld world = new XformWorld();
    private Scene scene;
    final XformCamera cameraXform = new XformCamera();
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    }

    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        world.getChildren().addAll(axisGroup);
    }

    private void handleMouse() {
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry(mouseDeltaX * 180.0 / scene.getWidth());
                cameraXform.rx(-mouseDeltaY * 180.0 / scene.getHeight());
            } else if (me.isSecondaryButtonDown()) {
                camera.setTranslateZ(camera.getTranslateZ() + mouseDeltaY);
            }
        });
    }

    private void handleKeyboard() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                    case V:
                        modelGroup.setVisible(!modelGroup.isVisible());
                        break;
                }
            }
        });
    }

    public void createEmptyScene(int largeur, int hauteur) {

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        buildCamera();
        buildAxes();

        world.getChildren().add(modelGroup);
        world.getChildren().add(nodeSelected);
        scene = new Scene(root, largeur, hauteur, true);
        scene.setFill(Color.DARKGREY);
        handleKeyboard();
        handleMouse();

        scene.setCamera(camera);
    }

    public Scene getScene() {
        return scene;
    }

    private void createPart(int[] faceIndices, float[] verticesCoord) {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        TriangleMesh partMesh = new TriangleMesh();
        partMesh.getTexCoords().addAll(0, 0);
        partMesh.getPoints().addAll(verticesCoord);
        partMesh.getFaces().addAll(faceIndices);

        MeshView part = new MeshView(partMesh);
        part.setDrawMode(DrawMode.FILL);
        part.setMaterial(redMaterial);
        //part.setCullFace(CullFace.NONE);


        Xform partXform = new Xform();
        partXform.getChildren().add(part);
        modelGroup.getChildren().add(partXform);
        //world.getChildren().add(modelGroup);
    }

    public void addPart(int[] faceIndices, float[] verticesCoord) {
        createPart(faceIndices, verticesCoord);
        scene.setRoot(root);
    }

    private void createSphere(ArrayList<double[]> coord, double rayon) {
        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.LIGHTGRAY);
        whiteMaterial.setSpecularColor(Color.WHITE);

        for (int i = 0; i < coord.size(); i++) {
            Sphere node = new Sphere(rayon, 4);
            node.setDrawMode(DrawMode.FILL);
            node.setMaterial(whiteMaterial);

            //Xform nodeXform = new Xform();
            Group test = new Group(node);
            test.setTranslateX(coord.get(i)[0]);
            test.setTranslateY(coord.get(i)[1]);
            test.setTranslateZ(coord.get(i)[2]);
            nodeSelected.getChildren().addAll(test);
            //nodeXform.getChildren().add(node);
            //nodeXform.setTranslate(coord.get(i)[0], coord.get(i)[1], coord.get(i)[2]);
            //nodeSelected.getChildren().addAll(nodeXform);
        }
    }

    public void addSphere(ArrayList<double[]> coord, double rayon) {
        createSphere(coord, rayon);
        scene.setRoot(root);
    }

    public void clearNodeSelected() {
        nodeSelected.getChildren().removeAll();
        scene.setRoot(root);
    }

}

class XformWorld extends Group {
    final Translate t = new Translate(0.0, 0.0, 0.0);
    final Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    public XformWorld() {
        super();
        this.getTransforms().addAll(t, rx, ry, rz);
    }
}

class XformCamera extends Group {
    Point3D px = new Point3D(1.0, 0.0, 0.0);
    Point3D py = new Point3D(0.0, 1.0, 0.0);
    Rotate r;
    Transform t = new Rotate();

    public XformCamera() {
        super();
    }

    public void rx(double angle) {
        r = new Rotate(angle, px);
        this.t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    public void ry(double angle) {
        r = new Rotate(angle, py);
        this.t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

}
