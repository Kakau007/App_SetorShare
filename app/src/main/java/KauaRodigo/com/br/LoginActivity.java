package KauaRodigo.com.br;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import KauaRodigo.com.br.helper.ConfiguracaoFireBase;
import KauaRodigo.com.br.model.Usuario;


public class LoginActivity extends AppCompatActivity {

    // Definindo os campos e botão para realizar o cadastro
    private EditText campoEmail, campoSenha;
    private TextView semConta;
    private Button bntEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // inicializando os campos e botão
        semConta = findViewById(R.id.textCriarConta);
        campoEmail = findViewById(R.id.loginEmail);
        campoSenha = findViewById(R.id.loginSenha);
        bntEntrar = findViewById(R.id.bntEntrar);

        //Texto clicável para o usuário ir para tela de criar uma conta
        semConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, CriarUserActivity.class);
                startActivity(in);
            }
        });


        bntEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmail = campoEmail.getText().toString();
                String textSenha = campoSenha.getText().toString();

                if( !textEmail.isEmpty()) {
                    if( !textSenha.isEmpty()) {

                        usuario = new Usuario();
                        usuario.setEmail( textEmail);
                        usuario.setSenha( textSenha);
                        validarLogin(usuario);

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha a Senha !",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o e-mail !",
                            Toast.LENGTH_SHORT).show();
                }
            }

            public void validarLogin(Usuario usuario){

                autenticacao = ConfiguracaoFireBase.getReferenciaAutenticacao();

                autenticacao.signInWithEmailAndPassword(
                        usuario.getEmail(),
                        usuario.getSenha()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            startActivity(new Intent(getApplicationContext(), PrincipalActivity.class));
                            finish();

                        } else {

                          Toast.makeText(LoginActivity.this,
                                  "Erro ao fazer login",
                                  Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }

        });




    }
}