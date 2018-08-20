package org.solarus_games.solarus;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestDetails extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PATH = "path";

    private Quest mQuest;

    private OnFragmentInteractionListener mListener;

    public QuestDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param path the path of the quest to show
     * @return A new instance of fragment QuestDetails.
     */
    public static QuestDetails newInstance(String path) {
        QuestDetails fragment = new QuestDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuest = Quest.fromPath(getArguments().getString(ARG_PATH));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_quest_details, container, false);
        final ImageButton b = v.findViewById(R.id.delete_button);
        TextView title = v.findViewById(R.id.title_text);
        title.setText(mQuest.title);
        TextView description = v.findViewById(R.id.description_text);
        description.setText(mQuest.longDescription);
        TextView author = v.findViewById(R.id.author_text);
        author.setText(mQuest.author);
        TextView version = v.findViewById(R.id.version_text);
        version.setText(mQuest.version);
        TextView format = v.findViewById(R.id.format_text);
        format.setText(mQuest.format);
        TextView path = v.findViewById(R.id.path_text);
        path.setText(mQuest.path);
        ImageView logo = v.findViewById(R.id.quest_logo);
        logo.setImageBitmap(mQuest.logo);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteButtonPressed();
            }
        });
        return v;
    }


    public void onDeleteButtonPressed() {
        if (mListener != null) {
            mListener.onQuestDeleteRequest(mQuest);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onQuestDeleteRequest(Quest q);
    }
}
