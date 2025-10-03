package com.example.Service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;

public class ImagemHelper {

    /**
     * Redimensiona uma imagem do drawable e aplica em um botão como drawable à esquerda.
     *
     * @param context    Contexto da Activity ou Fragment
     * @param botao      O botão que receberá a imagem
     * @param drawableId ID do drawable (ex: R.drawable.minha_imagem)
     * @param larguraMax Largura máxima em pixels
     * @param alturaMax  Altura máxima em pixels
     */
    public static void aplicarImagemNoBotao(Context context, Button botao, int drawableId, int larguraMax, int alturaMax) {
        // Lê o bitmap
        Bitmap bitmapOriginal = BitmapFactory.decodeResource(context.getResources(), drawableId);

        // Calcula proporção para manter aspecto
        float proporcao = Math.min(
                (float) larguraMax / bitmapOriginal.getWidth(),
                (float) alturaMax / bitmapOriginal.getHeight()
        );

        int larguraFinal = Math.round(bitmapOriginal.getWidth() * proporcao);
        int alturaFinal = Math.round(bitmapOriginal.getHeight() * proporcao);

        // Redimensiona
        Bitmap bitmapRedimensionado = Bitmap.createScaledBitmap(bitmapOriginal, larguraFinal, alturaFinal, true);

        // Aplica no botão à esquerda do texto
        botao.setCompoundDrawablesWithIntrinsicBounds(
                new BitmapDrawable(context.getResources(), bitmapRedimensionado),
                null, null, null
        );
    }
}
