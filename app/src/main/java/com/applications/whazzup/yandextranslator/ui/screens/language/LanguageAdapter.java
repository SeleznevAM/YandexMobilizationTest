package com.applications.whazzup.yandextranslator.ui.screens.language;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.LangRealm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private List<LangRealm> mLanguageList = new ArrayList<>();
    private CustomClickListener mClickListener;


    public LanguageAdapter(CustomClickListener clickListener) {
        mClickListener = clickListener;
    }

    public LanguageAdapter(List<LangRealm> languageList) {
        mLanguageList = languageList;
    }

    public void addItem(LangRealm language){
        mLanguageList.add(language);
        Collections.sort(mLanguageList);
        notifyDataSetChanged();
    }

    public LangRealm getLangFromPosition(int position){
        return mLanguageList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       LangRealm language = mLanguageList.get(position);
        holder.mLanguage.setText(language.getLang());

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
