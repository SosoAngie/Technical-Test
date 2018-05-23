package com.example.angel.technicaltest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class UserReposListAdapter extends ArrayAdapter<Repo>{

    public UserReposListAdapter(Context context, List<Repo> repo){
        super(context, 0, repo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_repo_item,parent,false);
        }

        RepoViewHolder repoViewHolder = (RepoViewHolder) convertView.getTag();
        if(repoViewHolder == null){
            repoViewHolder = new RepoViewHolder();
            repoViewHolder.name = (TextView) convertView.findViewById(R.id.repoName);
            repoViewHolder.description = (TextView) convertView.findViewById(R.id.repoDescription);
            repoViewHolder.watcherCount = (TextView) convertView.findViewById(R.id.repoWatcherCount);
            convertView.setTag(repoViewHolder);
        }

        //get repo
        Repo repo = getItem(position);

        //fill view
        repoViewHolder.name.setText(repo.getName());
        repoViewHolder.description.setText(repo.getDescription());
        repoViewHolder.watcherCount.setText(Integer.toString(repo.getWatcherCount()));


        return convertView;
    }


    private class RepoViewHolder{
        public TextView name;
        public TextView description;
        public TextView watcherCount;
    }
}
