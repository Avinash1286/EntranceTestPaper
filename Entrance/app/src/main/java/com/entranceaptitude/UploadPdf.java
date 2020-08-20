package com.entranceaptitude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {


    ImageView back,file;
    TextView selectStatus;
    EditText getTitle;
    Spinner options;
    Button btn;
    boolean file_avaliable=false;
    Uri pdfFile=null;
    String child=null;
    DatabaseReference uploadData;
    StorageReference uploadPdf;
    long fileCount;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        back=(ImageView)findViewById(R.id.backButton);
        file=(ImageView)findViewById(R.id.selectFile);
        selectStatus=(TextView)findViewById(R.id.selectStatus);
        getTitle=(EditText)findViewById(R.id.pdfTitle);
        options=(Spinner)findViewById(R.id.showOptions);
        btn=(Button)findViewById(R.id.uploadBtn);
        progressDialog=new ProgressDialog(this);
        uploadData= FirebaseDatabase.getInstance().getReference();
        uploadPdf= FirebaseStorage.getInstance().getReference().child("pdfs");
        final  String [] sections={"Maths","Science","English","Question Bank","Sample Paper"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,sections);
        options.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        child="Maths";
                        break;
                    case 1:
                        child="Science";
                        break;
                    case 2:
                        child="English";
                        break;
                    case 3:
                        child="QuestionBank";
                        break;
                    case 4:
                        child="SamplePaper";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData();
            }
        });

    }

    private void UploadData() {
        final String title=getTitle.getText().toString();
        if (title.isEmpty()){
            getTitle.setError("Please give title to the pdf file");
            getTitle.setFocusable(true);
            return;
        }
        if (child.isEmpty()){
            Toast.makeText(this, "Please Select Section", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pdfFile==null){
            Toast.makeText(this, "Pdf file not selected yet", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            StorageReference sRef = uploadPdf.child(System.currentTimeMillis() + ".pdf");
            sRef.putFile(pdfFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> result=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            progressDialog.setMessage("Saving Information");
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url=uri.toString();

                                    Calendar calendar=Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy");
                                   final String date=simpleDateFormat.format(calendar.getTime());
                                    SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("HH:mm:ss");
                                   String time=simpleDateFormat1.format(calendar.getTime());
                                    SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("HH:mm a");
                                   String timeToShow=simpleDateFormat2.format(calendar.getTime());
                                   String randomValue=date+time;
                                   if (child !=null){
                                       uploadData.addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               if (dataSnapshot.exists()){
                                                   fileCount=dataSnapshot.getChildrenCount();
                                               }
                                               else {
                                                   fileCount=0;
                                               }
                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                           }
                                       });

                                       HashMap puts=new HashMap();
                                       puts.put("title",title);
                                       puts.put("date",date);
                                       puts.put("time",timeToShow);
                                       puts.put("timesec",time);
                                       puts.put("url",url);
                                       puts.put("counter",fileCount);
                                     uploadData.child(child).child(randomValue).updateChildren(puts).addOnCompleteListener(new OnCompleteListener() {
                                         @Override
                                         public void onComplete(@NonNull Task task) {
                                           if (task.isSuccessful()){
                                               Toast.makeText(UploadPdf.this, "File Saved Successfully", Toast.LENGTH_SHORT).show();
                                               progressDialog.dismiss();
                                           }
                                           else {
                                               Toast.makeText(UploadPdf.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                               progressDialog.dismiss();
                                           }
                                         }
                                     });
                                   }

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage((int)progress+" Uploading...");

                        }
                    });
        }

    }

    private void getPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf"),7543);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==7543 && resultCode== RESULT_OK && data !=null && data.getData() !=null){
            pdfFile=data.getData();
            selectStatus.setText("File Selected");
            file.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        }
        else {
            Toast.makeText(this, "No file is selected", Toast.LENGTH_SHORT).show();
        }
    }
}