package com.applications.whazzup.yandextranslator.ui.screens.favorite;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.applications.whazzup.yandextranslator.R;
import com.applications.whazzup.yandextranslator.data.storage.realm.FavoriteRealm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<FavoriteRealm> list = new ArrayList<>();
    private CustomClickListener mListener;

    FavoriteAdapter(CustomClickListener listener) {
        mListener = listener;
    }

    public FavoriteAdapter(List<FavoriteRealm> list, CustomClickListener listener) {
        this.list = list;
        mListener = listener;
    }

    void addItem(FavoriteRealm item){
        list.add(item);
        notifyDataSetChanged();
    }


    void deleteItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    FavoriteRealm getFavoriteFromPosition(int position){
        return list.get(position);
    }

    void clearFavorite(){
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




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.favorite_cbx)
        CheckBox isFavoriteCheckBox;
        @BindView(R.id.original_txt)
        TextView mOriginalText;
        @BindView(R.id.translate_txt)
        TextView mTranslateText;
        @BindView(R.id.direction_txt)
        TextView mDirection;

        ViewHolder(View itemView) {
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

    interface CustomClickListener{
        void clickOnFavorite(int position);
    }
}
