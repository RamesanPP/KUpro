package com.eduzap.android.ui.drawer.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.InternetConnection;
import com.eduzap.android.R;
import com.eduzap.android.ui.drawer.MainActivity;
import com.eduzap.android.ui.drawer.home.Adapter.CoursesAdapter;
import com.eduzap.android.ui.drawer.home.Adapter.SliderAdapter;
import com.eduzap.android.ui.drawer.home.Interface.IFirebaseLoadListener;
import com.eduzap.android.ui.drawer.home.Model.CoursesModel;
import com.eduzap.android.ui.drawer.home.Model.SliderModel;
import com.eduzap.android.ui.drawer.home.Model.SubjectsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements IFirebaseLoadListener {
    //    private AlertDialog alertDialog;
    private SliderView sliderView;
    private IFirebaseLoadListener iFirebaseLoadListener;
    private RecyclerView courses_recycler_view;
    private ProgressBar progressBar;
    private DatabaseReference myData, imgSliderData;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ValueEventListener coursesAndSubjectsListener, imageSliderListener;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.drawer_fragment_home, container, false);

        //Views
        sliderView = root.findViewById(R.id.imageSlider);
        progressBar = root.findViewById(R.id.homeProgressBar);
        courses_recycler_view = root.findViewById(R.id.coursesRecyclerView);
        courses_recycler_view.setHasFixedSize(true);
        courses_recycler_view.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        alertDialog = new SpotsDialog.Builder().setContext(this.getActivity()).build();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // signed in

                } else {
                    //signed out
                    if (myData != null) {
                        myData.removeEventListener(coursesAndSubjectsListener);
                    }
                    if (imgSliderData != null) {
                        imgSliderData.removeEventListener(imageSliderListener);
                    }

                }
            }
        };
        firebaseAuth = FirebaseAuth.getInstance();

        if (!InternetConnection.checkConnection(getContext())) {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        loadCoursesAndSubjects();
        loadImageSlider();
        return root;
    }


    private void loadImageSlider() {
        imgSliderData = FirebaseDatabase.getInstance().getReference("ImageSlider");
        imgSliderData.keepSynced(true);
        imageSliderListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SliderModel> list = new ArrayList<SliderModel>();

                for (DataSnapshot groupSnapShot : snapshot.getChildren()) {
                    SliderModel sliderModel = new SliderModel();
                    sliderModel.setDescription(groupSnapShot.child("description").getValue(true).toString());
                    sliderModel.setImageUrl(groupSnapShot.child("imageUrl").getValue(true).toString());

                    list.add(sliderModel);
                }
                SliderAdapter adapter = new SliderAdapter(getContext());
                adapter.renewItems(list);

                sliderView.setSliderAdapter(adapter);

                sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                sliderView.setIndicatorSelectedColor(Color.WHITE);
                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(8);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();

                sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
                    @Override
                    public void onIndicatorClicked(int position) {
                        sliderView.setCurrentPagePosition(position);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading..", Toast.LENGTH_SHORT).show();
            }
        };
        imgSliderData.addListenerForSingleValueEvent(imageSliderListener);
    }

    private void loadCoursesAndSubjects() {

        //Init
        myData = FirebaseDatabase.getInstance().getReference("Courses");
        myData.keepSynced(true);
        iFirebaseLoadListener = this;

//        alertDialog.show();
        progressBar.setVisibility(View.VISIBLE);

        coursesAndSubjectsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CoursesModel> coursesList = new ArrayList<>();

                //for traversing all the child of the reference
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CoursesModel coursesItem = new CoursesModel();
                    coursesItem.setCourseTitle(snapshot.child("CourseTitle").getValue(true).toString());
                    GenericTypeIndicator<ArrayList<SubjectsModel>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<SubjectsModel>>() {
                    };
                    coursesItem.setSubjectItem(snapshot.child("SubjectItem").getValue(genericTypeIndicator));
                    coursesList.add(coursesItem);
                }
                iFirebaseLoadListener.onFirebaseLoadSuccess(coursesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadListener.FirebaseLoadFailed(databaseError.getMessage());
            }
        };
        myData.addListenerForSingleValueEvent(coursesAndSubjectsListener);
    }

    @Override
    public void onFirebaseLoadSuccess(List<CoursesModel> coursesModelList) {
        CoursesAdapter adapter = new CoursesAdapter(this.getActivity(), coursesModelList);
        courses_recycler_view.setAdapter(adapter);

//        alertDialog.dismiss();
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void FirebaseLoadFailed(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
//        alertDialog.dismiss();
        progressBar.setVisibility(View.GONE);

    }


    @Override
    public void onStart() {
        super.onStart();
        if (InternetConnection.checkConnection(getContext()) && firebaseAuth != null) {
            firebaseAuth.addAuthStateListener(authStateListener);
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_home_options_menu, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                return true;

            case R.id.action_refresh:
                Intent intent = new Intent(this.getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuth != null && InternetConnection.checkConnection(getContext())) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
