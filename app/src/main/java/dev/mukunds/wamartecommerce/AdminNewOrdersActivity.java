package dev.mukunds.wamartecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.mukunds.wamartecommerce.Model.AdminOrders;
import dev.mukunds.wamartecommerce.ViewHolder.AdminOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity
{
    private RecyclerView ordersList_recyclerview;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersList_recyclerview = findViewById(R.id.orders_list_recyclerview);
        ordersList_recyclerview.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        ordersList_recyclerview.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        final DatabaseReference  ordersListRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<AdminOrders> options
                =new  FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersListRef,AdminOrders.class)
                .build();


       FirebaseRecyclerAdapter <AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(AdminOrdersViewHolder holder, int position, AdminOrders model1)
                    {
                        holder.userName.setText("Name: " + model1.getName());
                        holder.userPhoneNumber.setText("Phone" + model1.getPhone());
                        holder.userDateTime.setText("Ordered on:" + model1.getDate() + " Time:" + model1.getTime());
                        holder.userShippingAddress.setText(("Address:" + model1.getAddress() + "City " + model1.getCity()));
                        holder.userTotalPrice.setText("Total Price :" + model1.getTotalAmount());

                        holder.ShowordersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uid = getRef(position).getKey();
                                Intent intent = new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                                intent.putExtra("uid",uid);
                                startActivity(intent);
                            }
                        });
                    }


                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder( ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersViewHolder(view);
                    }
                };

        ordersList_recyclerview.setAdapter(adapter);
        adapter.startListening();


    }



}