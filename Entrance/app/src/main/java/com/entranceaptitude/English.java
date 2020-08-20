package com.entranceaptitude;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class English extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ShimmerFrameLayout setShimmer;
    DatabaseReference engRef;
    AdView mAdView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_english, container, false);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
         engRef= FirebaseDatabase.getInstance().getReference().child("English");
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeEnglish);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycleEnglish);
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
       // layoutManager.setReverseLayout(true);
       // layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        setShimmer=(ShimmerFrameLayout)view.findViewById(R.id.showShimmer);
        swipeRefreshLayout.setOnRefreshListener(this);
        setShimmer.setVisibility(View.VISIBLE);
        setShimmer.startShimmer();
        loadEnglish();
        return view;
    }

    @Override
    public void onRefresh() {
        setShimmer.stopShimmer();
        setShimmer.setVisibility(View.GONE);
        loadEnglish();
    }

    public void loadEnglish(){
        Query loadDecendingOrder=engRef.orderByChild("counter");
        FirebaseRecyclerAdapter<Model, QuestionBank.ViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Model, QuestionBank.ViewHolder>
                (Model.class,R.layout.pdf_holder, QuestionBank.ViewHolder.class,loadDecendingOrder) {
            @Override
            protected void populateViewHolder(QuestionBank.ViewHolder viewHolder, final Model model, int i) {
                final String key=getRef(i).getKey();
                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setTitle("Title");
                        final TextView editText=new TextView(getActivity());
                        editText.setText(model.getTitle());
                        builder.setView(editText);
                        Dialog dialogs=builder.create();
                        dialogs.show();

                   /*     CharSequence option[]=new CharSequence[]{
                                "Edit",
                                "Delete"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setTitle("Select Option");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    dialog.dismiss();
                                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Edit Post");
                                    final EditText editText=new EditText(getActivity());
                                    editText.setText(model.getTitle());
                                    builder.setView(editText);
                                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            engRef.child(key).child("title").setValue(editText.getText().toString());
                                            Toast.makeText(getActivity(), "Title Updated..", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    Dialog dialogs=builder.create();
                                    dialogs.show();
                                }
                                if (which==1){
                                    dialog.dismiss();
                                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Are You Sure ?");
                                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            engRef.child(key).removeValue();
                                        }
                                    });
                                    builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    Dialog dialogs=builder.create();
                                    dialogs.show();
                                }

                            }
                        });
                        builder.show(); */
                        return true;
                    }
                });
                viewHolder.setTitles(model.getTitle());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),ViewContent.class);
                        intent.putExtra("url",model.getUrl());
                        intent.putExtra("title",model.getTitle());
                        startActivity(intent);
                    }
                });
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                setShimmer.stopShimmer();
                setShimmer.setVisibility(View.GONE);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

}