package jp.wineglass.quaternion3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.wineglass.opengl.MyGLSurfaceView
import jp.wineglass.opengl.MyRenderer
import jp.wineglass.opengl.Polyhedron


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val glView = findViewById<MyGLSurfaceView>(R.id.my_gl_surface_view)
        glView.setEGLConfigChooser(8, 8, 8, 8, 8, 8);
        val myRenderer = MyRenderer(glView)
        myRenderer.setRendered(Polyhedron.DODECAHEDRON);
        glView.setRenderer(myRenderer);
    }
}