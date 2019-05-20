package com.example.hariharansivakumar.tike;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Hariharan Sivakumar on 3/3/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;

    List<Hist> getDataAdapter;

    public RecyclerViewAdapter(List<Hist> getDataAdapter, Context context){

        super();

        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listhist, parent, false);

        ViewHolder viewHolder = new ViewHolder(v,context,getDataAdapter);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Hist getDataAdapter1 =  getDataAdapter.get(position);

        holder.quiz_questions.setText(getDataAdapter1.getStop());

        holder.dates.setText(getDataAdapter1.getDates());
        holder.amounts.setText(getDataAdapter1.getAmount());
        holder.froms.setText(getDataAdapter1.getFrom());
        holder.tos.setText(getDataAdapter1.getTo());
        holder.heads.setText(getDataAdapter1.getHeads());
        holder.tokens.setText(getDataAdapter1.getTicketid());



    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView quiz_questions;
        public TextView dates;
        public TextView heads;
        public TextView amounts;
        public TextView froms;
        public TextView tos;
        public TextView tokens;
        List<Hist> content = new ArrayList<Hist>();
        Context context;

        public ViewHolder(View itemView,Context context,List<Hist> content) {

            super(itemView);
            this.content = content;
            this.context = context;
            itemView.setOnClickListener(this);
            quiz_questions = (TextView) itemView.findViewById(R.id.quiz_question) ;
            dates = (TextView) itemView.findViewById(R.id.dates) ;
            heads = (TextView) itemView.findViewById(R.id.not) ;
            tokens = (TextView)itemView.findViewById(R.id.token);
            froms = (TextView)itemView.findViewById(R.id.from);
            tos = (TextView)itemView.findViewById(R.id.to);
            amounts = (TextView)itemView.findViewById(R.id.amount);


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Hist hist = this.content.get(position);
            Intent intent = new Intent(this.context,ShowTicket.class);
            intent.putExtra("id",hist.getTicketid());
            intent.putExtra("from",hist.getFrom());
            intent.putExtra("to",hist.getTo());
            intent.putExtra("date",hist.getDates());
            intent.putExtra("amount",hist.getAmount());
            this.context.startActivity(intent);
        }
    }
}