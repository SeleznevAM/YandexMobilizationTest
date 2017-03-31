package com.applications.whazzup.yandextranslator.ui.screens.language;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.applications.whazzup.yandextranslator.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private List<String> mLanguageList = new ArrayList<>();
    private CustomClickListener mClickListener;

    public LanguageAdapter() {

    }

    public LanguageAdapter(CustomClickListener clickListener) {
        mClickListener = clickListener;
    }

    public LanguageAdapter(List<String> languageList) {
        mLanguageList = languageList;
    }

    public void addItem(String language){
        mLanguageList.add(language);
        Collections.sort(mLanguageList);
        notifyDataSetChanged();
    }

    public String getLangFromPosition(int position){
        return mLanguageList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String s = mLanguageList.get(position);
        holder.mLanguage.setText(s);

    }

    @Override
    public int getItemCount() {
        return mLanguageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.language_item_text)
        TextView mLanguage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mLanguage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mClickListener != null){
                mClickListener.onLanguageClickListener(getAdapterPosition());
            }
        }
    }

    public interface CustomClickListener{
        void onLanguageClickListener(int position);
    }
}
