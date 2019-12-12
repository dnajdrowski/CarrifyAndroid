package pl.carrifyandroid.Screens.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.carrifyandroid.Models.Rent;
import pl.carrifyandroid.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<Rent> rentList = new ArrayList<>();
    private LayoutInflater mInflater;
    private HistoryAdapter.ItemClickListener mClickListener;

    HistoryAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rent rent = rentList.get(position);
        holder.historyStartDate.setText(rent.getCreatedAt());
        holder.historyEndDate.setText(rent.getEndAt());
        holder.historyCarRegistration.setText(rent.getCar().getRegistrationNumber());
        holder.historyCarName.setText(rent.getCar().getName());
    }

    @Override
    public int getItemCount() {
        return rentList.size();
    }

    void setList(List<Rent> results) {
        rentList.addAll(results);
        notifyDataSetChanged();
    }

    void clear() {
        rentList.clear();
        notifyDataSetChanged();
    }

    void setClickListener(HistoryAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView historyCarName, historyCarRegistration, historyEndDate, historyStartDate;

        ViewHolder(View itemView) {
            super(itemView);

            historyCarName = itemView.findViewById(R.id.historyCarName);
            historyCarRegistration = itemView.findViewById(R.id.historyCarRegistration);
            historyEndDate = itemView.findViewById(R.id.historyEndDate);
            historyStartDate = itemView.findViewById(R.id.historyStartDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
