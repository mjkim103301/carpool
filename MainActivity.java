package com.example.isl.dbtest;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;




public class MainActivity extends AppCompatActivity {
    //선언
    TextView TextView, IDView, PWView;
    EditText IDText, PWText;
    Button btnSave, btnOverlap;
    int check=0;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml 원하는거 환경설정
        setContentView(R.layout.activity_main);
        btnSave= findViewById(R.id.btnSave);
        IDText=findViewById(R.id.IDText);
        PWText=findViewById(R.id.PWText);
        TextView=findViewById(R.id.TextView);
        IDView=findViewById(R.id.IDView);
        PWView=findViewById(R.id.PWView);
        btnOverlap=findViewById(R.id.btnOverlap);

        View.OnClickListener onClickListener= new View.OnClickListener(){
            @Override
            public void onClick(View v)

            {

                String ID, PW;

                String value=((Button)v).getText().toString();
                Map<String, Object> user3=new HashMap<>();
                ID=IDText.getText().toString();//입력받은 ID
                PW=PWText.getText().toString();//입력받은 PW

                if("회원가입 하기".equals(value)){


                        if (check == 0) {
                            Toast.makeText(getApplicationContext(), "ID 중복확인이 필요합니다.", Toast.LENGTH_LONG).show();
                        }
                        else if(check>0){

                                user3.put("ID", ID);
                                user3.put("PW", PW);
                            db.collection("민지컬렉션").document(ID)//ID를 문서에도 저장
                                    .set(user3)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully written!" );
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error adding document", e);
                                        }
                                    });


                            Toast.makeText(getApplicationContext(),"회원가입 되었습니다.", Toast.LENGTH_LONG).show();

                            //ID, PW 텍스트필드 비우기
                            IDText.setText(null);
                            PWText.setText(null);

                        }




                }
                else if("중복확인".equals(value)){
                    DocumentReference idRef=db.collection("민지컬렉션").document(ID);
                    idRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document=task.getResult();
                                if(document.exists()){
                                    Toast.makeText(getApplicationContext(),"중복된 ID 입니다.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "사용 가능한 ID  입니다.", Toast.LENGTH_LONG).show();
                                    check++;
                                }
                            }
                            else{
                                Log.d("TAG", "get failed with", task.getException());
                            }
                        }
                    });


                }

            }
        };// View.OnClickListener onClickListener= new View.OnClickListener(){
        btnSave.setOnClickListener(onClickListener);
        btnOverlap.setOnClickListener(onClickListener);


    }

}
