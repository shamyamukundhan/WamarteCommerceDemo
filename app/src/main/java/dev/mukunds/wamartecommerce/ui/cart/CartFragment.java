package dev.mukunds.wamartecommerce.ui.cart;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dev.mukunds.wamartecommerce.ConfirmFinalActivity;
import dev.mukunds.wamartecommerce.MainActivity;
import dev.mukunds.wamartecommerce.Model.Cart;
import dev.mukunds.wamartecommerce.Prevalent.Prevalent;
import dev.mukunds.wamartecommerce.ProductsDetailsActivity;
import dev.mukunds.wamartecommerce.R;
import dev.mukunds.wamartecommerce.ViewHolder.CartViewHolder;
import dev.mukunds.wamartecommerce.databinding.FragmentCartBinding;


public class CartFragment extends Fragment
{
    private FragmentCartBinding binding;
    private TextView txtTotalAmount, txtmsg1;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcess_Btn;
    private int overallTotalPrice = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        txtmsg1 = root.findViewById(R.id.msg1);
        txtTotalAmount = root.findViewById(R.id.total_price);
        recyclerView = root.findViewById(R.id.cart_list_recyclerview);
        NextProcess_Btn = root.findViewById(R.id.next_process_btn);
        binding.cartListRecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        binding.nextProcessBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ConfirmFinalActivity.class);
            intent.putExtra("Total Price", String.valueOf(overallTotalPrice));
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onStart()
    {
        super.onStart();
  //      CheckOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentonlineusers.getPhone()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText("Quantity :  "+ model.getQuantity());
                holder.txtProductPrice.setText("Price:"+ model.getPrice());

                int onetypeProductPrice=((parseInt(model.getPrice().substring(1)))) * Integer.valueOf(model.getQuantity());

                overallTotalPrice=overallTotalPrice+onetypeProductPrice;

                txtTotalAmount.setText("Total Price = $"+ overallTotalPrice);

                holder.itemView.setOnClickListener(view -> {
                    CharSequence option[] =new CharSequence []
                            {
                                    "Edit",
                                    "Remove"
                            };
                    AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                    builder.setTitle("Cart options");
                    builder.setItems(option, (dialogInterface, i) -> {
                        if(i==0)
                        {
                            Intent intent = new Intent(getContext(), ProductsDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        }
                        if(i==1)
                        {
                            cartListRef.child("User View")
                                    .child(Prevalent.currentonlineusers.getPhone())
                                    .child("Products")
                                    .child(model.getPid())
                                    .removeValue()
                                    .addOnCompleteListener(task -> {
                                        Toast.makeText(getContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);

                                    });
                        }

                    });

                    builder.show();

                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder= new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private  void CheckOrderState()
    {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineusers.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String username = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {

                        txtTotalAmount.setText(getString(R.string.dear) + username + " \n your order is shipped successfully");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        txtmsg1.setText("Congratulations!,Your final order has been shipped successfully Soon you will receive your order at your doorstep");
                        NextProcess_Btn.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "You can purchase more products once you receive you first final order", Toast.LENGTH_SHORT).show();

                    } else if (shippingState.equals("not shipped")) {
                        txtTotalAmount.setText("Shipping State= Not Shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        NextProcess_Btn.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "You can purchase more products once you receive you first final order", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}
