package com.oli.unipay.classes;

import static com.oli.unipay.util.Util.PriceFormat;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.oli.unipay.R;
import com.oli.unipay.model.PaymentItem;
import com.oli.unipay.model.UserItem;
import com.oli.unipay.util.Strings;
import com.oli.unipay.util.Util;
import java.util.Date;

public class Reports extends Activity {

    private final int tuition;
    private final UserItem userItem;
    private final Context context;
    private final FirebaseUser user;
    int paid;
    private FirestoreRecyclerAdapter<PaymentItem, ViewHolder> adapter;

    public Reports(int tuition, UserItem userItem, Context context, FirebaseUser user) {
        this.tuition = tuition;
        this.userItem = userItem;
        this.context = context;
        this.user = user;
    }

    public void loadLedger() {
        Dialog dialog = Util.dialog(context, R.layout.reports_dialog);

        TextView tvCredit = dialog.findViewById(R.id.tvCredit),
                tvDebit = dialog.findViewById(R.id.tvDebit),
                tvDate = dialog.findViewById(R.id.tvDate),
                tvStatus = dialog.findViewById(R.id.tvStatus);
        Button pBtn = dialog.findViewById(R.id.printBtn);


        RecyclerView recyclerView = dialog.findViewById(R.id.recView);

        Query query = Util.paymentsRef.document(user.getUid()).collection(Strings.sdata)
                .whereEqualTo("year", userItem.getYear()).whereEqualTo("sem", userItem.getSem());

        FirestoreRecyclerOptions<PaymentItem> options = new FirestoreRecyclerOptions.Builder<PaymentItem>()
                .setLifecycleOwner((LifecycleOwner) context)
                .setQuery(query, PaymentItem.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PaymentItem, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.report_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PaymentItem model) {
                holder.setData(model);
                paid += model.getPaid();
                tvCredit.setText(PriceFormat(paid));

                if (paid >= tuition) {
                    tvStatus.setText("Cleared");
                    tvStatus.getBackground().setColorFilter(context.getResources().getColor(R.color.primary_500), PorterDuff.Mode.MULTIPLY);
                }

            }

            @Override
            public void onDataChanged() {
              /*  if (adapter.getItemCount() > 0) {

                }*/
                Util.hideProgress(dialog.getWindow().getDecorView());

            }
        };

        pBtn.setOnClickListener(view -> {
            Toast.makeText(context, "No Printer Found", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        tvDate.setText(Util.dateFormat(new Date()));
        tvDebit.setText(PriceFormat(tuition));


        dialog.show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2, tv3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tvDate);
            tv2 = itemView.findViewById(R.id.tvPart);
            tv3 = itemView.findViewById(R.id.tvAmount);
        }

        private void setData(PaymentItem paymentItem) {
            tv1.setText(Util.dateFormat2(paymentItem.getTimestamp()));
            tv2.setText("UniPay");
            tv3.setText(Util.PriceFormat(paymentItem.getPaid()));
        }
    }
}
