package com.example.lucasalbuquerque.inectest.activities.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.lucasalbuquerque.inectest.R;

/**
 * Created by Lucas Albuquerque on 27/12/2017.
 */

public class ListHolder extends RecyclerView.ViewHolder {

    public TextView name, description, html_url, created_at, updated_at, pushed_at;

    public ListHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        description = (TextView) itemView.findViewById(R.id.description);
        html_url = (TextView) itemView.findViewById(R.id.html_url);
        created_at = (TextView) itemView.findViewById(R.id.created_at);
        updated_at = (TextView) itemView.findViewById(R.id.updated_at);
        pushed_at = (TextView) itemView.findViewById(R.id.pushed_at);
    }
}
