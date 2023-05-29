package KauaRodigo.com.br;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import KauaRodigo.com.br.model.UsuarioFirebase;

public class MenuActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView imagePerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView textMeusPedidos = findViewById(R.id.textMeusPedidos);

        textMeusPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MenuActivity.this, MeusPedidosActivity.class);
                startActivity(in);
            }
        });

        imagePerfil = findViewById(R.id.imagePerfil);

        ImageView imgMenu = findViewById(R.id.imgVoltar);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MenuActivity.this, PrincipalActivity.class);
                startActivity(in);
            }
        });

        TextView textDeslogar = findViewById(R.id.textDesconectar);
        textDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerLogout();
            }
        });

        TextView textEditarPerfil = findViewById(R.id.textEditarPerfil);
        textEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MenuActivity.this, MeuPerfilActivity.class);
                startActivity(in);
            }
        });

        TextView campoUsuarioAtual = findViewById(R.id.textUsuarioAtual);
        TextView campoEmailAtual = findViewById(R.id.textEmailAtual);

        mAuth = FirebaseAuth.getInstance();

        // Obter o usuário atualmente logado
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        campoUsuarioAtual.setText(usuarioPerfil.getDisplayName());
        campoEmailAtual.setText(usuarioPerfil.getEmail());

        // Recuperar a foto do usuário logado
        Uri urlFoto = usuarioPerfil.getPhotoUrl();
        if (urlFoto != null) {
            Glide.with(MenuActivity.this)
                    .load(urlFoto)
                    .into(imagePerfil);
        } else {
            // Caso não tenha foto, exiba uma imagem padrão
            imagePerfil.setImageResource(R.drawable.baseline_foto_usuario);
        }

        String email = usuarioPerfil.getEmail();
        if (email != null) {
            String[] parts = email.split("@");
            if (parts.length > 0) {
                String username = parts[0];
                campoUsuarioAtual.setText(username);
            } else {
                campoUsuarioAtual.setText("Nome do usuário não disponível");
            }
        } else {
            campoUsuarioAtual.setText("Nome do usuário não disponível");
        }
    }

    private void fazerLogout() {
        // Fazer logout do usuário
        mAuth.signOut();

        // Exibir mensagem de deslogado com sucesso
        Toast.makeText(MenuActivity.this, "Deslogado com sucesso", Toast.LENGTH_SHORT).show();

        // Redirecionar para a tela de login
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Fecha a atividade do menu para evitar que o usuário volte para ela após o logout
    }

}

