package com.example.moyowanga;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import id.zelory.compressor.Compressor;


import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
//facebook keytool
//C:\Program Files\Android\Android Studio\jre\bin>keytool -exportcert -alias androiddebugkey -keystore"C:\Users\Frank Mwafulirwa Jr\.android\debug.keystore"|"C:\OpenSSL\bin\openssl" sha1 -binary | "C:\OpenSSL\bin\openssl" base64
import static com.example.moyowanga.MoyoUniversalImageLoader.setImage;

public class MoyoHomeActivity extends AppCompatActivity{
    Toolbar toolbar;
    private TextView textVDzina,textVDOB,textVWeight,textVBp,textVDia,textVHeight,textVNationalId;
    //public ImageView imageButton;
    private static final String TAG = "MoyoHomeActivity";
    DatabaseReference dbb;
   // private CircleImageView uImageProfile;
    private static final int IMAGE_REQUEST=1;
    private Uri imageurl;
    byte[] thumb_byte_data;
    private FirebaseUser user;
    public String userId;
    public  String phone;
    public String e_patient_name;
    public ProgressBar progressBar;
    private ViewPagerAdapter viewPagerAdapter;
    private CropImageView cropImageView;
    private CircleImageView uImageProfile;
    Bitmap bitmap=null;


    StorageReference storageRef;
    StorageTask uploadTask;
    public Button buttonToken;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentReference docuR;
    private CollectionReference collectRef;
    FirebaseAuth auth;
    public double profileProgress=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fresco.initialize(this);
        setContentView(R.layout.moyo_home_activity);

        toolbar=findViewById(R.id.moyo_toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);



        textVDzina=findViewById(R.id.viePersonName);
        textVDOB=findViewById(R.id.viwPersonalDOB);
        textVHeight=findViewById(R.id.viwPersonalHeight);
        textVWeight=findViewById(R.id.viwPersonalWeight);
        textVBp=findViewById(R.id.viwPersonalBp);
        textVDia=findViewById(R.id.viwPersonalDiaB);
        textVNationalId=findViewById(R.id.viewNationalId);
        uImageProfile=findViewById(R.id.image_Profile);
        //draweeView=findViewById(R.id.image_Profile);
        uImageProfile.setImageURI(imageurl);
        progressBar=findViewById(R.id.progress_circularBar);
        cropImageView=findViewById(R.id.cropImageView);
        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                cropImageView.getCroppedImageAsync();
            }
        });
        cropImageView.setImageUriAsync(imageurl);


                //refering to storage
        storageRef=FirebaseStorage.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            userId =user.getUid();
            collectRef=FirebaseFirestore.getInstance().collection(userId);
   String auth_provider=user.getProviderId();
            if (auth_provider !=null){
                docuR=collectRef.document(auth_provider);

            }else {

            }
 docuR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
     @Override
     public void onSuccess(DocumentSnapshot documentSnapshot) {
         String name=documentSnapshot.getString("1userName");
         textVDzina.setText(name);
         String dateOfBirth=documentSnapshot.getString("3user date of birth");
         textVDOB.setText(dateOfBirth);
         String userWeight=documentSnapshot.getString("4userWeight");
         textVWeight.setText(userWeight);
         String userHeight=documentSnapshot.getString("5userHeight");
         textVHeight.setText(userHeight);
         String userBloodPressure=documentSnapshot.getString("6userBloodPressure");
         textVBp.setText(userBloodPressure);
         String bloodSugar=documentSnapshot.getString("7userBloodSugar");
         textVDia.setText(bloodSugar);
         String nationalId=documentSnapshot.getString("2userNationalIdNo");
         textVNationalId.setText(nationalId);
     }
 });
        }

        settingUpImageLoder();


RelativeLayout relativeLayout1=findViewById(R.id.relativeMw);
AnimationDrawable animationDrawable1=(AnimationDrawable)relativeLayout1.getBackground();
animationDrawable1.setExitFadeDuration(2500);
animationDrawable1.start();

