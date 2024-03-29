package com.example.blogapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.blogapp.R;
import com.example.blogapp.adapter.CommentAdapter;
import com.example.blogapp.model.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost;
    CircleImageView imgCurrentUser, postUserImg;
    TextView txtPostDesc, txtPostDateName, txtPostTitle;
    EditText editTextComment;
    Button addComment;
    String postKey;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //let's set the status bar to transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_post_detail);

        getSupportActionBar().hide();

        //ini Views
        RvComment = findViewById(R.id.rv_comment);
        imgPost = findViewById(R.id.post_detail_img);
        imgCurrentUser = findViewById(R.id.post_detail_current_user_img);
        postUserImg = findViewById(R.id.post_detail_user_img);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);
        txtPostTitle = findViewById(R.id.post_detail_title);
        addComment = findViewById(R.id.post_detail_add_comment_btn);
        editTextComment = findViewById(R.id.post_detail_comment);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //add Comment button click listener
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_content = editTextComment.getText().toString().trim();
                String uid = currentUser.getUid();
                String uname = currentUser.getDisplayName();
                String uimg = currentUser.getPhotoUrl().toString();
                if (TextUtils.isEmpty(comment_content)) {
                    Toast.makeText(PostDetailActivity.this, "Write some comment!", Toast.LENGTH_SHORT).show();
                } else {

                    DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(postKey)
                            .push();

                    Comment comment = new Comment(comment_content, uid, uimg, uname);
                    commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showMessage("comment added");
                            editTextComment.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = e.getMessage();
                            showMessage(message);
                        }
                    });
                }
            }
        });


        //now we can get post data
        String postImage = getIntent().getStringExtra("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        String posttitle = getIntent().getStringExtra("title");
        txtPostTitle.setText(posttitle);

        String userPostImage = getIntent().getStringExtra("userPhoto");
        Glide.with(this).load(userPostImage).into(postUserImg);

        String postDescription = getIntent().getStringExtra("description");
        txtPostDesc.setText(postDescription);

        //setcomment user image
        Glide.with(this).load(currentUser.getPhotoUrl()).into(imgCurrentUser);

        postKey = getIntent().getExtras().getString("postKey");

        String postDate = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(postDate);

        initRvComment();
    }

    private void initRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef = firebaseDatabase.getReference("Comment").child(postKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }
}
