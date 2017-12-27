package com.example.lucasalbuquerque.inectest.activities.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucasalbuquerque.inectest.R;
import com.example.lucasalbuquerque.inectest.activities.Models.Repository;

import java.util.List;

/**
 * Created by Lucas Albuquerque on 27/12/2017.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<ListHolder> {

    private final List<Repository> mRepository;

    public RepositoryAdapter(List<Repository> repositorys) {
        mRepository = repositorys;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_line_view, parent, false);

        ListHolder holder = new ListHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        holder.name.setText(mRepository.get(position).getName());
        holder.description.setText(mRepository.get(position).getDescription());
        holder.html_url.setText(mRepository.get(position).getHtml_url());
        holder.created_at.setText(mRepository.get(position).getCreated_at().replace("T", " - ").replace("Z", ""));
        holder.updated_at.setText(mRepository.get(position).getUpdated_at().replace("T", " - ").replace("Z", ""));
        holder.pushed_at.setText(mRepository.get(position).getPushed_at().replace("T", " - ").replace("Z", ""));
    }

    @Override
    public int getItemCount() {
        return mRepository != null ? mRepository.size() : 0;
    }

}