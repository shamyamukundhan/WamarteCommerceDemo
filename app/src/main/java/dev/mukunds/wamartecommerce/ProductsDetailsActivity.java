package dev.mukunds.wamartecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import dev.mukunds.wamartecommerce.Model.Products;
import dev.mukunds.wamartecommerce.Prevalent.Prevalent;
import dev.mukunds.wamartecommerce.ui.home.HomeFragment;

public class ProductsDetailsActivity extends AppCompatActivity
{
    private ImageView productImage;
    private TextView productPrice,productDescription,productName;
    private String productID= "";
    public String  state="Normal";
    public  TextView counter_value;
    public FloatingActionButton addbtn,subtract_btn;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);

        productID=getIntent().getStringExtra("pid");
        Button addtoCartBtn = findViewById(R.id.add_product_to_cart_btn);
        productImage= findViewById(R.id.product_image_details);
        counter_value = findViewById(R.id.counter_value);
        addbtn= findViewById(R.id.addquantity);
        subtract_btn = findViewById(R.id.minus_quantity);
        productPrice= findViewById(R.id.product_price_details);
        productDescription= findViewById(R.id.product_description_details);
        productName= findViewById(R.id.product_name_details);

        addbtn.setOnClickListener(v -> {
            count++;
            counter_value.setText(Integer.toString(count));
        });
        subtract_btn.setOnClickListener(v -> {
            count--;
            counter_value.setText(Integer.toString(count));
        });

        getProductDetails(productID);

        addtoCartBtn.setOnClickListener(view -> {

            if(state.equalsIgnoreCase("order placed")||state.equalsIgnoreCase("order shipped"))
            {
                Toast.makeText(ProductsDetailsActivity.this, "You can purchase more orders once your order is shipped or confirmed ", Toast.LENGTH_LONG).show();
            }
            else
            {
                addingtoCartList();
            }
       });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }

    private void addingtoCartList()
    {
        String saveCurrentTime,saveCurrentDate;

        Calendar calforDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calforDate.getTime());

        final DatabaseReference cartRef =FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",counter_value.getText());
        cartMap.put("discount","");
        cartRef.child("User View").child(Prevalent.currentonlineusers.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        cartRef.child("Admin View").child(Prevalent.currentonlineusers.getPhone())
                                .child("Products").child(productID)
                                .updateChildren(cartMap)
                                .addOnCompleteListener(task1 -> {
                                    Toast.makeText(ProductsDetailsActivity.this, "Added to cart list", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(ProductsDetailsActivity.this, MainActivity.class);
                                            startActivity(intent);
                                });

                    }

                });
    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    //Products products= dataSnapshot.child("Products").child("productID").getValue(Products.class);
                    Products products= dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private  void CheckOrderState()
    {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineusers.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingState= dataSnapshot.child("state").getValue().toString();
                    if(shippingState.equalsIgnoreCase("shipped"))
                    {
                       state="Order Shipped";
                    }
                    else if (shippingState.equalsIgnoreCase("not Shipped"))
                    {
                        state="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}