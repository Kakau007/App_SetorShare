package KauaRodigo.com.br;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import KauaRodigo.com.br.adpter.AdpterPedidos;
import KauaRodigo.com.br.databinding.ActivityMeusPedidosBinding;
import KauaRodigo.com.br.helper.ConfiguracaoFireBase;
import KauaRodigo.com.br.helper.RecyclerItemClickListener;
import KauaRodigo.com.br.model.Pedido;
import KauaRodigo.com.br.model.UsuarioFirebase;
import dmax.dialog.SpotsDialog;

public class MeusPedidosActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMeusPedidosBinding binding;

    private RecyclerView recyclerPedidos;

    private List<Pedido> pedidos = new ArrayList<>();

    private AdpterPedidos adpterPedidos;

    private DatabaseReference pedidosUsuarioRef;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connfigurações iniciais

        pedidosUsuarioRef = ConfiguracaoFireBase.referenciaFirebase()
                .child("meus_pedidos")
                        .child(UsuarioFirebase.getIndetificadorUsuario());

        inicializarComponentes();

        binding = ActivityMeusPedidosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_meus_pedidos);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), CadastrarPedidosActivity.class));
            }

        });


        ImageView imgVoltarP = findViewById(R.id.imgVoltarP);
        imgVoltarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MeusPedidosActivity.this, MenuActivity.class);
                startActivity(in);
            }
        });

        // Configurar o RecyclerView
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));

        adpterPedidos = new AdpterPedidos(pedidos, this);
        recyclerPedidos.setAdapter(adpterPedidos);

        // recuperar pedidos
        recuperarPedidos();

        // adiciona evento de click no recyclerview

        recyclerPedidos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerPedidos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Pedido pedidoSelecionado = pedidos.get(position);
                                Context context = view.getContext(); // Obtenha o contexto a partir da visualização (View) fornecida

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Remover Pedido");
                                builder.setMessage("Tem certeza de que deseja remover o pedido?");
                                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        pedidoSelecionado.remover();
                                        adpterPedidos.notifyDataSetChanged();
                                    }
                                });
                                builder.setNegativeButton("Cancelar", null);

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }


                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );




    }

    private void recuperarPedidos(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando Pedidos...")
                .setCancelable(false)
                .build();
        dialog.show();

        pedidosUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pedidos.clear();
                for (DataSnapshot ds : snapshot.getChildren()){

                    pedidos.add(ds.getValue(Pedido.class));

                }

                Collections.reverse(pedidos);
                adpterPedidos.notifyDataSetChanged();

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void inicializarComponentes(){

        recyclerPedidos = findViewById(R.id.recyclerPedidos);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_meus_pedidos);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}