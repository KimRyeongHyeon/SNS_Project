package com.myandroid.sns_project.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.myandroid.sns_project.PostInfo;
import com.myandroid.sns_project.R;
import com.myandroid.sns_project.adapter.GalleryAdapter;
import com.myandroid.sns_project.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends BasicActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MainActivity";

    private int PERMISSION_ALL = 1;
    private View mLayout;
    String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            myStartActivity(SignUpActivity.class);
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                myStartActivity(MemberInitActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            db.collection("posts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<PostInfo> postList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    postList.add(new PostInfo(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>) document.getData().get("contents"),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime())));
                                }
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                                RecyclerView.Adapter mAdapter = new MainAdapter(MainActivity.this, postList);
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        }

//        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.logoutButton:
//                        FirebaseAuth.getInstance().signOut();
//                        myStartActivity(SignUpActivity.class);
//                        break;
//                }
//            }
//        });

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.floatingActionButton:
                        myStartActivity(WritePostActivity.class);
                        break;
                }
            }
        });






        //퍼미션
        mLayout = findViewById(R.id.layout_main);

        if(!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if(context != null && permissions != null) {
            for(String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //퍼미션

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[0])) {
            Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();
        }
    }
}
