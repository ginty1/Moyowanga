package com.example.moyowanga;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;


public class MayoLoginActivity extends AppCompatActivity {
    private static final String TAG = "MayoLoginActivity";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference dbb;
    private FirebaseDatabase firebaseDatabase;
    public CollectionReference docRef;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moyo_login_activity);
        Toolbar toolbarl=findViewById(R.id.tool_home);
        setSupportActionBar(toolbarl);
        setTitle("MoyoWanga");
        ImageButton imageButton = findViewById(R.id.facebook_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        mCallbackManager = CallbackManager.Factory.create();
        user=FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null){
            user.getIdToken(true);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(MayoLoginActivity.this,MoyoHomeActivity.class);
                //startActivity(intent);
             LoginManager.getInstance().logInWithReadPermissions(MayoLoginActivity .this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                        updateUI(null);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void updateUI(FirebaseUser currentUser) {
        if (mAuth.getCurrentUser() !=null){
            Intent intentl=new Intent(MayoLoginActivity .this,MoyoHomeActivity.class);
            startActivity(intentl);
            finish();

        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user=mAuth.getCurrentUser();
                            docRef = FirebaseFirestore.getInstance().collection("personalDetails");

                            String userId=user.getUid();
                            docRef.document(userId);
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MayoLoginActivity .this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }

                         }
                });
    }
    //login menu inflator/showing icons
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moyologinmenu, menu);
        return true;
    }

    public void phoneNumbedEnter(View view) {
        Intent intentPhonenumberEntery=new Intent(MayoLoginActivity .this,MoyoPhonNumber.class);
        startActivity(intentPhonenumberEntery);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
           user=mAuth.getCurrentUser();
        if (user !=null){
            updateUI(user);} }

    @Override
    protected void onResume() {
        super.onResume();
        user=mAuth.getCurrentUser();
        if (user!=null){
            Intent intentl=new Intent(MayoLoginActivity .this,MoyoHomeActivity.class);
            startActivity(intentl);
            finish();

        }

    }
}
