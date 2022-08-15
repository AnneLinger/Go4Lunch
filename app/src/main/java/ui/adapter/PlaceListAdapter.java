package ui.adapter;

import static java.lang.String.format;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.BuildConfig;
import com.anne.linger.go4lunch.R;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import model.Booking;
import model.Place;
import model.nearbysearchpojo.NearbySearchResponse;
import model.nearbysearchpojo.OpeningHours;
import model.nearbysearchpojo.Result;
import repositories.NearbySearchRepositoryImpl;
import ui.activities.PlaceDetailsActivity;
import ui.activities.PlacesActivity;

/**
*Adapter and ViewHolder to display a recycler view for the place list
*/

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder> {

    private static List<Result> mPlaceList;
    private final Location mLocation;
    private final List<Booking> mBookingList;

    public PlaceListAdapter(List<Result> placeList, Location location, List<Booking> booking) {
        mPlaceList = placeList;
        mLocation = location;
        mBookingList = booking;
    }

    @NonNull
    @Override
    public PlaceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceListAdapter.ViewHolder holder, int position) {
        holder.displayPlace(mPlaceList.get(position), mLocation, mBookingList);
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView range;
        private final TextView address;
        private final TextView workmateNumber;
        private final TextView open;
        private final RatingBar rate;
        private final ImageView placeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_place_name);
            range = itemView.findViewById(R.id.tv_place_range);
            address = itemView.findViewById(R.id.tv_place_address);
            workmateNumber = itemView.findViewById(R.id.tv_workmate_number);
            open = itemView.findViewById(R.id.tv_place_open);
            rate = itemView.findViewById(R.id.rb_list_view_rate);
            placeImage = itemView.findViewById(R.id.im_place);
        }

        private void displayPlace(Result place, Location mLocation, List<Booking> mBookingList) {
            //For name
            name.setText(place.getName());
            //For range
            Location placeLocation = new Location("Place location");
            placeLocation.setLatitude(place.getGeometry().getLocation().getLat());
            placeLocation.setLongitude(place.getGeometry().getLocation().getLng());
            range.setText(String.format("%sm", Math.round(mLocation.distanceTo(placeLocation))));
            //For address
            address.setText(place.getVicinity());
            //For workmate number
            if(mBookingList.isEmpty()) {
                workmateNumber.setText("0");
            }
            else{
                for(Booking mBooking : mBookingList){
                    if(Objects.equals(mBooking.getPlaceId(), place.getPlaceId())){
                        //TODO manage numbers with joining workmates
                        workmateNumber.setText(format(Locale.getDefault(), "%d", 1));
                    }
                }
            }
            //For open or closed
            if(place.getOpeningHours()!=null) {
                open.setText(R.string.open_now);
            }
            else {
                open.setText(R.string.closed);
            }
            //For rating
            float rating = (float) ((place.getRating()/5)*3);
            rate.setRating(rating);
            //For place photo
            if(place.getPhotos()==null){
                placeImage.setImageResource(R.drawable.ic_baseline_restaurant_24);
            }
            else {
                String placePhoto = place.getPhotos().get(0).getPhotoReference();
                Glide.with(itemView)
                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=90&photo_reference=" + placePhoto + "&key=" + BuildConfig.MAPS_API_KEY)
                        .into(placeImage);
            }
            //For navigate to details from itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToPlaceDetails(place);
                }
            });
        }

        private void navigateToPlaceDetails(Result place) {
            Intent intent = new Intent(itemView.getContext(), PlaceDetailsActivity.class);
            intent.putExtra("place id", place.getPlaceId());
            itemView.getContext().startActivity(intent);
        }
    }
}
