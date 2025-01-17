package com.minsudongP;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.minsudongP.Model.AllRecyclerAdapter;
import com.minsudongP.Model.PromissItem;
import com.minsudongP.Model.PromissType;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import java.util.ArrayList;

public class PastActivity extends BaseActivity {
    RecyclerView recyclerView;
    AllRecyclerAdapter adapter;
    ArrayList<PromissItem> arrayList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past);

        recyclerView=findViewById(R.id.past_recycle);
        adapter=new AllRecyclerAdapter(arrayList,PastActivity.this);

        arrayList.add(new PromissItem(PromissType.Past_Appoint,"성공회대","19.05.21","5","3","1000원","3000원"));
        arrayList.add(new PromissItem(PromissType.Past_Appoint,"홍대 마라샹궈","19.05.29","3","3","0원","0원"));
        arrayList.add(new PromissItem(PromissType.Past_Appoint,"볼링장","19.06.22","5","4","0원","3421원"));

        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(PastActivity.this));
        recyclerView.setAdapter(adapter);



        View.OnClickListener BackListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        ((Button)findViewById(R.id.past_backButton)).setOnClickListener(BackListener);
    }
}
