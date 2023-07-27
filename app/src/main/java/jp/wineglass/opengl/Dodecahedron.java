package jp.wineglass.opengl;

import android.opengl.GLES11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.wineglass.quaternion.Quaternionf;

import static jp.wineglass.quaternion.QuaternionfKt.adj;


public class Dodecahedron {
    private static final int DIMENSION = 3;
    private static final float r5 = (float) Math.sqrt(5);
    private static final float phi = (float) (r5+1)/2;
    private static final float phi_i = (float) (r5-1)/2;

    private final float[] diffuse;
    private final float[] ambient;
    private final float[] specular;
    private final float[] shininess;

    // A(1, 1, 1)  B(0, phi_i, phi)  C(0, -phi_i, phi)  D(1, -1, 1)  E(phi, 0, phi_i)
    // F(phi_i, phi, 0)  G(-1, 1, 1)  H(-1, -1, 1)  I(phi_i, -phi, 0)  J(phi, 0, -phi_i)
    // K(-phi_i, phi, 0)  L(-phi, 0, phi_i)  M(-phi_i, -phi, 0)  N(1, -1, -1) O(1, 1, -1)
    // P(-1, 1, -1)  Q(-phi, 0, -phi_i)  R(-1, -1, -1)  S(0, -phi_i, -phi)  T(0, phi_i, -phi)
    private final float[] vertices = {
            // △ABC
            1,  1,  1,
            0, phi_i, phi,
            0, -phi_i, phi,
            // △ACD
            1,  1,  1,
            0, -phi_i, phi,
            1, -1, 1,
            // △ADE
            1,  1,  1,
            1, -1, 1,
            phi, 0, phi_i,

            // △AFK
            1,  1,  1,
            phi_i, phi, 0,
            -phi_i, phi, 0,
            // △AKG
            1,  1,  1,
            -phi_i, phi, 0,
            -1, 1, 1,
            // △AGB
            1,  1,  1,
            -1, 1, 1,
            0, phi_i, phi,

            // △BGL
            0, phi_i, phi,
            -1, 1, 1,
            -phi, 0, phi_i,
            // △BLH
            0, phi_i, phi,
            -phi, 0, phi_i,
            -1, -1, 1,
            // △BHC
            0, phi_i, phi,
            -1, -1, 1,
            0, -phi_i, phi,

            // △CHM
            0, -phi_i, phi,
            -1, -1, 1,
            -phi_i, -phi, 0,
            // △CMI
            0, -phi_i, phi,
            -phi_i, -phi, 0,
            phi_i, -phi, 0,
            // △CID
            0, -phi_i, phi,
            phi_i, -phi, 0,
            1, -1, 1,

            // △DIN
            1, -1, 1,
            phi_i, -phi, 0,
            1, -1, -1,
            // △DNJ
            1, -1, 1,
            1, -1, -1,
            phi, 0, -phi_i,
            // △DJE
            1, -1, 1,
            phi, 0, -phi_i,
            phi, 0, phi_i,

            // △AEJ
            1,  1,  1,
            phi, 0, phi_i,
            phi, 0, -phi_i,
            // △AJO
            1,  1,  1,
            phi, 0, -phi_i,
            1, 1, -1,
            // △AOF
            1,  1,  1,
            1, 1, -1,
            phi_i, phi, 0,


            // △TSR
            0, phi_i, -phi,
            0, -phi_i, -phi,
            -1, -1, -1,
            // △TRQ
            0, phi_i, -phi,
            -1, -1, -1,
            -phi, 0, -phi_i,
            // △TQP
            0, phi_i, -phi,
            -phi, 0, -phi_i,
            -1, 1, -1,

            // △TOJ
            0, phi_i, -phi,
            1, 1, -1,
            phi, 0, -phi_i,
            // △TJN
            0, phi_i, -phi,
            phi, 0, -phi_i,
            1, -1, -1,
            // △TNS
            0, phi_i, -phi,
            1, -1, -1,
            0, -phi_i, -phi,

            // △SNI
            0, -phi_i, -phi,
            1, -1, -1,
            phi_i, -phi, 0,
            // △SIM
            0, -phi_i, -phi,
            phi_i, -phi, 0,
            -phi_i, -phi, 0,
            // △SMR
            0, -phi_i, -phi,
            -phi_i, -phi, 0,
            -1, -1, -1,

            // △RMH
            -1, -1, -1,
            -phi_i, -phi, 0,
            -1, -1, 1,
            // △RHL
            -1, -1, -1,
            -1, -1, 1,
            -phi, 0, phi_i,
            // △RLQ
            -1, -1, -1,
            -phi, 0, phi_i,
            -phi, 0, -phi_i,

            // △QLG
            -phi, 0, -phi_i,
            -phi, 0, phi_i,
            -1, 1, 1,
            // △QGK
            -phi, 0, -phi_i,
            -1, 1, 1,
            -phi_i, phi, 0,
            // △QKP
            -phi, 0, -phi_i,
            -phi_i, phi, 0,
            -1, 1, -1,

            // △PKF
            -1, 1, -1,
            -phi_i, phi, 0,
            phi_i, phi, 0,
            // △PFO
            -1, 1, -1,
            phi_i, phi, 0,
            1, 1, -1,
            // △POT
            -1, 1, -1,
            1, 1, -1,
            0, phi_i, -phi,
    };

