package com.example.Eventos;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class BotaoUtils {

    public static void aplicarEfeitoPressionar(final Button botao) {
        botao.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                        v.getBackground().setAlpha(230); // leve brilho
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                        v.getBackground().setAlpha(255); // volta ao normal
                        break;
                }
                return false; // permite clique normal
            }
        });
    }
}
