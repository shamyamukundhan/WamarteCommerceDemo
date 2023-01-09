package dev.mukunds.wamartecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity
{
    private ProgressDialog LoadingBar;
    private EditText InputName,InputPhoneNumber,InputPassword;
    private Button CreateAccountButton;
    private TextView Login_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_username_input);
        InputPassword= findViewById(R.id.register_password_input);
        InputPhoneNumber= findViewById(R.id.register_phone_number_input);
        LoadingBar = new ProgressDialog(this);


        CreateAccountButton.setOnClickListener(view -> createAccount());
        Login_txt = findViewById(R.id.signIn);
        Login_txt.setOnClickListener(view -> {
            Intent intent = new Intent(RegistrationActivity.this , LoginActivity.class);
            startActivity(intent);
        });

    }

    private void createAccount()
    {
        String name = InputName.getText().toString();
        String password = InputPassword.getText().toString();
        String phone = InputPhoneNumber.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please Enter your name",Toast.LENGTH_LONG).show();
        }

        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please Enter your phone number",Toast.LENGTH_LONG).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Enter your password",Toast.LENGTH_LONG).show();
        }
        else
        {
            LoadingBar.setTitle("Create Account");
            LoadingBar.setMessage("Please wait,while we are checking credentials ");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();

            ValidatePhoneNumber(name,phone,password);
        }

    }

    private void ValidatePhoneNumber(String name, final String phone, String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap= new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegistrationActivity.this,"Congratulations!,Your account is created",Toast.LENGTH_SHORT).show();
                                        LoadingBar.dismiss();
                                        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        LoadingBar.dismiss();
                                        Toast.makeText(RegistrationActivity.this,"Network Error:Please Try Again",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegistrationActivity.this,"This phone number "+phone+" already exits",Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                    Toast.makeText(RegistrationActivity.this,"Please try again",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }
}