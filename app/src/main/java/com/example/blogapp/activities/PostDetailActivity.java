package com.example.blogapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.blogapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost;
    CircleImageView imgCurrentUser, postUserImg;
    TextView txtPostDesc, txtPostDateName, txtPostTitle;
    EditText editTextComment;
    Button addComment;
    String postKey;


    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //let's set the status bar to transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_post_detail);

        getSupportActionBar().hide();

        //ini Views
        imgPost = findViewById(R.id.post_detail_img);
        imgCurrentUser = findViewById(R.id.post_detail_current_user_img);
        postUserImg = findViewById(R.id.post_detail_user_img);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);
        txtPostTitle = findViewById(R.id.post_detail_title);
        addComment = findViewById(R.id.post_detail_add_comment_btn);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
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
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;
    }
}
