package ui.adapter;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.R;

import java.util.ArrayList;
import java.util.List;

import model.Place;
import model.User;
import model.nearbysearchpojo.NearbySearchResponse;
import model.nearbysearchpojo.Result;
import repositories.NearbySearchRepositoryImpl;

/**
 *Adapter and ViewHolder to display a recycler view for the place list
 */

public class JoiningWorkmatesListAdapter extends RecyclerView.Adapter<JoiningWorkmatesListAdapter.ViewHolder> {

    private static List<String> mJoiningWorkmatesList = new ArrayList<>();

    public JoiningWorkmatesListAdapter(List<String> joiningWorkmatesList) {
        mJoiningWorkmatesList = joiningWorkmatesList;
    }

    public JoiningWorkmatesListAdapter() {
    }

    @NonNull
    @Override
    public JoiningWorkmatesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_workmates_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.displayWorkmates(mJoiningWorkmatesList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mJoiningWorkmatesList.isEmpty()) {
            return 0;
        }
        else {
            return mJoiningWorkmatesList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        private final ImageView avatar;

        private final TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.im_detail_workmate);
            name = itemView.findViewById(R.id.tv_joining_workmate);
        }

        private void displayWorkmates(String workmate) {
            //avatar.set...
            name.setText(workmate);
        }
    }
}
