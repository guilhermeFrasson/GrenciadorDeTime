package com.example.cadastro;

import static com.example.Objetos.Time.getNomeTime;
import static com.example.Service.BuscaDadosTime.proximoCodigo;
import static com.example.Service.FormataString.formatarNome;
import static com.example.gerenciadordetime.Menu.idTimeUsuario;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadUsuario extends AppCompatActivity {

    private TextInputEditText telefoneEditText;
    private CheckBox checkTermos;
    private Button btnProximo, btnTermos, btnVoltar;
    private EditText emailEditText, nomeEditText, senhaEditText;
    Usuario usuario = new Usuario();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            if (!verificaCampos()) {
                Toast.makeText(this, "Preencha os campos obrigatorios", Toast.LENGTH_SHORT).show();
            }else if (!verificaEmail()) {
                Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
            }else if (!verificaTelefone()) {
                Toast.makeText(this, "Telefone inválido!", Toast.LENGTH_SHORT).show();
            }else if (!verificaSenha()){
                Toast.makeText(this, "Senha inválida!", Toast.LENGTH_SHORT).show();
            } else {
                salvaDadosUsuario();
                Intent intent = new Intent(this, CadTime.class);
                startActivity(intent);
            }
        });

        checkTermos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnProximo.setEnabled(isChecked);
        });

        btnVoltar.setOnClickListener(v -> finish());

    }

    private void salvaDadosUsuario() {
        usuario.setEmail(emailEditText.getText().toString());
        usuario.setNome(nomeEditText.getText().toString());
        usuario.setSenha(senhaEditText.getText().toString());
        usuario.setTelefone(telefoneEditText.getText().toString());
    }

    public boolean verificaCampos() {
        if (emailEditText.getText().toString().isEmpty() ||
                telefoneEditText.getText().toString().isEmpty() ||
                nomeEditText.getText().toString().isEmpty() ||
                senhaEditText.getText().toString().isEmpty()) {
            return false;
            }else {
            return true;
        }
    }

    public boolean verificaEmail (){
        String email = emailEditText.getText().toString().trim();

        if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verificaTelefone(){
        String telefone = telefoneEditText.getText().toString().trim();

        if (telefone.matches("^\\(?\\d{2}\\)? ?9?\\d{4}-?\\d{4}$")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verificaSenha(){
        String senha = senhaEditText.getText().toString().trim();
        int qtdCaracteresSenha = senha.length();

        if (qtdCaracteresSenha >= 6) {
            return true;
        } else {
            return false;
        }
    }

//    public void criarUsuarioAuth(String email, String senha, String primeiroAcesso) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//
//        auth.createUserWithEmailAndPassword(email, senha)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = auth.getCurrentUser();
//                        if (user != null) {
//                            String uid = user.getUid();
//
//                            if ("SIM".equals(primeiroAcesso)) {
//                                salvaDadosUsuarioPrimeiroAcesso(uid);
//                            } else {
//                                salvaDadosUsuarioCadJogador(uid);
//                            }
//                        }
//                    } else {
//                        Log.e("APP", "Erro ao criar usuário: " + task.getException().getMessage());
//                    }
//                });
//    }

    public void salvaDadosUsuarioPrimeiroAcesso(String uid) {

        Map<String, Object> infoUsuario = new HashMap<>();

        infoUsuario.put("NOME", usuario.getNome());
        infoUsuario.put("EMAIL", usuario.getEmail());
        infoUsuario.put("SENHA", usuario.getSenha());
        infoUsuario.put("TIME", getNomeTime());
        infoUsuario.put("IDTIME", proximoCodigo);
        infoUsuario.put("FUNCAO", "Administrador");

        db.collection("GTUSUARIOS").document(uid).set(infoUsuario);
    }

    public void salvaDadosUsuarioCadJogador(String uid) {
        Map<String, Object> infoUsuario = new HashMap<>();

        String email = formatarNome(nomeEditText.getText().toString().trim()) + idTimeUsuario + "@gmail.com";

        infoUsuario.put("NOME", formatarNome(nomeEditText.getText().toString().trim()));
        infoUsuario.put("EMAIL", email);
        infoUsuario.put("SENHA", "123456");
        infoUsuario.put("IDTIME", idTimeUsuario);
        infoUsuario.put("FUNCAO", "Jogador");

        db.collection("GTUSUARIOS").document(uid).set(infoUsuario);
    }

//    private void loginUser(String email, String senha) {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//        mAuth.signInWithEmailAndPassword(email, senha)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Intent intent = new Intent(CadUsuario.this, Menu.class);
//                        intent.putExtra("PRIMEIROACESSO", "SIM");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    }
//                });
//    }
}