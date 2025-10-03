package com.example.gerenciadordetime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Service.LoginHelper;
import com.example.cadastro.CadUsuario;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class Login extends AppCompatActivity {
    private EditText emailEditText, senhaEditText;
    private FirebaseAuth mAuth;
    private TextInputLayout emailLayout, senhaLayout;
    private Button btnLogin, btnNovoCadastro;
    View overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configuração inicial do Remote Config
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // busca a cada 1h (ajuste se quiser)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        // Valores padrão (se der erro no fetch)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        // Busca valores do servidor
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkForUpdate(mFirebaseRemoteConfig);
                    }
                    // Se falhar, o fluxo continua normalmente. Não é preciso fazer nada no 'else'.
                });

        // Inicialize o Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind dos componentes da UI
        bindViews();

        // Configuração dos listeners
        setupListeners();

        // Utilitário para pular campos (mantido como está)
        LoginHelper.configurarPularCampos(new EditText[]{emailEditText, senhaEditText}, btnLogin);

    }

    private void bindViews() {
        emailEditText = findViewById(R.id.emailEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        emailLayout = findViewById(R.id.emailLayout);
        senhaLayout = findViewById(R.id.senhaLayout);
        btnLogin = findViewById(R.id.btnLogin);
        btnNovoCadastro = findViewById(R.id.btnNovoCadastro);
        overlay = findViewById(R.id.progressOverlay);

    }

    private void setupListeners() {
        // Limpa o erro ao digitar no campo de email
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailLayout.getError() != null) {
                    emailLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Limpa o erro ao digitar no campo de senha
        senhaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (senhaLayout.getError() != null) {
                    senhaLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnLogin.setOnClickListener(v -> {
            esconderTeclado();
            if (validarCampos()) {
                loginUser(emailEditText.getText().toString().trim(), senhaEditText.getText().toString().trim());
            }
        });

        btnNovoCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(this, CadUsuario.class);
            startActivity(intent);
        });
    }

    private boolean validarCampos() {
        boolean isValid = true;
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailLayout.setError("Preencha o email");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        if (senha.isEmpty()) {
            senhaLayout.setError("Preencha a senha");
            isValid = false;
        } else {
            senhaLayout.setError(null);
        }

        return isValid;
    }

    private void loginUser(String email, String senha) {
        // Mostra o progresso e desabilita os botões para evitar múltiplos cliques
        setUiLoading(true);

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    // Ao final, reabilita a UI
                    setUiLoading(false);

                    if (task.isSuccessful()) {
                        // Login bem-sucedido, navega para o Menu
                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent);
                        finish(); // Fecha a tela de login
                    } else {
                        // Erro no login. Mostra um feedback mais claro.
                        emailLayout.setError(" "); // Não recomendado
                        senhaLayout.setError("Email ou senha inválidos");
                    }
                });
    }

    private void setUiLoading(boolean isLoading) {
        if (isLoading) {
            overlay.setVisibility(View.VISIBLE); // Use a constante correta
            btnLogin.setEnabled(false);
            btnNovoCadastro.setEnabled(false);
        } else {
            overlay.setVisibility(View.GONE); // Use a constante correta
            btnLogin.setEnabled(true);
            btnNovoCadastro.setEnabled(true);
        }
    }

    private void esconderTeclado() {
        // Método para esconder o teclado
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkForUpdate(FirebaseRemoteConfig remoteConfig) {
        long minVersion = remoteConfig.getLong("min_version");
        String updateUrl = remoteConfig.getString("update_url");

        // CORRIGIDO: Chamando o método que busca a versão corretamente.
        int currentVersion = getAppVersionCode();

        // -1 indica que houve um erro ao obter a versão, então pulamos a verificação.
        if (currentVersion != -1 && currentVersion < minVersion) {
            new AlertDialog.Builder(this)
                    .setTitle("Atualização obrigatória")
                    // MENSAGEM AJUSTADA PARA O DOWNLOAD DIRETO
                    .setMessage("Uma nova versão está disponível. O download começará agora. Após terminar, abra o arquivo baixado para instalar a atualização.")
                    .setCancelable(false)
                    .setPositiveButton("Baixar Atualização", (dialog, which) -> { // Texto do botão mais claro
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                        startActivity(intent);
                        finish();
                    })
                    .show();
        }
        // CORRIGIDO: O 'else' foi removido, pois se não há atualização,
        // nenhuma ação é necessária. O usuário já está na tela de login.
    }

    private int getAppVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Isso raramente acontece, mas é bom tratar
            e.printStackTrace();
            return -1; // Retorna um valor inválido em caso de erro
        }
    }

    private void goToLogin() {
        startActivity(new Intent(this, Login.class));
        finish();
    }
}