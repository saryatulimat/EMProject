package abodx3.sar.emproject;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import abodx3.sar.emproject.DataBase.Modle.Recourd;

import java.util.List;


public class MyRecourdRecyclerViewAdapter extends RecyclerView.Adapter<SoundItem> {

    private final List<Recourd> Recourds;

    public MyRecourdRecyclerViewAdapter(List<Recourd> items) {
        Recourds = items;
    }

    @Override
    public SoundItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recourd, parent, false);
        return new SoundItem(view);
    }

    @Override
    public void onBindViewHolder(final SoundItem holder, int position) {
        holder.setRecourd(Recourds.get(position));
    }

    @Override
    public int getItemCount() {
        return Recourds.size();
    }


    //region s
     /*
    public class ViewHolder extends RecyclerView.ViewHolder {
       //public final View mView;
      //  public final TextView mIdView;
        //public final TextView mContentView;
      //  public DummyItem mItem;

        public ViewHolder(View view) {
          //  super(view);
            //mView = view;
            //mIdView = (TextView) view.findViewById(R.id.item_number);
            //mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
    */
    //endregion s
}