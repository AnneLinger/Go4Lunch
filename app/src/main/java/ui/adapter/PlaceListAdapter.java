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
import model.nearbysearchpojo.NearbySearchResponse;
import repositories.NearbySearchRepositoryImpl;

/**
*Adapter and ViewHolder to display a recycler view for the place list
*/

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder> {

    private static List<NearbySearchResponse> mPlaceList;

    public PlaceListAdapter(List<NearbySearchResponse> placeList) {
        mPlaceList = placeList;
    }

    @NonNull
    @Override
    public PlaceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceListAdapter.ViewHolder holder, int position) {
        holder.displayPlace(mPlaceList.get(position));
        holder.navigateToPlaceDetails();
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

        private void displayPlace(NearbySearchResponse place) {
            //TODO complete with API
            //name.setText(place.);
        }

        private void navigateToPlaceDetails() {
            //TODO complete with API and new activity
        }
    }
}
