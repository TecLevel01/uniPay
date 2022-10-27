package com.matt.unipay.activities;

import static com.matt.unipay.util.Strings.scourse;
import static com.matt.unipay.util.Strings.stuition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.matt.unipay.R;
import com.matt.unipay.model.UserItem;
import com.matt.unipay.util.Spacing;
import com.matt.unipay.util.Util;

public class StudentView extends AppCompatActivity {

    private RecyclerView recView;
    private String course;
    private FirestoreRecyclerAdapter<UserItem, ViewHolder> adapter;
    private int tuition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Students");

        mInit();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            course = bundle.getString(scourse);
            tuition = bundle.getInt(stuition);
        }
        getData();
    }

    private void mInit() {
        recView = findViewById(R.id.recView);
        recView.addItemDecoration(new Spacing.LinearItemDecoration(20, 20, 0));
    }

    private void getData() {
        Query query = Util.userRef.whereEqualTo("course", course).orderBy("paid", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<UserItem> options = new FirestoreRecyclerOptions.Builder<UserItem>()
                .setLifecycleOwner(this)
                .setQuery(query, UserItem.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<UserItem, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.student_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserItem model) {
                holder.setData(model, tuition);
            }

            @Override
            public void onDataChanged() {
                if (adapter.getItemCount() == 0) {
                }
                Util.hideProgress(getWindow().getDecorView());
            }
        };
        recView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return Util.onOptionsItemSelected(this, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2, tv3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
        }

        private void setData(UserItem userItem, int tuition) {
            tv1.setText(String.format("%s %s", userItem.getFname(), userItem.getLname()));
            tv2.setText(Util.PriceFormat(userItem.getPaid()));
            tv3.setText(String.format("%s - %s", userItem.getYear(), userItem.getSem()));

            itemView.setOnClickListener(view -> {
                Util.showStudentSheet(view.getContext(), userItem, tuition);
            });
        }
    }
}