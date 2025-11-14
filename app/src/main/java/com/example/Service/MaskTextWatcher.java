package com.example.Service;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskTextWatcher implements TextWatcher {

    private final EditText editText;
    private boolean isUpdating = false;
    // A máscara que queremos: (99) 99999-9999
    private final String mask = "(##) #####-####";

    public MaskTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não precisamos fazer nada aqui
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não precisamos fazer nada aqui
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isUpdating) {
            return;
        }

        isUpdating = true;

        // 1. Remove todos os caracteres que não são dígitos
        String digitsOnly = s.toString().replaceAll("[^\\d]", "");

        String formatted = "";
        int digitIndex = 0;

        // 2. Constrói a string formatada baseada na máscara
        for (char maskChar : mask.toCharArray()) {
            if (digitIndex >= digitsOnly.length()) {
                break; // Se não houver mais dígitos, para de formatar
            }

            if (maskChar == '#') {
                formatted += digitsOnly.charAt(digitIndex);
                digitIndex++;
            } else {
                formatted += maskChar;
            }
        }

        // 3. Atualiza o texto no EditText e posiciona o cursor no final
        editText.setText(formatted);
        editText.setSelection(formatted.length());

        isUpdating = false;
    }
}