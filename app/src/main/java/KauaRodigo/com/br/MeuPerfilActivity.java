package KauaRodigo.com.br;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import KauaRodigo.com.br.helper.ConfiguracaoFireBase;
import KauaRodigo.com.br.model.Usuario;
import KauaRodigo.com.br.model.UsuarioFirebase;

public class MeuPerfilActivity extends AppCompatActivity {


    private Usuario usuarioLogado;
    private static final int SELECAO_GALERIA = 200;

    private StorageReference storageRef;

    private String identificadorUsuario;




    ImageView imageEditarPerfil;
    TextView alterarFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        storageRef = ConfiguracaoFireBase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIndetificadorUsuario();

        ImageView imgMenu = findViewById(R.id.imgVoltarMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MeuPerfilActivity.this, MenuActivity.class);
                startActivity(in);
            }
        });


        imageEditarPerfil = findViewById(R.id.imagePerfil);
        EditText editNomePerfil = findViewById(R.id.editNomePerfil);
        EditText editEmailPerfil = findViewById(R.id.editEmailPerfil);
        Button bntSalvarAlteracoes = findViewById(R.id.bntSalvarAlterações);
        alterarFoto = findViewById(R.id.textAlterarFoto);


        // Obter o usuário atualmente logado
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        editNomePerfil.setText(usuarioPerfil.getDisplayName());
        editEmailPerfil.setText(usuarioPerfil.getEmail());

        //recuperar a foto
        Uri url = usuarioPerfil.getPhotoUrl();
        if(url != null){

            Glide.with(MeuPerfilActivity.this)
                    .load(url)
                    .into(imageEditarPerfil);
        } else{
            imageEditarPerfil.setImageResource(R.drawable.baseline_foto_usuario);
        }

        String email = usuarioPerfil.getEmail();
        if (email != null) {
            String[] parts = email.split("@");
            if (parts.length > 0) {
                String username = parts[0];
                editNomePerfil.setText(username);
            } else {
                editNomePerfil.setText("Nome do usuário não disponível");
            }
        } else {
            editNomePerfil.setText("Nome do usuário não disponível");
        }

        bntSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeAtualizado = editNomePerfil.getText().toString();

                //atualizar nome no perfil
                UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);

                //atualizar  o nome direto no banco
                usuarioLogado.setNome(nomeAtualizado);
                usuarioLogado.atualizar();
            }
        });

        // alterar foto
        alterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, SELECAO_GALERIA);

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            Bitmap imagem = null;

            try {
                // selecão apenas da galeria
                switch (requestCode){
                    case SELECAO_GALERIA:
                    Uri localImagemSelecionada = data.getData();
                    imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                    break;
                }
                // caso tenha escolhido uma imagem
                if (imagem != null){

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                  imageEditarPerfil.setImageBitmap(imagem);
                    final StorageReference imagemRef =storageRef
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + ".jpeg>");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MeuPerfilActivity.this, "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Recuperar o local da foto
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                   Uri url = task.getResult();
                                   atualizarfotoUsuario(url);
                                }
                            });




                            Toast.makeText(MeuPerfilActivity.this, "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void atualizarfotoUsuario(Uri url){

        UsuarioFirebase.atualizarFotoUsuario(url);

        usuarioLogado.setCaminhoFoto(url.toString());
        usuarioLogado.atualizar();

        Toast.makeText(MeuPerfilActivity.this, "Sua foto foi alterada com sucesso!",
                Toast.LENGTH_SHORT).show();

    }
}