    private final float[] normals = {
            // △ABC
            0.52573111f, 0, 0.85065081f,
            0.52573111f, 0, 0.85065081f,
            0.52573111f, 0, 0.85065081f,
            // △ACD
            0.52573111f, 0, 0.85065081f,
            0.52573111f, 0, 0.85065081f,
            0.52573111f, 0, 0.85065081f,
            // △ADE
            0.52573111f, 0, 0.85065081f,
            0.52573111f, 0, 0.85065081f,
            0.52573111f, 0, 0.85065081f,

            // △AFK
            0, 0.85065081f, 0.52573111f,
            0, 0.85065081f, 0.52573111f,
            0, 0.85065081f, 0.52573111f,
            // △AKG
            0, 0.85065081f, 0.52573111f,
            0, 0.85065081f, 0.52573111f,
            0, 0.85065081f, 0.52573111f,
            // △AGB
            0, 0.85065081f, 0.52573111f,
            0, 0.85065081f, 0.52573111f,
            0, 0.85065081f, 0.52573111f,

            // △BGL
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,
            // △BLH
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,
            // △BHC
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,
            -5.25731112e-01f, 1.52809090e-16f, 8.50650808e-01f,

            // △CHM
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,
            // △CMI
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,
            // △CID
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,
            -7.64045449e-17f, -8.50650808e-01f, 5.25731112e-01f,

            // △DIN
            0.85065081f, -0.52573111f,  0,
            0.85065081f, -0.52573111f,  0,
            0.85065081f, -0.52573111f,  0,
            // △DNJ
            0.85065081f, -0.52573111f,  0,
            0.85065081f, -0.52573111f,  0,
            0.85065081f, -0.52573111f,  0,
            // △DJE
            0.85065081f, -0.52573111f,  0,
            0.85065081f, -0.52573111f,  0,
            0.85065081f, -0.52573111f,  0,

            // △AEJ
            0.85065081f, 0.52573111f, 0,
            0.85065081f, 0.52573111f, 0,
            0.85065081f, 0.52573111f, 0,
            // △AJO
            0.85065081f, 0.52573111f, 0,
            0.85065081f, 0.52573111f, 0,
            0.85065081f, 0.52573111f, 0,
            // △AOF
            0.85065081f, 0.52573111f, 0,
            0.85065081f, 0.52573111f, 0,
            0.85065081f, 0.52573111f, 0,

            // △TSR
            -0.52573111f, 0, -0.85065081f,
            -0.52573111f, 0, -0.85065081f,
            -0.52573111f, 0, -0.85065081f,
            // △TRQ
            -0.52573111f, 0, -0.85065081f,
            -0.52573111f, 0, -0.85065081f,
            -0.52573111f, 0, -0.85065081f,
            // △TQP
            -0.52573111f, 0, -0.85065081f,
            -0.52573111f, 0, -0.85065081f,
            -0.52573111f, 0, -0.85065081f,

            // △TOJ
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,
            // △TJN
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,
            // △TNS
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,
            5.25731112e-01f,  1.52809090e-16f, -8.50650808e-01f,


            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,
            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,
            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,

            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,
            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,
            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,

            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,
            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,
            7.64045449e-17f, -8.50650808e-01f, -5.25731112e-01f,


            -0.85065081f, -0.52573111f,  0,
            -0.85065081f, -0.52573111f,  0,
            -0.85065081f, -0.52573111f,  0,

            -0.85065081f, -0.52573111f,  0,
            -0.85065081f, -0.52573111f,  0,
            -0.85065081f, -0.52573111f,  0,

            -0.85065081f, -0.52573111f,  0,
            -0.85065081f, -0.52573111f,  0,
            -0.85065081f, -0.52573111f,  0,


            -0.85065081f,  0.52573111f,  0,
            -0.85065081f,  0.52573111f,  0,
            -0.85065081f,  0.52573111f,  0,

            -0.85065081f,  0.52573111f,  0,
            -0.85065081f,  0.52573111f,  0,
            -0.85065081f,  0.52573111f,  0,

            -0.85065081f,  0.52573111f,  0,
            -0.85065081f,  0.52573111f,  0,
            -0.85065081f,  0.52573111f,  0,


            0, 0.85065081f, -0.52573111f,
            0, 0.85065081f, -0.52573111f,
            0, 0.85065081f, -0.52573111f,

            0, 0.85065081f, -0.52573111f,
            0, 0.85065081f, -0.52573111f,
            0, 0.85065081f, -0.52573111f,

            0, 0.85065081f, -0.52573111f,
            0, 0.85065081f, -0.52573111f,
            0, 0.85065081f, -0.52573111f,
    };

    public Dodecahedron(Builder builder) {
        this.diffuse = builder.diffuse;
        this.ambient = builder.ambient;
        this.specular = builder.specular;
        this.shininess = builder.shininess;
    }

    public Dodecahedron(Builder builder, float scale) {
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

        GLES11.glPointSize(3.0f);
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
