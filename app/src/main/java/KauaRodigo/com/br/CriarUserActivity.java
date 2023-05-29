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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.ktx.Firebase;

import KauaRodigo.com.br.helper.ConfiguracaoFireBase;
import KauaRodigo.com.br.model.Usuario;
import KauaRodigo.com.br.model.UsuarioFirebase;

public class CriarUserActivity extends AppCompatActivity {



    // Definindo os campos e botão para realizar o cadastro
    EditText textCampoNome;
    EditText textCampoEmail;
    EditText textCampoMatricula;
    EditText textCampoSenha;
    Button bntCadastro;

    //Definindo o link para voltar a tela de login
    TextView tenhoConta;

    // criando um atributo para chamar o usuário
    private Usuario usuario;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_user);

        // inicializando os campos e botão
        textCampoNome = findViewById(R.id.campoNome);
        textCampoEmail = findViewById(R.id.campoEmail);
        textCampoMatricula = findViewById(R.id.campoMatricula);
        textCampoSenha = findViewById(R.id.campoSenha);
        bntCadastro = findViewById(R.id.bntCadastrar);

        //Definindo o link para voltar a tela de login
        tenhoConta = findViewById(R.id.textTenhoConta);


        tenhoConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CriarUserActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });

        bntCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textNome = textCampoNome.getText().toString();
                String textEmail = textCampoEmail.getText().toString();
                String textMatricula = textCampoMatricula.getText().toString();
                String textSenha = textCampoSenha.getText().toString();

                if( !textNome.isEmpty()) {
                    if( !textEmail.isEmpty()) {
                        if( !textMatricula.isEmpty()) {
                            if( !textSenha.isEmpty()) {

                                usuario = new Usuario();
                                usuario.setNome(textNome);
                                usuario.setEmail(textEmail);
                                usuario.setMatricula(textMatricula);
                                usuario.setSenha(textSenha);
                                cadastrar(usuario);

                            } else {
                                Toast.makeText(CriarUserActivity.this,
                                        "Preencha a Senha!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CriarUserActivity.this,
                                    "Preencha a Matrícula!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CriarUserActivity.this,
                                "Preencha o Email!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CriarUserActivity.this,
                            "Preencha o Nome!",
                            Toast.LENGTH_SHORT).show();
                }



            }

            // Método resposável por cadastrar usuário
            public void cadastrar(Usuario usuario){

                autenticacao = ConfiguracaoFireBase.getReferenciaAutenticacao();
                autenticacao.createUserWithEmailAndPassword(
                        usuario.getEmail(),
                        usuario.getSenha()
                ).addOnCompleteListener(
                        CriarUserActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if(task.isSuccessful()){


                                    try {


                                        String idUsuario = task.getResult().getUser().getUid();
                                        usuario.setId(idUsuario);
                                        usuario.salvar();

                                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());



                                        Toast.makeText( CriarUserActivity.this,
                                                "Cadastrado com sucesso",
                                                Toast.LENGTH_SHORT).show();



                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        finish();

                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }


                                } else {
                                    String erroExcecao = "";
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e){
                                        erroExcecao = "Digite uma senha mais forte !";
                                    } catch (FirebaseAuthInvalidCredentialsException e){
                                        erroExcecao = "Por favor, Digite um e-mail válido";
                                    } catch (FirebaseAuthUserCollisionException e){
                                        erroExcecao = "Essa conta já foi cadastrada";
                                    } catch (Exception e){
                                        erroExcecao = "ao cadastrar usuário:" + e.getMessage();
                                        e.printStackTrace();
                                    }

                                    Toast toast = Toast.makeText(CriarUserActivity.this, "Erro " +
                                                    erroExcecao, Toast.LENGTH_SHORT);
                                    TextView textView = toast.getView().findViewById(android.R.id.message);
                                    if (textView != null) {
                                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    }
                                    toast.show();
                                }
                            }
                        }
                );
            }

        });



    }
}