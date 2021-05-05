package com.flytant.firebaseexplore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private MainActivity.OnUserItemClickListener listener;

    public UserAdapter(Context context, ArrayList<User> userList, MainActivity.OnUserItemClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvEmail.setText("Email: " + userList.get(position).getEmail());
        holder.tvPassword.setText("Password: " + userList.get(position).getPassword());

        holder.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSaveButtonClick(userList.get(position).getUserId());
            }
        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteButtonClick(userList.get(position).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvEmail, tvPassword;
        private Button btAdd, btDelete;
        private ImageView imgUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmail = itemView.findViewById(R.id.textView);
            tvPassword = itemView.findViewById(R.id.textView1);
            btAdd = itemView.findViewById(R.id.button);
            btDelete = itemView.findViewById(R.id.button1);
            imgUser = itemView.findViewById(R.id.imageView);
        }
    }
}
