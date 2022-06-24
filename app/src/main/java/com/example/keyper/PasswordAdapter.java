package com.example.keyper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {
    Context context;
    List<Service> passwordsList;
    RecyclerView rvPasswords;
    TextView emptyPassViewerTextView;
    boolean isSelectMode = false;
    public ArrayList<Service> selectedItems = new ArrayList<>();
    final View.OnClickListener onClickListener = new MyOnClickListener();

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView serviceName;
        TextView servicePassword;
        ImageButton copyBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            servicePassword = itemView.findViewById(R.id.servicePassword);
            copyBtn = itemView.findViewById(R.id.servicePasswordCopyButton);
        }
    }

    public PasswordAdapter(Context context, List<Service> passwordsList, RecyclerView rvPasswords, TextView emptyPassViewerTextView) {
        this.context = context;
        this.passwordsList = passwordsList;
        this.rvPasswords = rvPasswords;
        this.emptyPassViewerTextView = emptyPassViewerTextView;
    }

    public PasswordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.service, parent, false);
        view.setOnClickListener(onClickListener);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordAdapter.ViewHolder holder, int position) {
        Service service = passwordsList.get(position);
        holder.serviceName.setText("" + service.getName());
        holder.servicePassword.setText("" + service.getPassword());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isSelectMode = true;
                if (selectedItems.contains(service)) {
                    v.setBackgroundResource(R.color.window_background_color);
                    selectedItems.remove(service);
                } else {
                    v.setBackgroundResource(R.color.window_clicked_color);
                    selectedItems.add(service);
                }

                if (selectedItems.size() == 0)
                    isSelectMode = false;
                return true;
            }

        });

        holder.copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordValue = service.getPassword();
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("copy text", passwordValue);
                clipboardManager.setPrimaryClip(data);
                Toast.makeText(context, "Password copied to Clipboard!", Toast.LENGTH_LONG).show();
            }

        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isSelectMode) {
                    if (selectedItems.contains(service)) {
                        v.setBackgroundResource(R.color.window_background_color);
                        selectedItems.remove(service);
                    } else {
                        v.setBackgroundResource(R.color.window_clicked_color);
                        selectedItems.add(service);
                    }

                    if (selectedItems.size() == 0)
                        isSelectMode = false;
                } else {

            }
        }
    });
    }

    @Override
    public int getItemCount() {
        return passwordsList.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = rvPasswords.getChildLayoutPosition(v);
            String name = passwordsList.get(itemPosition).getName();
            String password = passwordsList.get(itemPosition).getPassword();
        }

    }
}
