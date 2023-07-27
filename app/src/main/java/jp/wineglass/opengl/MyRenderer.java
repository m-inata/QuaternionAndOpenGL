package jp.wineglass.opengl;

import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.wineglass.quaternion.Quaternionf;
import jp.wineglass.quaternion.Vector3f;

import static jp.wineglass.quaternion.Vector3fKt.length;
import static jp.wineglass.quaternion.Vector3fKt.toUnit;


public class MyRenderer implements GLSurfaceView.Renderer {
//    private Tetrahedron tetrahedron
//            = new Tetrahedron(new Tetrahedron.Builder()
//            .ambient(0.1745f, 0.01175f, 0.01175f, 1.0f)
//            .diffuse(0.61424f, 0.04136f, 0.04136f, 1.0f)
//            .specular(0.727811f, 0.326959f, 0.626959f, 1.0f)
//            .shininess(1.0f),
//            1.73f
//    );

    private Cube cube = new Cube(new Cube.Builder()
            .ambient(0.1745f, 0.1745f, 0.01175f, 1.0f)
            .diffuse(0.61424f, 0.61424f, 0.04136f, 1.0f)
            .specular(0.727811f, 0.727811f, 0.626959f, 1.0f)
            .shininess(1.0f),
            1.73f);

//    private Octahedron octahedron = new Octahedron(new Octahedron.Builder()
//            .ambient(0.01175f, 0.1745f, 0.01175f, 1.0f)
//            .diffuse(0.04136f, 0.61424f, 0.04136f, 1.0f)
//            .specular(0.326959f,0.427811f,0.626959f, 1.0f)
//            .shininess(1.0f),
//            3.0f);

    private Dodecahedron dodecahedron = new Dodecahedron(new Dodecahedron.Builder()
            .ambient(0.01175f, 0.1745f, 0.1745f, 1.0f)
            .diffuse(0.03136f, 0.61424f, 0.61424f, 1.0f)
            .specular(0.326959f,0.427811f,0.427811f, 1.0f)
            .shininess(1.0f),
            1.73f);

//    private Icosahedron icosahedron = new Icosahedron(new Icosahedron.Builder()
//            .ambient(0.01175f, 0.01175f, 0.03175f, 1.0f)
//            .diffuse(0.40424f, 0.40424f, 0.40424f, 1.0f)
//            .specular(0.326959f,0.326959f,0.326959f, 1.0f)
//            .shininess(1.5f));

    private Camera camera = new Camera();
    private final MyGLSurfaceView myGLSurfaceView;

    private Polyhedron rendered = Polyhedron.TETRAHEDRON;

    private Quaternionf rotQuat = new Quaternionf(1.0f, 0.0f, 0.0f, 0.0f);


    public MyRenderer(MyGLSurfaceView myGLSurfaceView) {
        this.myGLSurfaceView = myGLSurfaceView;
    }

    public void setRendered(Polyhedron rendered) {
        this.rendered = rendered;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        float a1 = myGLSurfaceView.getDx();
        float a2 = myGLSurfaceView.getDy();

        if (a1 * a1 + a2 * a2 >= 0.001f) {
            Vector3f toCenter = camera.getCenter().add(camera.getEye().m());
            Vector3f up = camera.getUp();
            Vector3f e1 = toUnit(toCenter.cross(up));
            Vector3f e2 = toUnit(e1.cross(toCenter));
            Vector3f v = e1.multiply(a1).add(e2.multiply(a2));
            Vector3f rotAxis = v.cross(toUnit(toCenter));

            final float constant = 0.01f;
            float c = (float) Math.cos(length(rotAxis) * constant);
            float s = (float) Math.sin(length(rotAxis) * constant);
            Vector3f u = toUnit(rotAxis);
            Quaternionf direction = new Quaternionf(0.0f, u.getX(), u.getY(), u.getZ());
            rotQuat = new Quaternionf(c, 0, 0, 0).add(direction.multiply(s));

            myGLSurfaceView.setDx(0.0f);
            myGLSurfaceView.setDy(0.0f);

        } else {
            rotQuat = new Quaternionf(1.0f, 0.0f, 0.0f, 0.0f);
        }


        GLES11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES11.glClear(GLES11.GL_COLOR_BUFFER_BIT | GLES11.GL_DEPTH_BUFFER_BIT);

        GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
        GLES11.glLoadIdentity();
        Vector3f eye = camera.getEye();
        Vector3f center = camera.getCenter();
        Vector3f up = camera.getUp();
        GLU.gluLookAt(gl10, eye.getX(), eye.getY(), eye.getZ(),
                center.getX(), center.getY(), center.getZ(),
                up.getX(), up.getY(), up.getZ());

        switch (rendered) {
//            case TETRAHEDRON:
//                tetrahedron.onDrawFrame(gl10, rotQuat);
//                break;
            case CUBE:
                cube.onDrawFrame(gl10, rotQuat);
                break;
//            case OCTAHEDRON:
//                octahedron.onDrawFrame(gl10, rotQuat);
//                break;
            case DODECAHEDRON:
                dodecahedron.onDrawFrame(gl10, rotQuat);
                break;
//            case ICOSAHEDRON:
//                icosahedron.onDrawFrame(gl10, rotQuat);
//                break;
//            default:
//                tetrahedron.onDrawFrame(gl10, rotQuat);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int mWidth, int mHeight) {
        GLES11.glViewport(0, 0, mWidth, mHeight);

        GLES11.glMatrixMode(GLES11.GL_PROJECTION);
        GLES11.glLoadIdentity();
//        GLES11.glFrustumf(-1,1,-1,1,1f,100f);
        GLES11.glOrthof(-5.0f, 5.0f, -5.0f, 5.0f, -5.5f, 5.5f);
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        GLES11.glEnableClientState(GLES11.GL_VERTEX_ARRAY);
        GLES11.glEnableClientState(GLES11.GL_NORMAL_ARRAY);

        GLES11.glEnable(GLES11.GL_CULL_FACE);
        GLES11.glFrontFace(GLES11.GL_CCW);


        GLES11.glEnable(GLES11.GL_LIGHTING);
        int light = GLES11.GL_LIGHT0;
        GLES11.glEnable(light);
        GLES11.glLightfv(light, GLES11.GL_POSITION, new float[]{.0f,.0f,.5f,.0f},0);
        GLES11.glLightfv(light, GLES11.GL_DIFFUSE,  new float[]{.6f,.6f,.6f,.0f},0);
        GLES11.glLightfv(light, GLES11.GL_AMBIENT,  new float[]{.6f,.6f,.6f,.0f},0);
        GLES11.glLightfv(light, GLES11.GL_SPECULAR, new float[]{.5f,.5f,.5f,.0f},0);
    }
}
