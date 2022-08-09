package ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
*Adapter and ViewHolder to display a recycler view for the workmates list
*/

public class WorkmatesListAdapter extends RecyclerView.Adapter<WorkmatesListAdapter.ViewHolder> {

    private static List<FirebaseUser> mUserList;

    public WorkmatesListAdapter(List<FirebaseUser> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public WorkmatesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.displayWorkmate(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final TextView name;
        private final TextView eating;
        private final TextView place;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.im_workmate);
            name = itemView.findViewById(R.id.tv_workmate_name);
            eating = itemView.findViewById(R.id.tv_workmate_eating);
            place = itemView.findViewById(R.id.tv_workmate_place);
        }

        private void displayWorkmate(FirebaseUser user){
            //For avatar
            if(user.getPhotoUrl()!=null) {
                Glide.with(itemView)
                        .load(user.getPhotoUrl())
                        .circleCrop()
                        .into(avatar);
            }
            //For name
            name.setText(user.getDisplayName());
            //For eating or not
        }
    }
}
