package com.example.project_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class RecentRecyclerViewAdapter extends RecyclerView.Adapter<RecentRecyclerViewAdapter.ViewHolder> implements Filterable {


    ArrayList<Problem> arrayList;
    ArrayList<Problem> arrayListFilter;

    public RecentRecyclerViewAdapter(ArrayList<Problem> arrayList) {
        this.arrayList = arrayList;
        arrayListFilter = new ArrayList<>();

        arrayListFilter.addAll(arrayList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNumber, tvProblem;
        private ImageButton btnFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvShowProblemNumber);
            tvProblem = itemView.findViewById(R.id.tvShowProblemName);
            btnFavorite = itemView.findViewById(R.id.btnFavoriteShow);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(itemView.getContext(), arrayList.get(getAdapterPosition()).getProblemName()
//                    , Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("problemName", arrayList.get(getAdapterPosition()).getProblemName());
            v.getContext().startActivity(new Intent(v.getContext(), ProblemActivity.class).putExtras(bundle));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.problem_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNumber.setText(arrayList.get(position).getProblemNumber());
        holder.tvProblem.setText(arrayList.get(position).getProblemName());
        if (arrayList.get(position).isFavorite()) {
            holder.btnFavorite.setImageResource(R.drawable.heart_red);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.heart_white);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    Filter mFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Problem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0 || constraint != "recent") {
                filteredList.addAll(arrayListFilter);
            } else {

                for (Problem problem: arrayListFilter) {
                    if (filteredList.size() < 5) {
                        filteredList.add(problem);
                    } else {
                        break;
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((Collection<? extends  Problem>) results.values);
            notifyDataSetChanged();
        }
    };
}