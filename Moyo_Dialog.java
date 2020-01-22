package com.example.moyowanga;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Moyo_Dialog extends DialogFragment {
  //  FirebaseFirestore db;
     private MultiAutoCompleteTextView nurseName;
     private MultiAutoCompleteTextView nurseNotes;
    private TextView textOkay,nurseDate;
    private TextView textCancel;
    private FirebaseAuth auth;
    private FirebaseUser user;
    public CollectionReference docRef;
    public  String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.moyo_dialog,container,false);


      user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            userId=user.getUid();

            docRef = FirebaseFirestore.getInstance().collection(userId);

        }

    nurseDate=view.findViewById(R.id.nurseDateInput);
    nurseName=view.findViewById(R.id.nurseNameInput);
    nurseNotes=view.findViewById(R.id.nurseNotesInput);

        String cdt = DateFormat.getDateTimeInstance().format(new Date());
        nurseDate.setText(cdt);
        textCancel=view.findViewById(R.id.diaCancel);
        textOkay=view.findViewById(R.id.diaOkay);


        String [] personelNames=getResources().getStringArray(R.array.personelNames);
        ArrayAdapter<String>arrayNames=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,personelNames);
        nurseName.setAdapter(arrayNames);
        nurseName.setTokenizer(new SpaceTokenizer());





        String [] notes=getResources().getStringArray(R.array.rootes);
        ArrayAdapter<String>arrNotes=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,notes);
        nurseNotes.setAdapter(arrNotes);
        nurseNotes.setTokenizer(new SpaceTokenizer());



        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        textOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String showNurseDate =nurseDate.getText().toString();
                String showNurseName=nurseName.getText().toString();
                 String showNurseNotes=nurseNotes.getText().toString();

                    docRef.add(new Nurse( showNurseDate,showNurseName,showNurseNotes)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                         //   Toast.makeText(getContext(), "Note added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "check ur network conn", Toast.LENGTH_SHORT).show();
                       }});

                    getDialog().dismiss();
                }
        //}
        });
        return view;
    }

}

