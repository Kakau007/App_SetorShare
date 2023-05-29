package KauaRodigo.com.br;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import KauaRodigo.com.br.adpter.AdpterPedidos;
import KauaRodigo.com.br.helper.ConfiguracaoFireBase;
import KauaRodigo.com.br.helper.RecyclerItemClickListener;
import KauaRodigo.com.br.model.Pedido;
import dmax.dialog.SpotsDialog;

public class PrincipalActivity extends AppCompatActivity {


    private RecyclerView recyclerViewPedidosPublicos;

    private Button bntSetor, bntCategoriaP;

    private AdpterPedidos adpterPedidos;

    private List<Pedido> Listapedidos = new ArrayList<>();

    private DatabaseReference pedidosPublicosRef;

    private AlertDialog dialog;

    private String filtroSetor = "";

    private String filtroCategoria = "";

    private boolean filtrandoPorSetor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        ImageView imgMenu = findViewById(R.id.imgVoltarFinal);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(PrincipalActivity.this, MenuActivity.class);
                startActivity(in);
            }
        });


        pedidosPublicosRef = ConfiguracaoFireBase.referenciaFirebase()
                        .child("pedidos");

        inicializarComponentes();

        // Configurar o RecyclerView
        recyclerViewPedidosPublicos = findViewById(R.id.recyclerPedidosPublicos);
        recyclerViewPedidosPublicos.setLayoutManager(new LinearLayoutManager(this));
        adpterPedidos = new AdpterPedidos(Listapedidos, this);
        recyclerViewPedidosPublicos.setAdapter(adpterPedidos);


        recuperarPedidosPublicos();

        // APlicar evento de clique

        recyclerViewPedidosPublicos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewPedidosPublicos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Pedido pedidoSelecionado = Listapedidos.get(position);

                                Intent i = new Intent(PrincipalActivity.this, DetalhesPedidosActivity.class);
                                i.putExtra("pedidoSelecionado", pedidoSelecionado);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    public void filtrarPorSetor(View view){

        AlertDialog.Builder dialogSetor = new AlertDialog.Builder(this);
        dialogSetor.setTitle("Selecione o setor desejado");



        // configurar spinner do setor
        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

        Spinner spinnerSetor = viewSpinner.findViewById(R.id.spinnerFiltroSetor);

        String[] setor = getResources().getStringArray(R.array.setor);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                setor
        );
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerSetor.setAdapter( adapter );


        dialogSetor.setView(viewSpinner);


        dialogSetor.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                filtroSetor = spinnerSetor.getSelectedItem().toString();

                recuperarPedidosPorSetor();

                filtrandoPorSetor = true;

            }
        });

        dialogSetor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog dialog = dialogSetor.create();
        dialog.show();
    }

    public void filtrarPorCategoria(View view){

        if(filtrandoPorSetor == true ){

            AlertDialog.Builder dialogSetor = new AlertDialog.Builder(this);
            dialogSetor.setTitle("Selecione a categoria desejada");



            // configurar spinner do setor
            View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

            final Spinner spinnerCategoria = viewSpinner.findViewById(R.id.spinnerFiltroSetor);

            String[] setor = getResources().getStringArray(R.array.categoria);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    setor
            );
            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter( adapter );


            dialogSetor.setView(viewSpinner);


            dialogSetor.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    filtroCategoria = spinnerCategoria.getSelectedItem().toString();

                    recuperarPedidosPorCategoria();




                }
            });

            dialogSetor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            AlertDialog dialog = dialogSetor.create();
            dialog.show();
        } else {

            Toast.makeText(this , "Escolha primeiro um setor!",
            Toast.LENGTH_SHORT).show();
        }


    }

    public void recuperarPedidosPorSetor(){


        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando Pedidos por setor...")
                .setCancelable(false)
                .build();
        dialog.show();

        pedidosPublicosRef = ConfiguracaoFireBase.referenciaFirebase()
                .child("pedidos")
                .child(filtroSetor);

        pedidosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Listapedidos.clear();
                for(DataSnapshot categorias: snapshot.getChildren()){

                    for(DataSnapshot pedidos: categorias.getChildren()){

                        Pedido pedido = pedidos.getValue(Pedido.class);
                        Listapedidos.add(pedido);


                    }
                }

                Collections.reverse(Listapedidos);
                adpterPedidos.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void recuperarPedidosPorCategoria(){


        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando Pedidos por categoria...")
                .setCancelable(false)
                .build();
        dialog.show();

        pedidosPublicosRef = ConfiguracaoFireBase.referenciaFirebase()
                .child("pedidos")
                .child(filtroSetor)
                .child(filtroCategoria);

        pedidosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Listapedidos.clear();
                for(DataSnapshot pedidos: snapshot.getChildren()){

                    Pedido pedido = pedidos.getValue(Pedido.class);
                    Listapedidos.add(pedido);
                }

                Collections.reverse(Listapedidos);
                adpterPedidos.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void recuperarPedidosPublicos(){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando Pedidos...")
                .setCancelable(false)
                .build();
        dialog.show();

        Listapedidos.clear();
        pedidosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot setores: snapshot.getChildren()){

                    for(DataSnapshot categorias: setores.getChildren()){

                        for(DataSnapshot pedidos: categorias.getChildren()){

                            Pedido pedido = pedidos.getValue(Pedido.class);
                            Listapedidos.add(pedido);
                            dialog.dismiss();


                        }
                    }

                }

                Collections.reverse(Listapedidos);
                adpterPedidos.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void inicializarComponentes(){

        recyclerViewPedidosPublicos = findViewById(R.id.recyclerPedidosPublicos);

    }


}