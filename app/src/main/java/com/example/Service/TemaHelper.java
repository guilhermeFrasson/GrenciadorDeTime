package com.example.Service;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

public class TemaHelper {

        public static boolean isModoEscuro(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }

    /**
     * Aplica cores de acordo com o modo (escuro ou claro)
     *
     * @param context Contexto da Activity ou Fragment
     * @param textos Array de TextViews que terão a cor alterada
     */
    public static void aplicarTema(Context context, TextView[] textos, TextView[] Botao) {
        if (isModoEscuro(context)) {
            // Modo escuro → textos e imagens em branco
            for (TextView t : textos) {
                t.setTextColor(Color.WHITE);
            }
            for (TextView t : Botao) {
                t.setTextColor(Color.BLACK);
            }
        } else {
            // Modo claro → textos e imagens em preto
            for (TextView t : textos) {
                t.setTextColor(Color.BLACK);
            }
            for (TextView t : Botao) {
                t.setTextColor(Color.WHITE);
            }
        }
    }
}