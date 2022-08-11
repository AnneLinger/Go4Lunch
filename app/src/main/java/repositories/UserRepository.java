package repositories;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 *Interface repository for users and their authentication
 */

public interface UserRepository {

    FirebaseUser getCurrentUser();

    List<FirebaseUser> getAllUsers();

    void createUser();

    void logOut();

}
