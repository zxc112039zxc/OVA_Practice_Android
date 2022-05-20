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

    /**上方的arrayList為RecyclerView所綁定的ArrayList*/
    ArrayList<Problem> arrayList;
    /**儲存最原先ArrayList的狀態(也就是充當回複RecyclerView最原先狀態的陣列)*/
    ArrayList<Problem> arrayListFilter;

    public RecentRecyclerViewAdapter(ArrayList<Problem> arrayList) {
        this.arrayList = arrayList;
        arrayListFilter = new ArrayList<>();
        /**這裡把初始陣列複製進去了*/
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
            /**點擊事件*/
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
    /**使用Filter濾除方法*/
    Filter mFilter = new Filter() {
        /**此處為正在濾除字串時所做的事*/
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            /**先將完整陣列複製過去*/
            ArrayList<Problem> filteredList = new ArrayList<>();
            /**如果沒有輸入，則將原本的陣列帶入*/
            if (constraint == null || constraint.length() == 0 || constraint != "recent") {
                filteredList.addAll(arrayListFilter);
            } else {
                /**如果有輸入，則照順序濾除相關字串
                 * 如果你有更好的搜尋演算法，就是寫在這邊*/
                for (Problem problem: arrayListFilter) {
                    if (filteredList.size() < 5) {
                        filteredList.add(problem);
                    } else {
                        break;
                    }
                }
            }
            /**回傳濾除結果*/
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }
        /**將濾除結果推給原先RecyclerView綁定的陣列，並通知RecyclerView刷新*/
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((Collection<? extends  Problem>) results.values);
            notifyDataSetChanged();
        }
    };
}