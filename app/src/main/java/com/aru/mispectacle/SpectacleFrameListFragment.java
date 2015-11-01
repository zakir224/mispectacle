package com.aru.mispectacle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.aru.mispectacle.model.Spectacle;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 *
 */
public class SpectacleFrameListFragment extends Fragment implements ListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Spectacle> spectacles;
    private OnSpectacleSelectedListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;


    public static SpectacleFrameListFragment newInstance(String param1, String param2) {
        SpectacleFrameListFragment fragment = new SpectacleFrameListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SpectacleFrameListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        spectacles = new ArrayList<>();
        spectacles.add(new Spectacle("RayBan",1 ,55));
        spectacles.add(new Spectacle("Primark",2 ,60));
        spectacles.add(new Spectacle("Next",3 ,100));
        spectacles.add(new Spectacle("Blue Inc",4 ,99));
        spectacles.add(new Spectacle("M & S",5 ,199));
        spectacles.add(new Spectacle("John Lewis",6 ,205));
        spectacles.add(new Spectacle("RayBan",7 ,45));
        spectacles.add(new Spectacle("RayBan",2 ,35));
        spectacles.add(new Spectacle("SuperDry",7 ,30));
        spectacles.add(new Spectacle("RayBan",2 ,198));
        spectacles.add(new Spectacle("RayBan",1 ,30));
        spectacles.add(new Spectacle("Primark",3 ,20));
        spectacles.add(new Spectacle("RayBan",1 ,50));
        spectacles.add(new Spectacle("Next",2 ,40));
        spectacles.add(new Spectacle("RayBan",1 ,50));
        spectacles.add(new Spectacle("RayBan",2 ,89));

        // TODO: Change Adapter to display your content
        mAdapter = new SpectacleFrameAdapter(getActivity(),
                android.R.layout.simple_list_item_1, spectacles);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spectacleframe, container, false);
        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSpectacleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            //mListener.onFragmentInteraction(spectacles[position]);
        }
    }

    public interface OnSpectacleSelectedListener {
         void onSpectacleSelected(String id);
    }

}
