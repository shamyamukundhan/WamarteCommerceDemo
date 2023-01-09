package dev.mukunds.wamartecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import dev.mukunds.wamartecommerce.Prevalent.Prevalent;

public class ConfirmFinalActivity extends AppCompatActivity
{
     private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
     private Button confirmOrderBtn;
     private String totalamount ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final);
        totalamount=getIntent().getStringExtra("Total Price");

        Toast.makeText(this,"Total Amount :$ "+totalamount,Toast.LENGTH_SHORT).show();

        confirmOrderBtn= findViewById(R.id.confirm_final_order_btn);

        nameEditText=findViewById(R.id.shipment_name);
        phoneEditText=findViewById(R.id.shipment_phone);
        addressEditText=findViewById(R.id.shipment_address);
        cityEditText=findViewById(R.id.shipment_city);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Check();


            }
        });

    }

    private void Check()
    {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter your namer", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
        Toast.makeText(this, "Please Enter your phone number", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter your address", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter your city", Toast.LENGTH_LONG).show();
        }
        else
        {
            ConfirmOrder();

        }

    }

    private void ConfirmOrder()
    {
       final String saveCurrentTime,saveCurrentDate;

        Calendar calforDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calforDate.getTime());
        final DatabaseReference OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        final HashMap<String,Object> ordersMap = new HashMap<>();

        ordersMap.put("totalAmount",totalamount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","not Shipped");

        OrderRef.updateChildren(ordersMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            FirebaseDatabase.getInstance().getReference().child("Cart List")
                                    .child("User View")
                                    .child(Prevalent.currentonlineusers.getPhone())
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                              if(task.isSuccessful())
                                              {
                                                  Toast.makeText(ConfirmFinalActivity.this, "Your final Order has been placed successfully ", Toast.LENGTH_SHORT).show();
                                                  Intent intent=new Intent(ConfirmFinalActivity.this,MainActivity.class);
                                                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                  startActivity(intent);
                                                  finish();

                                              }
                                        }
                                    });
                        }

                    }
                });



    }
}