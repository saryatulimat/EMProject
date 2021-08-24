package abodx3.sar.emproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import abodx3.sar.emproject.DataBase.Dao.EMDatabase;
import abodx3.sar.emproject.DataBase.Modle.Recourd;

/**
 * A fragment representing a list of Items.
 */
public class RecourdFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    MyRecourdRecyclerViewAdapter Adapter;

    public static List<Recourd> items;

    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecourdFragment() {

    }

    public void addRecourd(Recourd r) {
        items.add(r);
        Adapter.notifyDataSetChanged();
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecourdFragment newInstance(int columnCount) {
        RecourdFragment fragment = new RecourdFragment();
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
        EMDatabase db = EMDatabase.getConnection(getContext());
        items = db.RecourdsQuery().getAllRecourds();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recourd_list, container, false);
        Adapter = new MyRecourdRecyclerViewAdapter(RecourdFragment.items);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(Adapter);
        }
        return view;
    }
}