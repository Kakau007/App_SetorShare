package KauaRodigo.com.br;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.w3c.dom.Text;

import java.net.URLEncoder;

import KauaRodigo.com.br.model.Pedido;

public class DetalhesPedidosActivity extends AppCompatActivity {


    private CarouselView carouselView;

    private TextView nome;

    private TextView quantidade;

    private TextView codigo;

    private TextView setor;

    private TextView descricao;

    private Pedido pedidoSelecionado;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pedidos);

        inicializarComponentes();

        // recuperar dados do pedido

        pedidoSelecionado = (Pedido) getIntent().getSerializableExtra("pedidoSelecionado");

        if (pedidoSelecionado != null){

            nome.setText(pedidoSelecionado.getNome());
            quantidade.setText((pedidoSelecionado.getQuantidade()));
            codigo.setText(pedidoSelecionado.getCodigo());
            setor.setText(pedidoSelecionado.getSetor());
            descricao.setText(pedidoSelecionado.getDescricao());


            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {

                    String urlString = pedidoSelecionado.getFotos().get(position);
                    Picasso.get().load(urlString).into(imageView);
                }
            };

            carouselView.setPageCount(pedidoSelecionado.getFotos().size());
            carouselView.setImageListener(imageListener);

        }

        ImageView imgVoltarPrincipal = findViewById(R.id.imgVoltarFinal);
        imgVoltarPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(DetalhesPedidosActivity.this, PrincipalActivity.class);
                startActivity(in);
            }
        });

    }

    public void visualizarNumeroWhatsApp(View view) {
        String numeroTelefone = "82991305810"; // Substitua pelo número de telefone desejado
        String mensagem = "Olá! Este é um teste do mais novo aplicativo desenvolvido por Kauã Rodrigo de Lima Barbosa"; // Mensagem pré-definida

        // Verifica se o WhatsApp está instalado no dispositivo
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            // Adiciona o número de telefone e a mensagem no formato "https://api.whatsapp.com/send?phone=XXXXXXXXXX&text=Mensagem"
            String url = "https://api.whatsapp.com/send?phone=" + numeroTelefone + "&text=" + URLEncoder.encode(mensagem, "UTF-8");
            intent.setData(Uri.parse(url));
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent);
            } else {
                // WhatsApp não está instalado, trate o caso adequadamente
                Toast.makeText(this, "WhatsApp não está instalado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void inicializarComponentes(){
        carouselView = findViewById(R.id.carouselView);
        nome = findViewById(R.id.textNomeDetalhes);
        quantidade = findViewById(R.id.textQuantidadeDetalhes);
        codigo = findViewById(R.id.textCodigoDetalhes);
        setor = findViewById(R.id.textSetorDetalhes);
        descricao = findViewById(R.id.textDescricaoDetalhes);


    }


}