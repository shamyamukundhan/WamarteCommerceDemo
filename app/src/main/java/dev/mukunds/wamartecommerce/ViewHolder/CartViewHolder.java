package dev.mukunds.wamartecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dev.mukunds.wamartecommerce.Interface.ItemClickListener;
import dev.mukunds.wamartecommerce.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

{
     public TextView txtProductName,txtProductPrice,txtProductQuantity;
     private ItemClickListener itemClickListener;

    public CartViewHolder( View itemView) {
        super(itemView);

        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);

    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClicklistener(ItemClickListener itemClicklistener)
    {
        this.itemClickListener = itemClicklistener;
    }
}
