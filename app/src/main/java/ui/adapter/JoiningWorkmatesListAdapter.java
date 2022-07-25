package ui.adapter;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.R;

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

    private static List<User> mPlaceList;

    public JoiningWorkmatesListAdapter(List<User> placeList) {
        mPlaceList = placeList;
    }

    @NonNull
    @Override
    public JoiningWorkmatesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }
    
    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView range;
        private final TextView style;
        private final TextView hyphen;
        private final TextView address;
        private final ImageView workmate;
        private final TextView workmateNumber;
        private final TextView open;
        private final ImageView star;
        private final ImageView placeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_place_name);
            range = itemView.findViewById(R.id.tv_place_range);
            style = itemView.findViewById(R.id.tv_place_style);
            hyphen = itemView.findViewById(R.id.tv_hyphen);
            address = itemView.findViewById(R.id.tv_place_address);
            workmate = itemView.findViewById(R.id.im_workmate);
            workmateNumber = itemView.findViewById(R.id.tv_workmate_number);
            open = itemView.findViewById(R.id.tv_place_open);
            star = itemView.findViewById(R.id.im_star);
            placeImage = itemView.findViewById(R.id.im_place);
        }

        private void displayPlace(Result place) {
            //TODO complete with API
            //name.setText(place.);
        }

        private void navigateToPlaceDetails() {
            //TODO complete with API and new activity
        }
    }
}
