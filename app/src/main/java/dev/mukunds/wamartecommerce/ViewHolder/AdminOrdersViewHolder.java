package dev.mukunds.wamartecommerce.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dev.mukunds.wamartecommerce.R;

public  class AdminOrdersViewHolder extends RecyclerView.ViewHolder
{

    public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
    public Button ShowordersBtn;

    public AdminOrdersViewHolder( View itemView)
    {
        super(itemView);

        userName = itemView.findViewById(R.id.order_user_name);
        userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        userDateTime = itemView.findViewById(R.id.order_date_time);
        userShippingAddress = itemView.findViewById(R.id.order_address_city);
        ShowordersBtn = itemView.findViewById(R.id.show_all_products_btn);
    }
}
