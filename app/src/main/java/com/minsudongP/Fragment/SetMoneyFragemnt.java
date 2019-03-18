package com.minsudongP.Fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minsudongP.R;

import org.w3c.dom.Text;


public class SetMoneyFragemnt extends Fragment {
    private LinearLayout layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_2,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       layout=view.findViewById(R.id.frg_appoint2_addmoney);



       View.OnClickListener listener=new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               switch (view.getId()){
                   case R.id.frg_appoint2_deletebtn:

                       try{
                           final TextView money = (TextView) layout.getChildAt(layout.getChildCount() - 1);
                           money.animate()
                                   .alpha(0.1f)
                                   .setDuration(200)
                                   .setListener(new Animator.AnimatorListener() {
                                       @Override
                                       public void onAnimationStart(Animator animation) {

                                       }

                                       @Override
                                       public void onAnimationEnd(Animator animation) {
                                           layout.removeView(money);
                                       }

                                       @Override
                                       public void onAnimationCancel(Animator animation) {

                                       }

                                       @Override
                                       public void onAnimationRepeat(Animator animation) {

                                       }
                                   });

                       }catch (IndexOutOfBoundsException e)
                       {
                           e.printStackTrace();
                       }
                       break;
                   default:

                       if(layout.getChildCount()<9) {
                           TextView money2 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.addmoneytextview, null, false);
                           money2.setText(((Button) view).getText());

                           AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
                           animation.setDuration(400);
                           money2.startAnimation(animation);
                           layout.addView(money2);
                       }
                       break;
               }
           }
       };


        ((Button)view.findViewById(R.id.frg_appoint2_onebtn)).setOnClickListener(listener);
        ((Button)view.findViewById(R.id.frg_appoint2_deletebtn)).setOnClickListener(listener);
        ((Button)view.findViewById(R.id.frg_appoint2_twobtn)).setOnClickListener(listener);
    }

}