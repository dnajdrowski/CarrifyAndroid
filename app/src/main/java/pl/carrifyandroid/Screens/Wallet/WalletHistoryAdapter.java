package pl.carrifyandroid.Screens.Wallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import pl.carrifyandroid.Models.Transaction;
import pl.carrifyandroid.R;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.ViewHolder> {

    private Context context;
    private List<Transaction> transactionList = new ArrayList<>();
    private LayoutInflater mInflater;

    WalletHistoryAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WalletHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_wallet_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull WalletHistoryAdapter.ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        holder.walletOperationAmount.setText(String.format("%.2f PLN", (float) (transaction.getAmount() / 100)));
        holder.walletOperationBalance.setText(String.format("%.2f PLN", (float) (transaction.getBalance() / 100)));
        holder.walletOperationDate.setText(transaction.getCreatedAt());

        if (transaction.getOperationType() == 2)
            Glide.with(context).load(R.drawable.ic_add_wallet_operation).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.walletOperationImage);
        else if (transaction.getOperationType() == 1)
            Glide.with(context).load(R.drawable.ic_sub_wallet_operation_24dp).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.walletOperationImage);

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    void setList(List<Transaction> results) {
        transactionList.addAll(results);
        notifyDataSetChanged();
    }

    void clear() {
        transactionList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView walletOperationBalance, walletOperationAmount, walletOperationDate;
        ImageView walletOperationImage;

        ViewHolder(View itemView) {
            super(itemView);

            walletOperationImage = itemView.findViewById(R.id.walletOperationImage);
            walletOperationBalance = itemView.findViewById(R.id.walletOperationBalance);
            walletOperationAmount = itemView.findViewById(R.id.walletOperationAmount);
            walletOperationDate = itemView.findViewById(R.id.walletOperationDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
