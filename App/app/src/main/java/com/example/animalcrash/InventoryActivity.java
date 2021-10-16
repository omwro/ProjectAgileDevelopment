package com.example.animalcrash;

// Code by Liza Darwesh

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class InventoryActivity extends RecyclerView.Adapter<InventoryActivity.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<Bitmap> mImages = new ArrayList<>();
    private Context mContext;
    private ArrayList<Integer> mAantalDieren = new ArrayList<>();


    //Constructor die wordt geadapted in RecyclerView.java
    public InventoryActivity(Context context, ArrayList<String> imageNames, ArrayList<Bitmap> images,
                             ArrayList<Integer> aantalDieren) {
        mImageNames = imageNames;
        mImages = images;
        mContext = context;
        mAantalDieren = aantalDieren;
    }

    // Deze methode zorgt voor het vullen van acticity_detal.xml
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_detail, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Deze methode vult de widget circleimageview in activity_detail.xml met foto's
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(i))
                .into(viewHolder.image);
        viewHolder.imageName.setText(mImageNames.get(i));
        viewHolder.aantalDieren.setText(mAantalDieren.get(i).toString());

        //Hier wordt er gekeken of er op de foto's in de recyclerview wordt geklikt
        //om vervolgens naar de activity SwipeViewer te gaan.

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(i) + mAantalDieren.get(i));

                Toast.makeText(mContext, mImageNames.get(i), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, SwipeViewer.class);
                intent.putExtra("Animal ID", i);
                mContext.startActivity(intent);


            }
        });
    } // Deze methode vertelt hoeveel list items er in de recyclerView zitten

    @Override
    public int getItemCount() {


        return mImageNames.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;
        TextView aantalDieren;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            aantalDieren = itemView.findViewById(R.id.textView5);
        }
    }
}
