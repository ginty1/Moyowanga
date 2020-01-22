package com.example.moyowanga;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
public class Moyo_Sug_Activity extends AppCompatActivity implements View.OnClickListener {


    public Button btnSaveH,btnSearchButton;
    public ImageButton btn_back;
    public ImageView btn_clear,btn_share;
    public EditText editInputH;
    private AutoCompleteTextView editSearchBar;
    public NestedScrollView btnScrow;
    private BottomSheetBehavior boSB;
    public TextView mShowSearch;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private ProgressBar progressBar;
    private DocumentReference dRef=db.collection("HowIGotCured").document("GotCured");

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moyo_sug_activity);
        dbb= FirebaseDatabase.getInstance().getReference().child("Users").child("disease");


        btn_clear=findViewById(R.id.clearSearch);
        editSearchBar=findViewById(R.id.searchBar);
        editInputH=findViewById(R.id.edi_input_health);
        mShowSearch=findViewById(R.id.textShow);

        btn_share=findViewById(R.id.buttonShare);

        btnSaveH=findViewById(R.id.btn_save_health);
       // btnLoadH=findViewById(R.id.btn_load_bio);
        btnSearchButton=findViewById(R.id.searchButton);
        btn_back=findViewById(R.id.btn_backHome);

        btnScrow=findViewById(R.id.bottom_sheet);

        btnSearchButton.setOnClickListener(this);
        btnSaveH.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        //btnLoadH.setOnClickListener(this);
        boSB=BottomSheetBehavior.from(btnScrow);

        //listening to text
        mShowSearch.addTextChangedListener(changeBioInput);
        editInputH.addTextChangedListener(changeBioInput);
        editSearchBar.addTextChangedListener(changeBioInput);
        mShowSearch.addTextChangedListener(changeBioInput);
       // btn_clear.setOnClickListener(this);
        btn_back.setOnClickListener(this);



        //setting auto completion to search bar in bio activity
        String[]arDia=getResources().getStringArray(R.array.Diseases);
        ArrayAdapter<String> arDisease=new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,arDia);
        editSearchBar.setAdapter(arDisease);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             boSB.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        boSB.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;


                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.searchButton:
                dbb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String searchDisease=editSearchBar.getText().toString().trim();
                            if (dataSnapshot.child(searchDisease).exists()){
                                mShowSearch.setText(dataSnapshot.child(searchDisease).getValue().toString());
                            }else {
                                Toast.makeText(getApplicationContext(),"result not found",Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"no internet",Toast.LENGTH_SHORT).show();

                    }
                });
                boSB.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.btn_save_health:
                Object healthy=editInputH.getText().toString();
                HashMap<String,Object>health=new HashMap<>();
                health.put("howigotcure",healthy);
                dRef.set(health, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getApplicationContext(),"smile",Toast.LENGTH_LONG).show();

                        editInputH.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"network error",Toast.LENGTH_SHORT).show();

                    }
                });

                break;
            case  R.id.btn_backHome:
                Intent inteBackHome=new Intent(getApplicationContext(),MoyoHomeActivity.class);
                startActivity(inteBackHome);
                finish();
                break;
                //initializing share
            case R.id.buttonShare:
                String shareText=mShowSearch.getText().toString();
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Moyowanga");
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                startActivity(Intent.createChooser(shareIntent,"kudzera"));
        }

    }
    private TextWatcher changeBioInput=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //checking search bar input
            String searchInput=editSearchBar.getText().toString();
            if (!searchInput.isEmpty()&& searchInput.trim().length()<=21){
                btnSearchButton.setEnabled(true);
            }else {
                btnSearchButton.setEnabled(false);
            }
            //biography checking input
            String bioInput=editInputH.getText().toString();
            if (!bioInput.trim().equals("") && bioInput.length()<500){
                btnSaveH.setEnabled(true);
                if (bioInput.length()>500){
                    btnSaveH.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"you have maximized number of worlds",Toast.LENGTH_SHORT).show();
                }
            }else {
                btnSaveH.setEnabled(false );
            }
            String showText=mShowSearch.getText().toString();
            if (showText.isEmpty()){
                btn_share.setEnabled(false);
            }else {
                btn_share.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



}