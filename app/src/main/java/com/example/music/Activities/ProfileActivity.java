package com.example.music.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imgAvatar;
    private TextView tvName, tvEmail;
    private Uri imageUri;

    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        loadUserInfo();

        // Bấm avatar để đổi ảnh
        imgAvatar.setOnClickListener(v -> openFileChooser());
    }

    private void loadUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            tvEmail.setText(user.getEmail());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue(String.class);
                    String avatarPath = snapshot.child("avatarPath").getValue(String.class);

                    // Nếu name rỗng thì lấy luôn email trước @
                    if (name == null || name.trim().isEmpty()) {
                        name = user.getEmail().split("@")[0];
                    }
                    tvName.setText(name);

                    if (avatarPath != null) {
                        Glide.with(ProfileActivity.this)
                                .load(Uri.parse(avatarPath))
                                .placeholder(R.drawable.ic_avatar)
                                .circleCrop()   // ✅ bo tròn
                                .into(imgAvatar);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Lưu trực tiếp đường dẫn ảnh vào Realtime Database
            userRef.child("avatarPath").setValue(imageUri.toString());

            // Hiển thị ngay ảnh mới (bo tròn)
            Glide.with(ProfileActivity.this)
                    .load(imageUri)
                    .circleCrop()   // ✅ bo tròn
                    .into(imgAvatar);
        }
    }
}
