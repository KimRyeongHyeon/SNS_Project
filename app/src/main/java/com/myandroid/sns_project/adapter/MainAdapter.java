package com.myandroid.sns_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myandroid.sns_project.FirebaseHelper;
import com.myandroid.sns_project.activity.PostActivity;
import com.myandroid.sns_project.PostInfo;
import com.myandroid.sns_project.R;
import com.myandroid.sns_project.Util;
import com.myandroid.sns_project.activity.WritePostActivity;
import com.myandroid.sns_project.listener.OnPostListener;
import com.myandroid.sns_project.view.ReadContentsView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private Activity activity;
    private final int MORE_INDEX = 2;
    private FirebaseHelper firebaseHelper;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public MainAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;

        firebaseHelper = new FirebaseHelper(activity);
    }

    public void setOnPostListener(OnPostListener onPostListener) {
        firebaseHelper.setOnPostListener(onPostListener);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PostActivity.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });

        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, mainViewHolder.getAdapterPosition());
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);

        PostInfo postInfo = mDataset.get(position);
        titleTextView.setText(postInfo.getTitle());

        ReadContentsView readContentsView = cardView.findViewById(R.id.readContentsView);
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);

        if(contentsLayout.getTag() == null || !contentsLayout.getTag().equals(postInfo)){
            contentsLayout.setTag(postInfo);
            contentsLayout.removeAllViews();

            readContentsView.setMoreIndex(MORE_INDEX);
            readContentsView.setPostInfo(postInfo);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void showPopup(View v, final int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.modify:
                        myStartActivity(WritePostActivity.class, mDataset.get(position));
                        return true;
                    case R.id.delete:
                        firebaseHelper.storageDelete(mDataset.get(position));
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }

    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("postInfo", postInfo);
        activity.startActivity(intent);
    }
}