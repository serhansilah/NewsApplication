package edu.sabanciuniv.osmanserhansilahyureklihomework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    List<CommentItem> commentItems;
    Context context;



    public CommentAdapter(List<CommentItem> commentItems, Context context) {
        this.commentItems = commentItems;
        this.context = context;

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_row,parent,false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, final int position) {

      holder.comname.setText(commentItems.get(position).getName());
      holder.commessage.setText(commentItems.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }


    class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView comname;
        TextView commessage;
        ConstraintLayout root;



        public CommentViewHolder(@NonNull View itemView){
            super(itemView);
            comname = itemView.findViewById(R.id.comname);
            commessage = itemView.findViewById(R.id.comtext);
            root = itemView.findViewById(R.id.comcontainer);

        }

    }
}
