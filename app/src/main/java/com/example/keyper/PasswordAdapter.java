package com.example.keyper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {
    Context context;
    List<Service> passwordList;
    RecyclerView rvPasswords;
    TextView emptyPassViewerTextView;
    boolean isSelectMode = false;
    public ArrayList<Service> selectedServicesList = new ArrayList<>();
    final View.OnClickListener onClickListener = new MyOnClickListener();


    public PasswordAdapter(Context context, List<Service> passwordList, RecyclerView rvPasswords, TextView emptyPassViewerTextView) {
        this.context = context;
        this.passwordList = passwordList;
        this.rvPasswords = rvPasswords;
        this.emptyPassViewerTextView = emptyPassViewerTextView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.service,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordAdapter.ViewHolder holder, int position) {
        Service service = passwordList.get(position);
        holder.serviceName.setText("" + service.getName());
        holder.servicePassword.setText("" + service.getPassword());
        CardView cardView = holder.itemView.findViewById(R.id.service_card);
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.window_background_color));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isSelectMode)
                {
                    ActionMode.Callback callback = new ActionMode.Callback() {

                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                            mode.setTitle("Erase mode");
                            MenuInflater menuInflater = mode.getMenuInflater();
                            menuInflater.inflate(R.menu.selected,menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            isSelectMode = true;
                            ClickItem(holder);
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            int id = item.getItemId();
                            DBHandler db = new DBHandler(context);
                            if (id == R.id.deleteButton) {
                                for(Service s: selectedServicesList)
                                {
                                    passwordList.remove(s);
                                    db.deleteServicePassword(s.getName());
                                }
                                if(passwordList.size() == 0)
                                {
                                    emptyPassViewerTextView.setVisibility(View.VISIBLE);
                                }
                                isSelectMode = false;
                                selectedServicesList.clear();
                                notifyDataSetChanged();
                                mode.finish();
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            isSelectMode = false;
                            selectedServicesList.clear();
                            notifyDataSetChanged();
                        }
                    };
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                }
                else
                {
                    ClickItem(holder);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectMode) {
                    ClickItem(holder);
                }
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
    }

    private void ClickItem(ViewHolder holder) {
        Service s = passwordList.get(holder.getAdapterPosition());
        CardView cardView = holder.itemView.findViewById(R.id.service_card);
        if (!selectedServicesList.contains(s)) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.window_clicked_color));
            selectedServicesList.add(s);
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.window_background_color));
            selectedServicesList.remove(s);
        }
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

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

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = rvPasswords.getChildLayoutPosition(v);
            String name = passwordList.get(itemPosition).getName();
            String password = passwordList.get(itemPosition).getPassword();
        }

    }
}