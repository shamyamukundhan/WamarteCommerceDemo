package dev.mukunds.wamartecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.mukunds.wamartecommerce.Interface.ItemClickListener;
import dev.mukunds.wamartecommerce.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductName, txtproductdescription,txtproductprice;
    public ImageView productimage;
    private ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

         txtproductName= (TextView) itemView.findViewById(R.id.product_name);
         txtproductdescription=(TextView) itemView.findViewById(R.id.product_description);
         productimage=(ImageView)  itemView.findViewById(R.id.product_image);
         txtproductprice=(TextView) itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {

        listener.onClick(view,getAdapterPosition(),false);

    }
}
