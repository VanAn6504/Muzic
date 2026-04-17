package com.example.music.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.music.Models.Album;
import com.example.music.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateFragment extends Fragment {

    private EditText edtAlbumName;
    private Button btnCreate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        edtAlbumName = view.findViewById(R.id.edtPlaylistName);
        btnCreate = view.findViewById(R.id.btnCreatePlaylist);

        btnCreate.setOnClickListener(v -> createAlbum());

        return view;
    }

    private void createAlbum() {
        String name = edtAlbumName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Vui lòng nhập tên album!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUid())
                .child("albums");

        String id = ref.push().getKey();
        if (id == null) {
            Toast.makeText(getContext(), "Không thể tạo album (ID null)!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Dùng ảnh mặc định từ drawable
        String defaultImageUri = "android.resource://" + requireContext().getPackageName() + "/" + R.drawable.img_1;

        Album album = new Album(id, name, defaultImageUri, new ArrayList<>());

        ref.child(id).setValue(album)
                .addOnSuccessListener(unused -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Đã tạo album thành công!", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
