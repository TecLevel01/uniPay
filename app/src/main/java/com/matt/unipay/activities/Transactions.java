package com.matt.unipay.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.matt.unipay.R;
import com.matt.unipay.model.PaymentItem;
import com.matt.unipay.util.Spacing;
import com.matt.unipay.util.Strings;
import com.matt.unipay.util.Util;

public class Transactions extends AppCompatActivity {
    RecyclerView recView;
    private FirestoreRecyclerAdapter<PaymentItem, ViewHolder> adapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getClass().getSimpleName());

        mInit();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getData();
        }
    }

    private void getData() {
        Query query = Util.paymentsRef.document(user.getUid()).collection(Strings.sdata)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<PaymentItem> options = new FirestoreRecyclerOptions.Builder<PaymentItem>()
                .setLifecycleOwner(this)
                .setQuery(query, PaymentItem.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PaymentItem, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.payment_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PaymentItem model) {
                holder.setData(model);
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

    private void mInit() {
        recView = findViewById(R.id.recView);
        recView.addItemDecoration(new Spacing.LinearItemDecoration(20, 20, 0));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2, tv3;
        ImageView imageView, imgViewEnd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            imageView = itemView.findViewById(R.id.imgView);
            imgViewEnd = itemView.findViewById(R.id.imageViewEnd);
            imgViewEnd.setVisibility(View.GONE);
        }

        private void setData(PaymentItem paymentItem) {
            tv1.setText(Util.PriceFormat(paymentItem.getPaid()));
            tv2.setText(String.format("%s - %s", paymentItem.getYear(), paymentItem.getSem()));
            tv3.setText(Util.dateFormat(paymentItem.getTimestamp()));
        }
    }
}