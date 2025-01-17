package com.minsudongP.ViewHolder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.minsudongP.R;

public class FollowUsersViewHolder extends RecyclerView.ViewHolder {
    public ImageView friendImage;
    public TextView friendName;
    public TextView friendID;
    public Button followButton;

    public FollowUsersViewHolder(@NonNull final View itemView) {
        super(itemView);

        friendImage = itemView.findViewById(R.id.friend_image);
        friendName = itemView.findViewById(R.id.friend_name);
        friendID = itemView.findViewById(R.id.follow_id);
        followButton = itemView.findViewById(R.id.follow_button);
    }
}