//checkLoginStatus();

        //tab and view pager layout/adding fragments and so on
        final TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_pager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragmentMoyoActivity());
        viewPagerAdapter.addFragment(new Fragment_Moyo_Second());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_launcher_ask);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_lab_icon);





          user= FirebaseAuth.getInstance().getCurrentUser();
          if (user!=null){
              userId=user.getUid();
              docuR=db.collection(userId).document("profile");
              docuR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {
                      if (documentSnapshot !=null){
                          if (documentSnapshot.exists()){
                              String imageProfile=documentSnapshot.get("image").toString();
                              MoyoUniversalImageLoader.setImage(imageProfile,uImageProfile,progressBar,"");
                          }else {
                              uImageProfile.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);


                          }
                      }

                  }
              })

 .addOnFailureListener(new OnFailureListener() {
                  @Override
                 public void onFailure(@NonNull Exception e) {

                      Toast.makeText(getApplicationContext(),"allow data connection",Toast.LENGTH_LONG).show();

                      uImageProfile.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                  }
              }).addOnCanceledListener(new OnCanceledListener() {
                  @Override
                  public void onCanceled() {


                      uImageProfile.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);


                      Toast.makeText(getApplicationContext(),"canceled",Toast.LENGTH_SHORT).show();
                  }
              });


          }



        uImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfile();
            }
        });


    }

 private void settingUpImageLoder() {
        MoyoUniversalImageLoader universalImageLoader=new MoyoUniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void changeProfile() {
        pickImage();

    }

    public void checkAndroidBuildVersion(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            }catch (Exception e){

            }
        }else {
            pickImage();
        }

    }
    public void pickImage(){
        CropImage.startPickImageActivity(this);
    }
    private void cropRequest(Uri imageurl){
        CropImage.activity(imageurl)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setActivityTitle("MoyoWanga")
                .setBackgroundColor(Color.WHITE)
                .setBorderLineColor(Color.BLACK)
                .setAllowRotation(true)
                .setActivityMenuIconColor(Color.WHITE)
                .setMultiTouchEnabled(true)
                .start(MoyoHomeActivity.this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     if (requestCode==555 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
         pickImage();

     }else {
         checkAndroidBuildVersion();
     }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri imageurl = CropImage.getPickImageResultUri(this, data);
             cropRequest(imageurl);

        }
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);


            if  (resultCode==RESULT_OK){
                imageurl=result.getUri();

                Uri resultUri=imageurl;

                File file_path=new File(resultUri.getPath());

                try {

                    bitmap= new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(file_path);

                }catch (IOException e){
                    e.printStackTrace();

                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
             thumb_byte_data= baos.toByteArray();


                Log.d(TAG,"ActivityResult:imageurl"+imageurl.toString());
                uImageProfile.setImageURI(imageurl);
                Toast.makeText(getApplicationContext(),"wait uploading...",Toast.LENGTH_LONG).show();
                uploadProfile();


            }else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
            }


            }
        }

    private void uploadProfile() {




        if ( thumb_byte_data!=null){

    final StorageReference  imagesStorageReference;
           if (user!=null){
               userId=user.getUid();

               imagesStorageReference  = storageRef.child(userId).child(System.currentTimeMillis()+"."+getFileExtension(imageurl));

               dbb= FirebaseDatabase.getInstance().getReference("Users").child(userId);

              uploadTask=imagesStorageReference.putBytes(thumb_byte_data);



               uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                   @Override
                   public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if (!task.isSuccessful()) {
                           throw task.getException();
                       }

                       return imagesStorageReference.getDownloadUrl();
                   }
               }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                   @Override
                   public void onComplete(@NonNull Task<Uri> task) {
                       if (task.isSuccessful()) {
                           Uri downloadUri = task.getResult();
                           String profileUrl=downloadUri.toString();

                      if (user!=null){
                           userId=user.getUid();
                             HashMap<String,Object>Url=new HashMap();
                             Url.put("image",profileUrl);
                          docuR=db.collection(userId).document("profile");
                          docuR.set(Url);
                          Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();

                       }
                       }
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),"network error",Toast.LENGTH_SHORT).show();

                   }
               }).addOnCanceledListener(new OnCanceledListener() {
                   @Override
                   public void onCanceled() {
                       Toast.makeText(getApplicationContext(),"canceled",Toast.LENGTH_SHORT).show();

                   }
               });

           }







}

    }












    //item menu buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.moyomenu, menu);
        return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.moyo_write:
                Intent  intentmoyobio =new Intent(MoyoHomeActivity.this, Moyo_Sug_Activity.class);
                startActivity(intentmoyobio);
                break;
            case R.id.moyo_bio:
                Intent  intentB =new Intent(MoyoHomeActivity.this,PersonalInforActivity.class);
                startActivity(intentB);
               // finish();
                break;
                case R.id.Buttflogout:

                    auth=FirebaseAuth.getInstance();
                    auth.signOut();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
               Intent intent = new Intent(MoyoHomeActivity.this, MayoLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
                default:
        }
        return true;
    }

}




















































