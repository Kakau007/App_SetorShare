package KauaRodigo.com.br.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import KauaRodigo.com.br.helper.ConfiguracaoFireBase;

public class Pedido implements Serializable {

    private String idPedido;
    private String setor;
    private String categoria;
    private String nome;
    private String codigo;
    private String quantidade;
    private String descricao;

    private String dataAtual;

    private List<String> fotos;


    public Pedido() {

        DatabaseReference pedidoRef = ConfiguracaoFireBase.referenciaFirebase()
                .child("meus_pedidos");

        setIdPedido(pedidoRef.push().getKey());

    }

    public void salvar(){

        String idUsuario = UsuarioFirebase.getIndetificadorUsuario();

        DatabaseReference pedidoRef = ConfiguracaoFireBase.referenciaFirebase()
                .child("meus_pedidos");

        pedidoRef.child(idUsuario)
                .child(getIdPedido())
                .setValue(this);

        salvarPedidoPublico();

    }

    public void salvarPedidoPublico(){


        DatabaseReference pedidoRef = ConfiguracaoFireBase.referenciaFirebase()
                .child("pedidos");

        pedidoRef.child(getSetor())
                .child(getCategoria())
                .child(getIdPedido())
                .setValue(this);

    }

    public void remover (){

        String idUsuario = UsuarioFirebase.getIndetificadorUsuario();

        DatabaseReference pedidoRef = ConfiguracaoFireBase.referenciaFirebase()
                .child("meus_pedidos")
                .child(idUsuario)
                .child(getIdPedido());

        pedidoRef.removeValue();
        removerPedidoPublico();

    }

    public void removerPedidoPublico (){

        DatabaseReference pedidoRef = ConfiguracaoFireBase.referenciaFirebase()
                .child("pedidos")
                .child(getSetor())
                .child(getCategoria())
                .child(getIdPedido());

        pedidoRef.removeValue();

    }


    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }


    public String getData() {
        return dataAtual;
    }

    public void setData(String data) {
        this.dataAtual = data;
    }


}
