package com.mocktail.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mocktail.app.R;
import com.mocktail.app.models.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText(order.getOrderId());
        holder.tvDate.setText(order.getDate() + " • " + order.getTime());
        holder.tvTotal.setText(String.format("₹%.2f", order.getTotalAmount()));
        holder.tvStatus.setText(order.getStatus());
        holder.tvSummary.setText(order.getSummary());
        holder.tvItemCount.setText(order.getItems().size() + " item(s)");
    }

    @Override
    public int getItemCount() { return orders.size(); }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvTotal, tvStatus, tvSummary, tvItemCount;

        OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvDate = itemView.findViewById(R.id.tv_order_date);
            tvTotal = itemView.findViewById(R.id.tv_order_total);
            tvStatus = itemView.findViewById(R.id.tv_order_status);
            tvSummary = itemView.findViewById(R.id.tv_order_summary);
            tvItemCount = itemView.findViewById(R.id.tv_order_item_count);
        }
    }
}
