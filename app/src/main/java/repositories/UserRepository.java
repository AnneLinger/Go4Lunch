package repositories;

import com.google.firebase.auth.FirebaseUser;

/**
 *Interface repository for users and their authentication
 */

public interface UserRepository {

    FirebaseUser getCurrentUser();

    void createUser();

    void logOut();

}
