package com.applications.whazzup.yandextranslator.ui.screens.translate_detail.favorite;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.FavoriteRealm;
import com.applications.whazzup.yandextranslator.data.storage.realm.TranslateRealm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    List<FavoriteRealm> list = new ArrayList<>();
    CustomClickListener mListener;

    public FavoriteAdapter(CustomClickListener listener) {
        mListener = listener;
    }

    public FavoriteAdapter(List<FavoriteRealm> list, CustomClickListener listener) {
        this.list = list;
        mListener = listener;
    }

    public void addItem(FavoriteRealm item){
        list.add(item);
        notifyDataSetChanged();
    }


    public void deleteItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    public FavoriteRealm getFavoriteFromPosition(int position){
        return list.get(position);
    }

    public void clearFavorite(){
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FavoriteRealm realm = list.get(position);
        holder.isFavoriteCheckBox.setChecked(true);
        holder.mOriginalText.setText(realm.getOriginalText());
        holder.mTranslateText.setText(realm.getTranslateText());
        holder.mDirection.setText(realm.getDirection());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.favorite_cbx)
        CheckBox isFavoriteCheckBox;
        @BindView(R.id.original_txt)
        TextView mOriginalText;
        @BindView(R.id.translate_txt)
        TextView mTranslateText;
        @BindView(R.id.direction_txt)
        TextView mDirection;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            isFavoriteCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.clickOnFavorite(getAdapterPosition());
            }
        }
    }

    public interface CustomClickListener{
        void clickOnFavorite(int position);
    }
}
