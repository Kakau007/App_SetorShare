package KauaRodigo.com.br.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import KauaRodigo.com.br.R;
import KauaRodigo.com.br.model.Pedido;

public class AdpterPedidos extends RecyclerView.Adapter<AdpterPedidos.MyViewHolder> {




    private List<Pedido> pedidos;
    private Context context;

    public AdpterPedidos(List<Pedido> pedidos, Context context) {
        this.pedidos = pedidos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_pedidos, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Pedido pedido = pedidos.get(position);
        holder.setor.setText(pedido.getSetor());
        holder.nome.setText(pedido.getNome());
        holder.codigo.setText(pedido.getCodigo());
        holder.quantidade.setText(pedido.getQuantidade());

        // pegar a imagem da lista

        List<String> urlFotos = pedido.getFotos();
        String urlCapa = urlFotos.get(0);

        Picasso.get().load(urlCapa).into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public  class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView setor;
        TextView nome;
        TextView codigo;

        TextView quantidade;

        ImageView foto;

        public MyViewHolder(View itemView){
            super(itemView);

            setor = itemView.findViewById(R.id.textNomeSetor);
            nome = itemView.findViewById(R.id.textNomePedido);
            codigo = itemView.findViewById(R.id.textCodPedido);
            quantidade = itemView.findViewById(R.id.textQuantidade);
            foto = itemView.findViewById(R.id.imagePedido);

        }

    }


}
