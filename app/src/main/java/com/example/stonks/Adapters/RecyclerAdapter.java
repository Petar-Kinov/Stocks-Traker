package com.example.stonks.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonks.R;
import com.example.stonks.RequestClasses.FinantialModelingPrep.DailyMover;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<DailyMover> dailyMovers;
    private OnItemClickedListener mOnItemClickedListener;
    private static final String TAG = "RecyclerAdapter";

    public RecyclerAdapter(ArrayList<DailyMover> dailyMovers , OnItemClickedListener onItemClickedListener) {
        this.dailyMovers = dailyMovers;
        this.mOnItemClickedListener = onItemClickedListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView companyName, price, percentChange;
        OnItemClickedListener onItemClickedListener;

        public MyViewHolder(final View view , OnItemClickedListener onItemClickedListener){
            super(view);
            companyName = view.findViewById(R.id.overViewCompanyName);
            price = view.findViewById(R.id.overViewPrice);
            percentChange = view.findViewById(R.id.overViewPercentChange);
            this.onItemClickedListener = onItemClickedListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickedListener.onItemClick(dailyMovers.get(getAbsoluteAdapterPosition()).getTicker());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,parent,false);
        return new MyViewHolder(itemView , mOnItemClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.companyName.setText(dailyMovers.get(position).getCompanyName());
        holder.price.setText(dailyMovers.get(position).getPrice());
        holder.percentChange.setText(dailyMovers.get(position).getChangesPercentage());

    }

    @Override
    public int getItemCount() {
        return dailyMovers.size();
    }

    public interface  OnItemClickedListener{
        void onItemClick(String tickerSymbol);
    }
}
