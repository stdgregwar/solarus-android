package org.solarus_games.solarus;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collector;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class QuestFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Quest> mQuests;
    private static final String QUESTS_PATHS_KEY = "QUEST_LIST";
    private static final String TAG = "QuestFragment";
    private RecyclerView.Adapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QuestFragment newInstance(int columnCount) {
        QuestFragment fragment = new QuestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quest_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Set<String> questsPaths = getActivity().getPreferences(Context.MODE_PRIVATE).getStringSet(QUESTS_PATHS_KEY,new HashSet<String>());
            mQuests = new ArrayList<>();
            for(String path : questsPaths) {
                mQuests.add(Quest.fromPath(path));
            }
            Collections.sort(mQuests);
            mAdapter = new MyQuestRecyclerViewAdapter(mQuests, mListener);
            ViewCompat.setNestedScrollingEnabled(recyclerView,false);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    void addQuest(String path) {
        Quest q = Quest.fromPath(path);
        if(q.valid && !mQuests.contains(q)) {
            mQuests.add(q);
            Collections.sort(mQuests);
            mAdapter.notifyDataSetChanged();
        }
        if(!q.valid) {
            //Send error message
            Toast.makeText(getContext(),"Invalid quest : '" + q.path + "'",Toast.LENGTH_LONG).show();
        }
        saveQuests();
    }

    void removeQuest(Quest q) {
        mQuests.remove(q);
        mAdapter.notifyDataSetChanged();
        saveQuests();
    }

    void saveQuests() {
        Set<String> paths = new HashSet<>();
        for(Quest q : mQuests) {
            paths.add(q.path);
        }
        getActivity().getPreferences(Context.MODE_PRIVATE).edit()
                .putStringSet(QUESTS_PATHS_KEY,paths).apply();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onQuestDetailClick(Quest quest);
        void onQuestLaunchClick(Quest quest);
    }
}
