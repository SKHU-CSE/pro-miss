package com.minsudongP.Model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.minsudongP.FollowActivity;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.R;
import com.minsudongP.ViewHolder.AcceptViewHolder;
import com.minsudongP.ViewHolder.Add_FriendVIewHolder;
import com.minsudongP.ViewHolder.AppointStartViewHolder;
import com.minsudongP.ViewHolder.AttendViewHolder;
import com.minsudongP.ViewHolder.CancelViewHolder;
import com.minsudongP.ViewHolder.FollowViewHolder;
import com.minsudongP.ViewHolder.FriendViewHolder;

import com.minsudongP.ViewHolder.LateMemberViewHolder;
import com.minsudongP.ViewHolder.SearchViewHolder;

import com.minsudongP.ViewHolder.NewAppointViewHolder;
import com.minsudongP.ViewHolder.TimeLateViewHolder;


import java.util.ArrayList;

public class AllRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<PromissItem> arrayList;
    Activity activity;

    PromissClick click;

    public interface PromissClick{
        public void OnClick(View view,int position);
    }


    public AllRecyclerAdapter(ArrayList<PromissItem> arrayList, Activity activity)
    {
        this.arrayList=arrayList;
        this.activity=activity;

    }

    public void SetClickListner(PromissClick click)
    {
        this.click=click;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewposition) {

        RecyclerView.ViewHolder viewHolder=null;
        View view;

        switch (arrayList.get(viewposition).getType())
        {
            case FriendLIst:
                if (activity.getClass() == new FollowActivity().getClass()) {
                    view=activity.getLayoutInflater().inflate(R.layout.follow_card,viewGroup,false);
                    viewHolder=new FriendViewHolder(view);
                    break;
                } else {
                    view = activity.getLayoutInflater().inflate(R.layout.friendlist_card, viewGroup, false);
                    viewHolder = new FriendViewHolder(view);
                    break;
                }
            case FriendList_Grid:
                view=activity.getLayoutInflater().inflate(R.layout.add_friend_item,viewGroup,false);
                viewHolder=new Add_FriendVIewHolder(view);
                break;

            case SearchList:
                view=activity.getLayoutInflater().inflate(R.layout.searchlist_item,viewGroup,false);
                viewHolder=new SearchViewHolder(view);
                break;

            // -- 알림 카드뷰 -- //
            case New_Appoint:
                view=activity.getLayoutInflater().inflate(R.layout.alert_invite,viewGroup,false);
                viewHolder=new NewAppointViewHolder(view);
                break;

            case Time_Late:
                view=activity.getLayoutInflater().inflate(R.layout.alert_late,viewGroup,false);
                viewHolder=new TimeLateViewHolder(view);
                break;

            case Accept:
                view=activity.getLayoutInflater().inflate(R.layout.alert_accept,viewGroup,false);
                viewHolder=new AcceptViewHolder(view);
                break;

            case Cancel:
                view=activity.getLayoutInflater().inflate(R.layout.alert_cancel,viewGroup,false);
                viewHolder=new CancelViewHolder(view);
                break;

            case Appoint_START:
                view=activity.getLayoutInflater().inflate(R.layout.alert_timer,viewGroup,false);
                viewHolder=new AppointStartViewHolder(view);
                break;

            case Late_Member:
                view=activity.getLayoutInflater().inflate(R.layout.alert_safe,viewGroup,false);
                viewHolder=new LateMemberViewHolder(view);
                break;

            case Follow:
                view=activity.getLayoutInflater().inflate(R.layout.alert_follow,viewGroup,false);
                viewHolder=new FollowViewHolder(view);
                break;

            case Attend_Appoint:
                view=activity.getLayoutInflater().inflate(R.layout.attend_item,viewGroup,false);
                viewHolder=new AttendViewHolder(view);
                break;

            default:
                view=activity.getLayoutInflater().inflate(R.layout.friendlist_card,viewGroup,false);
                viewHolder=new FriendViewHolder(view);
                break;

        }


        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        switch (arrayList.get(i).getType())
        {
            case FriendLIst:
                BindFriendLIst(viewHolder,i);
                break;
            case FriendList_Grid:
                BindFriendList_Grid(viewHolder,i);
                break;
            case SearchList:
                BindSearchList(viewHolder,i);
                break;
            case New_Appoint:
                BindNew_Appoint(viewHolder,i);
                break;
            case Time_Late:
                BindTime_Late(viewHolder,i);
                break;
            case Accept:
                BindAccept(viewHolder,i);
                break;
            case Cancel:
                BindCancel(viewHolder,i);
                break;
            case Appoint_START:
                BindAppointStart(viewHolder,i);
                break;
            case Late_Member:
                BindLateMember(viewHolder,i);
                break;
            case Follow:
                BindFollow(viewHolder,i);
                break;
            case Attend_Appoint:
                BindAttend(viewHolder,i);
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    void BindSearchList(RecyclerView.ViewHolder viewHolder, final int position){
        SearchViewHolder holder=(SearchViewHolder)viewHolder;

        holder.address.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.OnClick(v,position);
            }
        });

        holder.address.setText(arrayList.get(position).getAddress());
        holder.jibun.setText(arrayList.get(position).getJibun());
    }

    void BindNew_Appoint(RecyclerView.ViewHolder viewHolder,int position)
    {
        NewAppointViewHolder holder=(NewAppointViewHolder)viewHolder;

        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.place.setText(arrayList.get(position).getPlace());
    }

    void BindTime_Late(RecyclerView.ViewHolder viewHolder,int position)
    {
        TimeLateViewHolder holder=(TimeLateViewHolder)viewHolder;

        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.place.setText(arrayList.get(position).getPlace());
        holder.money.setText(arrayList.get(position).getMoney());

    }

    void BindAccept(RecyclerView.ViewHolder viewHolder,int position)
    {
        AcceptViewHolder holder=(AcceptViewHolder)viewHolder;

        holder.name.setText(arrayList.get(position).getName());
        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.place.setText(arrayList.get(position).getPlace());
    }

    void BindCancel(RecyclerView.ViewHolder viewHolder,int position)
    {
        CancelViewHolder holder=(CancelViewHolder)viewHolder;

        holder.name.setText(arrayList.get(position).getName());
        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.place.setText(arrayList.get(position).getPlace());
    }

    void BindAppointStart(RecyclerView.ViewHolder viewHolder,int position)//약속이 시작됩니다
    {
        AppointStartViewHolder holder=(AppointStartViewHolder) viewHolder;

        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.place.setText(arrayList.get(position).getPlace());
    }

    void BindLateMember(RecyclerView.ViewHolder viewHolder,int position)//지각한 멤버는 *** 입니다
    {
        LateMemberViewHolder holder=(LateMemberViewHolder)viewHolder;

        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.place.setText(arrayList.get(position).getPlace());
        holder.member.setText(arrayList.get(position).getMember());
    }

    void BindFollow(RecyclerView.ViewHolder viewHolder,int position)
    {
        FollowViewHolder holder=(FollowViewHolder)viewHolder;

        holder.name.setText(arrayList.get(position).getName());
    }

    void BindAttend(RecyclerView.ViewHolder viewHolder, final int position)//Attend_Appoint
    {
        AttendViewHolder holder=(AttendViewHolder)viewHolder;

        holder.place.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.OnClick(v,position);
            }
        });

        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.place.setText(arrayList.get(position).getName());
    }



    void BindFriendLIst(RecyclerView.ViewHolder viewHolder,int position){

        FriendViewHolder holder=(FriendViewHolder)viewHolder;

        holder.friendName.setText(arrayList.get(position).getName());
        holder.friendImage.setImageResource(R.drawable.face);

    }

    void BindFriendList_Grid(RecyclerView.ViewHolder viewHolder,  int position)
    {
        Add_FriendVIewHolder holder=(Add_FriendVIewHolder)viewHolder;

        final int p=position;
        if(arrayList.get(position).getProfileImageURl().equals("추가하기"))
            holder.imageView.setImageResource(R.drawable.bt_add);
        holder.imageView.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.OnClick(v,p);
            }
        });

        holder.name.setText(arrayList.get(position).getName());
    }
}