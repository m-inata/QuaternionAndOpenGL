package jp.wineglass.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;


public class MyGLSurfaceView extends GLSurfaceView {
    private float previousX = 0.0f;
    private float previousY = 0.0f;
    private TextView textView;

    private float dx = 0.0f;
    private float dy = 0.0f;

    private float pinchDistanceOld = -1.0f;
    private float toFar = 0.0f;

    public float getToFar() {
        return toFar;
    }

    public void setToFar(float toFar) {
        this.toFar = toFar;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setText(CharSequence chars) { textView.setText(chars); }


    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (e.getPointerCount() == 2) {
            float x0 = e.getX(0);
            float y0 = e.getY(0);
            float x1 = e.getX(1);
            float y1 = e.getY(1);
            float x = x1 - x0;
            float y = y1 - y0;
            float d = (float) Math.sqrt(x * x + y * y);

            if (pinchDistanceOld <= 0.0f) {
                pinchDistanceOld = d;
            } else {
                toFar = d - pinchDistanceOld;
            }

        } else {
            float x = e.getX();
            float y = e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:

                    dx += x - previousX;
                    dy += -(y - previousY);
            }

            previousX = x;
            previousY = y;
        }

        return true;
    }
}
