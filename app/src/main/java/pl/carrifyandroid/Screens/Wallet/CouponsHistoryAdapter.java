package pl.carrifyandroid.Screens.Wallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.carrifyandroid.Models.Coupon;
import pl.carrifyandroid.R;

public class CouponsHistoryAdapter extends RecyclerView.Adapter<CouponsHistoryAdapter.ViewHolder> {

    private List<Coupon> coupons = new ArrayList<>();
    private LayoutInflater mInflater;

    CouponsHistoryAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CouponsHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_coupon_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull CouponsHistoryAdapter.ViewHolder holder, int position) {
        Coupon coupon = coupons.get(position);

        holder.couponName.setText(coupon.getName() + ":");
        holder.couponDate.setText(coupon.getUsedAt());
        holder.couponValue.setText(coupon.getValue());
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    void setList(List<Coupon> results) {
        coupons = results;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView couponValue, couponName, couponDate;

        ViewHolder(View itemView) {
            super(itemView);

            couponValue = itemView.findViewById(R.id.couponValue);
            couponName = itemView.findViewById(R.id.couponName);
            couponDate = itemView.findViewById(R.id.couponDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
