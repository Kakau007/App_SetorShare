package KauaRodigo.com.br.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import KauaRodigo.com.br.helper.ConfiguracaoFireBase;

public class Usuario {

    private String nome , id;
    private String email;
    private String matricula;
    private String senha;
    private String caminhoFoto;

    // construtor
    public Usuario() {

    }

    public void salvar(){
        DatabaseReference FirebaseRef = ConfiguracaoFireBase.referenciaFirebase();
        DatabaseReference usuariosRef = FirebaseRef.child("usuarios").child(getId());
        usuariosRef.setValue( this );
    }

    public void atualizar(){
        DatabaseReference firebaseRef = ConfiguracaoFireBase.referenciaFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(getId());

        Map<String, Object> valoresUsuario = converterParaMap();
        usuarioRef.updateChildren(valoresUsuario);
    }

    public Map<String, Object> converterParaMap(){
        HashMap<String, Object>usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("id", getId());
        usuarioMap.put("caminhoFoto", getCaminhoFoto());

        return usuarioMap;
    }

    // Gets e Sets


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
