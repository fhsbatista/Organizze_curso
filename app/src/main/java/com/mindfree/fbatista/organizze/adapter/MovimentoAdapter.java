package com.mindfree.fbatista.organizze.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindfree.fbatista.organizze.R;
import com.mindfree.fbatista.organizze.model.Movimentacao;

import java.util.List;

/**
 * Created by fbatista on 24/03/18.
 */

public class MovimentoAdapter extends RecyclerView.Adapter<MovimentoAdapter.MyViewHolder>{

    private Context context;
    private List<Movimentacao> movimentos;

    public MovimentoAdapter(Context context, List<Movimentacao> movimentos){
        this.context = context;
        this.movimentos = movimentos;
    };


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movimento_item
            , parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Movimentacao movimentacao = movimentos.get(position);

        holder.mDescricao.setText(movimentacao.getDescricao());
        holder.mCategoria.setText(movimentacao.getCategoria());

        if(movimentacao.getTipo().equals("d")){
            holder.mValor.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.mValor.setText("- R$" + movimentacao.getValor().toString());
        } else{
            holder.mValor.setTextColor(context.getResources().getColor(R.color.colorPrimaryDarkReceita));
            holder.mValor.setText("  R$" + movimentacao.getValor().toString());

        }



    }

    @Override
    public int getItemCount() {
        return movimentos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDescricao, mCategoria, mValor;

        public MyViewHolder(View itemView) {
            super(itemView);

            mDescricao = itemView.findViewById(R.id.tv_descricao_principal);
            mCategoria = itemView.findViewById(R.id.tv_categoria_principal);
            mValor = itemView.findViewById(R.id.tv_valor_principal);
        }


}

}
