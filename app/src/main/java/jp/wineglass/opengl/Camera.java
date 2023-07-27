package jp.wineglass.opengl;

import jp.wineglass.quaternion.Vector3f;


public class Camera {
    private Vector3f eye = new Vector3f(0.0f, 0.0f, 3.0f);
    private Vector3f center = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);

    public Vector3f getEye() {
        return eye;
    }

    public void setEye(Vector3f eye) {
        this.eye = eye;
    }

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
    }

    public Vector3f getUp() {
        return up;
    }

    public void setUp(Vector3f up) {
        this.up = up;
    }
}
