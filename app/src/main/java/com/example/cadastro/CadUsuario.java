package com.example.cadastro;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Objetos.Usuario;
import com.example.Service.ImagemHelper;
import com.example.Service.MaskTextWatcher;
import com.example.gerenciadordetime.R;
import com.example.info.InfoTermosUso;
import com.google.android.material.textfield.TextInputEditText;

public class CadUsuario extends AppCompatActivity {

    private TextInputEditText telefoneEditText;
    private CheckBox checkTermos;
    private Button btnProximo, btnTermos, btnVoltar;

    private EditText emailEditText, nomeEditText, senhaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_usuario);

        telefoneEditText = findViewById(R.id.telefoneEditText);
        telefoneEditText.addTextChangedListener(new MaskTextWatcher(telefoneEditText));

        bindViews();

        btnTermos.setPaintFlags(btnTermos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnProximo.setEnabled(false);

        ImagemHelper.aplicarImagemNoBotao(this, btnVoltar, R.drawable.btnvoltar, 70, 70);

        setupListeners();

    }

    private void bindViews() {
        btnTermos = findViewById(R.id.btnTermosUso);
        checkTermos = findViewById(R.id.checkGolContra);
        btnProximo = findViewById(R.id.btnProximo);
        btnVoltar = findViewById(R.id.btnVoltar);
        emailEditText = findViewById(R.id.emailEditText);
        nomeEditText = findViewById(R.id.nomeTimeEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
    }

    private void setupListeners() {

        btnTermos.setOnClickListener(v -> {
            Intent intent = new Intent(this, InfoTermosUso.class);
            startActivity(intent);
        });

        btnProximo.setOnClickListener(v -> {
//            if (verificaCampos()) {
                salvaDadosUsuario();
                Intent intent = new Intent(this, CadTime.class);
                startActivity(intent);
//            }else{
//                Toast.makeText(this, "Preencha os campos obrigatorios", Toast.LENGTH_SHORT).show();
//            }
        });

        checkTermos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnProximo.setEnabled(isChecked);
        });

        btnVoltar.setOnClickListener(v -> finish());

    }

    private void salvaDadosUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail(emailEditText.getText().toString());
        usuario.setNome(nomeEditText.getText().toString());
        usuario.setSenha(senhaEditText.getText().toString());
        usuario.setTelefone(telefoneEditText.getText().toString());
    }

//    public boolean verificaCampos() {
//        if (emailEditText.getText().toString().isEmpty() ||
//                telefoneEditText.getText().toString().isEmpty() ||
//                nomeEditText.getText().toString().isEmpty() ||
//                senhaEditText.getText().toString().isEmpty()) {
//            return false;
//            }else {
//            return true;
//        }
//
//    }
}