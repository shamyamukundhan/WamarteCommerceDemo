package dev.mukunds.wamartecommerce.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import dev.mukunds.wamartecommerce.Model.Products;
import dev.mukunds.wamartecommerce.Prevalent.Prevalent;
import dev.mukunds.wamartecommerce.ProductsDetailsActivity;
import dev.mukunds.wamartecommerce.R;
import dev.mukunds.wamartecommerce.ViewHolder.ProductViewHolder;
import dev.mukunds.wamartecommerce.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private DatabaseReference ProductsRef;
    RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ProductsRef= FirebaseDatabase.getInstance().getReference().child("Products");
         binding.recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(binding.recyclerMenu.getContext());
        binding.recyclerMenu.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=new  FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef,Products.class)
                .build();

        FirebaseRecyclerAdapter <Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtproductName.setText(model.getPname());
                        holder.txtproductdescription.setText(model.getDescription());
                        holder.txtproductprice.setText(getString(R.string.price)+ model.getPrice());

                        Picasso.get().load(model.getImage()).into(holder.productimage);
                        holder.itemView.setOnClickListener(view -> {
                            Intent intent = new Intent(getContext(), ProductsDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        return new ProductViewHolder(view);
                    }
                };

        binding.recyclerMenu.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}