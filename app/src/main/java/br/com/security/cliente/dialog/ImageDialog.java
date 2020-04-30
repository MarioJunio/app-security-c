package br.com.security.cliente.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import br.com.security.cliente.activities.R;

public class ImageDialog extends Dialog {

    private GestureDetector gestureDetector;

    private Bitmap bitmap;
    private ImageView fotoCapturada;

    public ImageDialog(@NonNull Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(getContext(), new GestureListener());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.checkin_foto);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.9));

        initWidgets();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void initWidgets() {
        fotoCapturada = findViewById(R.id.foto);
        fotoCapturada.setImageBitmap(bitmap);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            dismiss();
            return true;
        }
    }

}