package com.eduzap.android.ui.drawer.my_profile;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eduzap.android.InternetConnection;
import com.eduzap.android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PlacementFragment extends Fragment {

    private TextView supportEmailTV, facebookTV, instagramTV, telegramTV, twitterTV, websiteTV;
    private TextView about_1_title, about_1_description, about_2_title, about_2_description, about_3_title, about_3_description, about_4_title, about_4_description;
    private ImageView about_1_image, about_2_image, about_3_image, about_4_image;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root;
        root = inflater.inflate(R.layout.drawer_fragment_placement, container, false);

        about_1_title = root.findViewById(R.id.about_title_1);
        about_1_description = root.findViewById(R.id.about_description_1);
        about_1_image = root.findViewById(R.id.about_image_1);

        about_2_title = root.findViewById(R.id.about_title_2);
        about_2_description = root.findViewById(R.id.about_description_2);
        about_2_image = root.findViewById(R.id.about_image_2);

        about_3_title = root.findViewById(R.id.about_title_3);
        about_3_description = root.findViewById(R.id.about_description_3);
        about_3_image = root.findViewById(R.id.about_image_3);

        about_4_title = root.findViewById(R.id.about_title_4);
        about_4_description = root.findViewById(R.id.about_description_4);
        about_4_image = root.findViewById(R.id.about_image_4);

        supportEmailTV = root.findViewById(R.id.support_email);
        facebookTV = root.findViewById(R.id.facebook);
        instagramTV = root.findViewById(R.id.instagram);
        telegramTV = root.findViewById(R.id.telegram);
        twitterTV = root.findViewById(R.id.twitter);
        websiteTV = root.findViewById(R.id.website);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference contactUsRef = ref.child("ContactUsPlacement");

        contactUsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("support_mail").getValue(true).toString();
                String facebook = dataSnapshot.child("facebook").getValue(true).toString();
                String instagram = dataSnapshot.child("instagram").getValue(true).toString();
                String telegram = dataSnapshot.child("telegram").getValue(true).toString();
                String website = dataSnapshot.child("website").getValue(true).toString();
                String twitter = dataSnapshot.child("twitter").getValue(true).toString();
                supportEmailTV.setText(email);
                facebookTV.setText(facebook);
                instagramTV.setText(instagram);
                telegramTV.setText(telegram);
                websiteTV.setText(website);
                twitterTV.setText(twitter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading contact details", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference aboutUsRef1 = ref.child("Placement").child("0");
        aboutUsRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String title = snapshot.child("title").getValue(true).toString();
                String description = snapshot.child("description").getValue(true).toString();
                String image = snapshot.child("imageUrl").getValue(true).toString();
                about_1_title.setText(title);
                about_1_description.setText(description);
                Picasso.get().load(image).into(about_1_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading About Us 1 details", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference aboutUsRef2 = ref.child("Placement").child("1");
        aboutUsRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String title = snapshot.child("title").getValue(true).toString();
                String description = snapshot.child("description").getValue(true).toString();
                String image = snapshot.child("imageUrl").getValue(true).toString();
                about_2_title.setText(title);
                about_2_description.setText(description);
                Picasso.get().load(image).into(about_2_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading About Us 2 Details", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference aboutUsRef3 = ref.child("Placement").child("2");
        aboutUsRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String title = snapshot.child("title").getValue(true).toString();
                String description = snapshot.child("description").getValue(true).toString();
                String image = snapshot.child("imageUrl").getValue(true).toString();
                about_3_title.setText(title);
                about_3_description.setText(description);
                Picasso.get().load(image).into(about_3_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading About Us 2 Details", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference aboutUsRef4 = ref.child("Placement").child("3");
        aboutUsRef4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String title = snapshot.child("title").getValue(true).toString();
                String description = snapshot.child("description").getValue(true).toString();
                String image = snapshot.child("imageUrl").getValue(true).toString();
                about_4_title.setText(title);
                about_4_description.setText(description);
                Picasso.get().load(image).into(about_4_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading About Us 2 Details", Toast.LENGTH_SHORT).show();
            }
        });

        if (!InternetConnection.checkConnection(getContext())) {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }
        return root;
    }
}