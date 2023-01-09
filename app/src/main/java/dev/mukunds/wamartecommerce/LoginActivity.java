package dev.mukunds.wamartecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import dev.mukunds.wamartecommerce.Model.Users;
import dev.mukunds.wamartecommerce.Prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity
{
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog LoadingBar;
    private String parentDbName="Users";
    private CheckBox checkBox_remberme;
    private TextView AdminLink,NotAdminLink;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        InputPhoneNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);
        LoginButton = findViewById(R.id.login_btn);
        AdminLink =findViewById(R.id.admin_panel_link);
        NotAdminLink= findViewById(R.id.not_admin_panel_link);
        checkBox_remberme=findViewById(R.id.remember_me_chkb);
        LoadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(view -> LoginUser());

        AdminLink.setOnClickListener(view -> {
            LoginButton.setText(R.string.loginadmin);
            AdminLink.setVisibility(View.INVISIBLE);
            NotAdminLink.setVisibility(View.VISIBLE);
            parentDbName="Admins";

        });

        NotAdminLink.setOnClickListener(view -> {
            LoginButton.setText(R.string.Login);
            AdminLink.setVisibility(View.VISIBLE);
            NotAdminLink.setVisibility(View.INVISIBLE);
            parentDbName="Users";
        });
    }


    private void LoginUser() {
        String password = InputPassword.getText().toString();
        String phone = InputPhoneNumber.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter your phone number", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter your password", Toast.LENGTH_LONG).show();
        } else {
            LoadingBar.setTitle("Login Account");
            LoadingBar.setMessage("Please wait,while we are checking credentials ");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();
            AllowAccesstoAccount(phone, password);
        }
    }

    private void AllowAccesstoAccount(String phone, String password)
    {
        if(checkBox_remberme.isChecked())
        {
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData= dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    try
                    {
                        if (usersData.getPhone().equals(phone))
                        {
                            if (usersData.getPassword().equals(password))
                            {
                                if (parentDbName.equals("Admins"))
                                {
                                    Toast.makeText(LoginActivity.this, "Welcome Admin You are Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    LoadingBar.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                    startActivity(intent);
                                }
                                else if (parentDbName.equals("Users"))
                                {
                                    Toast.makeText(LoginActivity.this, "You are logged in successfully", Toast.LENGTH_SHORT).show();
                                    LoadingBar.dismiss();

                                    Intent startIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(startIntent);
                                    Prevalent.currentonlineusers=usersData;
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                LoadingBar.dismiss();
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                        Toast.makeText(LoginActivity.this, "Phone is null", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this phone number does not exist", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                   // Toast.makeText(LoginActivity.this,"You need to create a new Account",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}