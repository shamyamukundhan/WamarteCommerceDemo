package dev.mukunds.wamartecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName,InputProductDescription,InputProductprice;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private String productrandomKey,DownloadImageUrl;
    private DatabaseReference ProductsRef;
    private ProgressDialog LoadingBar;

    private  StorageReference ProductImagesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef =FirebaseDatabase.getInstance().getReference().child("Products");

        CategoryName=getIntent().getExtras().get("category").toString();
        LoadingBar=new ProgressDialog(this);
        AddNewProductButton=(Button) findViewById(R.id.add_new_product);
        InputProductName=(EditText) findViewById(R.id.product_name);
        InputProductDescription=(EditText) findViewById(R.id.product_description);
        InputProductprice=(EditText) findViewById(R.id.product_price);
        InputProductImage=(ImageView) findViewById(R.id.select_product_image);

        InputProductImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateProductData();

            }
        });

    }

    private void ValidateProductData()
    {
          Description=InputProductDescription.getText().toString();
          Price=InputProductprice.getText().toString();
          Pname=InputProductName.getText().toString();

          if(ImageUri==null)
          {
              Toast.makeText(this, "Product Image is mandatory", Toast.LENGTH_SHORT).show();
          }
          else if (TextUtils.isEmpty(Description))
          {
              Toast.makeText(this,"PLease enter Product description",Toast.LENGTH_SHORT).show();
          }
          else if (TextUtils.isEmpty(Price))
          {
              Toast.makeText(this,"PLease enter Product price",Toast.LENGTH_SHORT).show();
          }
          else if (TextUtils.isEmpty(Pname))
          {
              Toast.makeText(this,"PLease enter Product name",Toast.LENGTH_SHORT).show();
          }
          else
          {
              storeProductInformation();
          }

    }

    private void storeProductInformation()
    {

        LoadingBar.setTitle("Add new Product");
        LoadingBar.setMessage("Dear Admin ,Please wait,while we are adding products ");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();


        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentdate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentdate.format(calendar.getTime());
        SimpleDateFormat currenttime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currenttime.format(calendar.getTime());

        productrandomKey= saveCurrentDate+ saveCurrentTime;
        StorageReference filepath= ProductImagesRef.child(ImageUri.getLastPathSegment() + productrandomKey+".jpg");
        final UploadTask uploadTask =  filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {

                    String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();
                LoadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "ProductImage Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                        DownloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            DownloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,"got Product Image Url successfully",Toast.LENGTH_SHORT).show();
                            saveProductInfotoDatabase();
                        }
                    }

                    private void saveProductInfotoDatabase() {

                        HashMap<String,Object> productMap=new HashMap<>();
                        productMap.put("pid",productrandomKey);
                        productMap.put("date",saveCurrentDate);
                        productMap.put("time",saveCurrentTime);
                        productMap.put("description",Description);
                        productMap.put("image",DownloadImageUrl);
                        productMap.put("category",CategoryName);
                        productMap.put("price",Price);
                        productMap.put("pname",Pname);

                        ProductsRef.child(productrandomKey).updateChildren(productMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                             if(task.isSuccessful())
                                             {
                                                 LoadingBar.dismiss();
                                                 Intent intent=new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                                                 startActivity(intent);

                                                 Toast.makeText(AdminAddNewProductActivity.this, "Product is added succesfully", Toast.LENGTH_SHORT).show();
                                             }
                                             else
                                             {
                                                 LoadingBar.dismiss();

                                                 String message= task.getException().toString();
                                                 Toast.makeText(AdminAddNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                             }
                                    }
                                });

                    }
                });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode ==RESULT_OK && data!=null)
        {
             ImageUri=data.getData();
             InputProductImage.setImageURI(ImageUri);
        }
    }

    private void OpenGallery()
    {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GalleryPick);
    }




}