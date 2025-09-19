package com.example.Service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PopupUtils {
    public static void mostrarConfirmacao(Context context, String titulo, String mensagem,
                                          DialogInterface.OnClickListener onConfirmar) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        builder.setPositiveButton("Confirmar", onConfirmar);

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // só fecha o popup
            }
        });

        builder.show();
    }
}
