package com.example.blog_app_new.CModels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog_app_new.R;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private List<String> photosBase64;

    public PhotosAdapter(List<String> photosBase64) {
        this.photosBase64 = photosBase64;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String base64Photo = photosBase64.get(position);
        Bitmap bitmap = decodeBase64ToBitmap(base64Photo);
        holder.photoImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return photosBase64.size();
    }

    public void updatePhotos(List<String> newPhotos) {
        this.photosBase64 = newPhotos;
        notifyDataSetChanged();
    }

    private Bitmap decodeBase64ToBitmap(String base64) {
        try {
            // Usuń niepotrzebne białe znaki
            base64 = base64.trim();

            // Sprawdzenie, czy dane Base64 nie są puste
            if (base64.isEmpty()) {
                Log.e("PhotosAdapter", "Base64 string is empty.");
                return null;
            }

            // Dekodowanie danych Base64
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (IllegalArgumentException e) {
            Log.e("PhotosAdapter", "Invalid Base64 string: " + e.getMessage());
            return null;
        }
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }
}
