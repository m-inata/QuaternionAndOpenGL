package jp.wineglass.opengl;

import android.opengl.GLES11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.wineglass.quaternion.Quaternionf;

import static jp.wineglass.quaternion.QuaternionfKt.adj;

public class Cube {
    private static final int DIMENSION = 3;

    private final float[] diffuse;
    private final float[] ambient;
    private final float[] specular;
    private final float[] shininess;

    // A(-1, 1, 1)  B(1, 1, 1)  C(1, 1, -1)  D(-1, 1, -1)
    // E(-1, -1, 1)  F(1, -1, 1)  G(1, -1, -1)  H(-1, -1, -1)
    private final float[] vertices = {
            //top △ABC
            -1,  1,  1,
            1, 1, 1,
            1,  1, -1,
            //top △ACD
            -1,  1,  1,
            1, 1,  -1,
            -1,  1, -1,
            //bottom △EGF
            -1, -1, 1,
            1, -1, -1,
            1, -1, 1,
            //bottom △EHG
            -1, -1, 1,
            -1, -1, -1,
            1, -1, -1,
            //front △AEB
            -1, 1,  1,
            -1,  -1, 1,
            1, 1, 1,
            //front △BEF
            1,  1,  1,
            -1, -1,  1,
            1, -1, 1,
            //back △DCG
            -1, 1, -1,
            1, 1, -1,
            1, -1, -1,
            //back △DGH
            -1, 1, -1,
            1, -1, -1,
            -1, -1, -1,
            //right △BFC
            1, 1, 1,
            1, -1, 1,
            1, 1, -1,
            //right △CFG
            1, 1, -1,
            1, -1, 1,
            1, -1, -1,
            //left △AHE
            -1, 1, 1,
            -1, -1, -1,
            -1, -1, 1,
            //left △ADH
            -1, 1, 1,
            -1, 1, -1,
            -1, -1, -1,
    };

    private final float[] normals = {
            //top △ABC
            0,  1, 0,
            0,  1, 0,
            0,  1, 0,
            //top △ACD
            0,  1, 0,
            0,  1, 0,
            0,  1, 0,
            //bottom △EGF
            0,  -1, 0,
            0,  -1, 0,
            0,  -1, 0,
            //bottom △EHG
            0,  -1, 0,
            0,  -1, 0,
            0,  -1, 0,
            //front △AEB
            0,  0, 1,
            0,  0, 1,
            0,  0, 1,
            //front △BEF
            0,  0, 1,
            0,  0, 1,
            0,  0, 1,
            //back △DCG
            0,  0, -1,
            0,  0, -1,
            0,  0, -1,
            //back △DHG
            0,  0, -1,
            0,  0, -1,
            0,  0, -1,
            //right △BFC
            1,  0, 0,
            1,  0, 0,
            1,  0, 0,
            //right △CFG
            1,  0, 0,
            1,  0, 0,
            1,  0, 0,
            //left △AHE
            -1,  0, 0,
            -1,  0, 0,
            -1,  0, 0,
            //left △ADH
            -1,  0, 0,
            -1,  0, 0,
            -1,  0, 0,
    };

    public Cube(Builder builder) {
        this.diffuse = builder.diffuse;
        this.ambient = builder.ambient;
        this.specular = builder.specular;
        this.shininess = builder.shininess;
    }

    public Cube(Builder builder, float scale) {
        this(builder);

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] *= scale;
        }
    }

    public void onDrawFrame(GL10 gl10, Quaternionf rotQuat) {
        for (int i = 0; i < vertices.length / DIMENSION; i++) {
            float x = vertices[3 * i];
            float y = vertices[3 * i + 1];
            float z = vertices[3 * i + 2];
            Quaternionf v = new Quaternionf(0.0f, x, y, z);
            Quaternionf rotQat_ = adj(rotQuat);
            Quaternionf result = rotQuat.multiply(v).multiply(rotQat_);
            vertices[3 * i] = result.getX();
            vertices[3 * i + 1] = result.getY();
            vertices[3 * i + 2] = result.getZ();
        }

        for (int i = 0; i < normals.length / DIMENSION; i++) {
            float x = normals[3 * i];
            float y = normals[3 * i + 1];
            float z = normals[3 * i + 2];
            Quaternionf v = new Quaternionf(0.0f, x, y, z);
            Quaternionf rotQat_ = adj(rotQuat);
            Quaternionf result = rotQuat.multiply(v).multiply(rotQat_);
            normals[3 * i] = result.getX();
            normals[3 * i + 1] = result.getY();
            normals[3 * i + 2] = result.getZ();
        }

        FloatBuffer vertBuf = ByteBuffer.allocateDirect(vertices.length * Float.BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertBuf.put(vertices).position(0);
        GLES11.glVertexPointer(DIMENSION, GLES11.GL_FLOAT,0, vertBuf);

        FloatBuffer normalBuf = ByteBuffer.allocateDirect(normals.length * Float.BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        normalBuf.put(normals).position(0);
        GLES11.glNormalPointer(GLES11.GL_FLOAT, 0, normalBuf);

        GLES11.glMaterialfv(GLES11.GL_FRONT_AND_BACK, GLES11.GL_DIFFUSE, diffuse,0);
        GLES11.glMaterialfv(GLES11.GL_FRONT_AND_BACK, GLES11.GL_AMBIENT, ambient,0);
        GLES11.glMaterialfv(GLES11.GL_FRONT_AND_BACK, GLES11.GL_SPECULAR, specular,0);
        GLES11.glMaterialfv(GLES11.GL_FRONT_AND_BACK, GLES11.GL_SHININESS, shininess,0);

        GLES11.glDrawArrays(GLES11.GL_TRIANGLES,0,vertices.length / DIMENSION);
    }


    public static class Builder {
        private float[] diffuse = new float[] {0.8f, 0.8f, 0.8f, 1.0f};
        private float[] ambient = new float[] {0.2f, 0.2f, 0.2f, 1.0f};
        private float[] specular = new float[] {0, 0, 0, 1};
        private float[] shininess = new float[] {0};

        public Builder diffuse(float r, float g, float b, float a) {
            this.diffuse = new float[] {r, g, b, a};
            return this;
        }

        public Builder ambient(float r, float g, float b, float a) {
            this.ambient = new float[] {r, g, b, a};
            return this;
        }

        public Builder specular(float r, float g, float b, float a) {
            this.specular = new float[] {r, g, b, a};
            return this;
        }

        public Builder shininess(float specluarExponent) {
            this.shininess = new float[] {specluarExponent};
            return this;
        }
    }

}
