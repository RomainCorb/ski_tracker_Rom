package cc_ski_track.ski_tracker;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;


import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

import cc_ski_track.ski_tracker.Examples.Square;
import cc_ski_track.ski_tracker.Examples.Arthur;
import cc_ski_track.ski_tracker.Examples.Mountain;



public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Square mSquare;
    private Mountain mMountain;
    private Arthur mArthur;
    private final float[] mMVPMatrix = new float[16];// mMVPMatrix="Model View Projection Matrix"
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private volatile float mAngle;
    private Context context;

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor((float)30/255, (float)144/255, 1.0f, 1.0f);
        //Initialisation et load du triangle
        //mTriangle = new Triangle();
        //Initialisation et load du carré
        mSquare = new Square();
        mArthur = new Arthur();
        mMountain = new Mountain(context);
    }

    public void onDrawFrame(GL10 unused) {
        float[] touche = new float[16];
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, -5, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Create a rotation transformation for the triangle

        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *MUST BE FIRST* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(touche, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        //mTriangle.draw();
        mMountain.draw(touche);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height); //centre, largeur, hauteur de l'écran
        float ratio = (float) width / (float) height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    //permet de compiler le code shader avant d'utiliser l'environnement OpenGL ES
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public MyGLRenderer(Context context){
        this.context = context;
    }
}