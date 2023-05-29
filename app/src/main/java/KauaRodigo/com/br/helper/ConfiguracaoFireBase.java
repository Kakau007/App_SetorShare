package KauaRodigo.com.br.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.grpc.Context;

public class ConfiguracaoFireBase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth referenciaAutenticacao;

    private static StorageReference storage;

    public static String getIdUsuario(){
        FirebaseAuth autenticacao = getReferenciaAutenticacao();

        return autenticacao.getCurrentUser().getUid();
    }

    // Retorna a instancia do FirebaseAuth
    public static FirebaseAuth getReferenciaAutenticacao(){
        if(referenciaAutenticacao == null){
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }


    // Retorna a referencai do FireBase
    public static DatabaseReference referenciaFirebase(){
        if(referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static StorageReference getFirebaseStorage(){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return  storage;
    }



}
