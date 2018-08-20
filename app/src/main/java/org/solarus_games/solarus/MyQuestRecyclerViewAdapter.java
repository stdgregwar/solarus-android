package org.solarus_games.solarus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.solarus_games.solarus.QuestFragment.OnListFragmentInteractionListener;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyQuestRecyclerViewAdapter extends RecyclerView.Adapter<MyQuestRecyclerViewAdapter.ViewHolder> {

    private final List<Quest> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyQuestRecyclerViewAdapter(List<Quest> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_quest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.quest = mValues.get(position);
        holder.mTitle.setText(holder.quest.title);
        holder.mShortDescription.setText(holder.quest.shortDescription);
        holder.mIcon.setImageBitmap(holder.quest.icon);

        holder.mView.findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != mListener) {
                    mListener.onQuestLaunchClick(holder.quest);
                }
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onQuestDetailClick(holder.quest);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mShortDescription;
        public final ImageView mIcon;
        public Quest quest;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //TODO setup view here
            mTitle = view.findViewById(R.id.quest_title);
            mShortDescription  = view.findViewById(R.id.quest_short_description);
            mIcon = view.findViewById(R.id.quest_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + quest.title + "'";
        }
    }
}
