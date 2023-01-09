package dev.mukunds.wamartecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCategoryActivity extends AppCompatActivity {

    public ImageView glasses,purses_bagswallets,hats_caps,shoes;
    public ImageView headphones_handsfree,laptop_pc,watches,mobilesphones;
    public ImageView camera,brush,smart_toy,sim_card,music, bathtub;
    public ImageView furniture, gift_card,baby,book,bedroom, umbrella;

    public Button logout_btn,check_order_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        ImageView tshirts = findViewById(R.id.t_shirts);
        ImageView sportsT_shirts = findViewById(R.id.sports_t_shirts);
        ImageView femaleDresses = findViewById(R.id.female_dresses);
        ImageView sweaters = findViewById(R.id.sweaters);

        glasses= findViewById(R.id.glasses);
        purses_bagswallets= findViewById(R.id.purses_bags_wallets);
        hats_caps=findViewById(R.id.hats_caps);
        shoes=findViewById(R.id.shoes);

        headphones_handsfree=findViewById(R.id.headphones_handfree);
        laptop_pc=findViewById(R.id.laptop_pc);
        watches= findViewById(R.id.watches);
        mobilesphones= findViewById(R.id.mobilephones);

        logout_btn=findViewById(R.id.admin_logout_btn);
        check_order_btn=findViewById(R.id.check_orders_btn);

        camera=findViewById(R.id.camera);
        brush=findViewById(R.id.brush);
        smart_toy=findViewById(R.id.toys);
        sim_card=findViewById(R.id.sim);
        music=findViewById(R.id.music);
        bathtub=findViewById(R.id.bathtub);
        furniture=findViewById(R.id.chair);
        gift_card=findViewById(R.id.giftcard);
        baby=findViewById(R.id.baby);
        book=findViewById(R.id.books);
        bedroom=findViewById(R.id.bedroom);
        umbrella=findViewById(R.id.umbrella);

        logout_btn.setOnClickListener(view -> {

            Intent intent =new Intent(AdminCategoryActivity.this,LoginActivity.class);

            startActivity(intent);
            finish();

        });

        check_order_btn.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
            startActivity(intent);

        });

        tshirts.setOnClickListener(view -> {

            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","T-shirts");
            startActivity(intent);
        });

        sportsT_shirts.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Sports T-Shirts");
            startActivity(intent);

        });

        femaleDresses.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Female Dresses");
            startActivity(intent);

        });

        sweaters.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Sweaters");
            startActivity(intent);

        });

        glasses.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Glasses");
            startActivity(intent);

        });

        purses_bagswallets.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Purses Bags Wallets");
            startActivity(intent);
        });

        hats_caps.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Hats and Caps");
            startActivity(intent);
        });

        shoes.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Shoes");
            startActivity(intent);
        });

        headphones_handsfree.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Headphones Handsfree");
            startActivity(intent);
        });

        laptop_pc.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Laptop_pc");
            startActivity(intent);
        });

        watches.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Watches");
            startActivity(intent);
        });

        mobilesphones.setOnClickListener(view -> {
            Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
            intent.putExtra("category","Mobiles");
            startActivity(intent);
        });

    }
}