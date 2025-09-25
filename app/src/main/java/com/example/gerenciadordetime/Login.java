package com.example.gerenciadordetime;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Service.BuscaDadosUser;
import com.example.Service.LoginHelper;
import com.example.Service.TemaHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Login extends AppCompatActivity {
    private EditText emailEditText, senhaEditText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BuscaDadosUser buscadados = new BuscaDadosUser();
        buscadados.buscarIDTime();
        buscadados.buscarTime();
        buscadados.buscarFuncaoUsuario();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        Button btnLogin = findViewById(R.id.btnLogin);

        LoginHelper.configurarPularCampos(new EditText[]{emailEditText, senhaEditText}, btnLogin);

        int colorDefault = Color.GRAY;
        int colorError = Color.RED;
        int colorTextDefault = Color.BLACK;

        TemaHelper.aplicarTema(
                this,
                new TextView[]{emailEditText, senhaEditText},
                new TextView[]{btnLogin}
        );

        btnLogin.setOnClickListener(v -> {
            boolean camposOk = true;

            List<EditText> campos = Arrays.asList(emailEditText, senhaEditText);

            for (EditText campo : campos) {
                if (campo.getText().toString().trim().isEmpty()) {
                    campo.setBackgroundTintList(ColorStateList.valueOf(colorError));
                    campo.setTextColor(colorError);
                    campo.setHintTextColor(colorError);
                    camposOk = false;
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    campo.setBackgroundTintList(ColorStateList.valueOf(colorDefault));
                    campo.setTextColor(colorTextDefault);
                    campo.setHintTextColor(colorDefault);
                    campo.setError(null);
                }
            }

            if (camposOk) {
                loginUser();
            }
        });
    }
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
    }

    private void buscarFuncaoUsuarioEmail(String email, String senha) {
        db.collection("GTUSUARIOS")
                .whereEqualTo("EMAIL", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            // Se tem resultado, pega o primeiro documento
                            DocumentSnapshot usuario = task.getResult().getDocuments().get(0);

                            String tipoUsuario = usuario.getString("FUNCAO");
                            String senhaBanco = usuario.getString("SENHA");

                            if (senha.equals(senhaBanco)) {
                                Intent intent = new Intent(Login.this, Menu.class);
                                intent.putExtra("TIPOUSUARIO", tipoUsuario);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Nenhum usuário com esse email
                            Toast.makeText(this, "Email não encontrado, necessário logar online", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Erro ao buscar usuario", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro de conexão: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void buscarFuncaoUsuarioUid(String uid) {
        db.collection("GTUSUARIOS").document(uid).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String tipoUsuario = documentSnapshot.getString("FUNCAO");
                    String timeUsuario = documentSnapshot.getString("TIME");
                    String idTimeUsuario = documentSnapshot.getString("IDTIME");


                    // Passa o tipo para a tela de menu
                    Intent intent = new Intent(Login.this, Menu.class);
                    intent.putExtra("TIPOUSUARIO", tipoUsuario);
                    intent.putExtra("TIMEUSUARIO", timeUsuario);
                    intent.putExtra("IDTIMEUSUARIO", idTimeUsuario);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Usuário sem perfil definido", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Erro ao buscar perfil", Toast.LENGTH_SHORT).show();
            });
    }

}