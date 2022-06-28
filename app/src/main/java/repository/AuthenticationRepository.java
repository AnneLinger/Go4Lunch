package repository;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import model.User;

/**
*Repository for authentication of the users
*/

public class AuthenticationRepository {
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    public AuthenticationRepository() {
    }

    public void loginWithMail(String mail, String password){
        mFirebaseAuth.signInWithEmailAndPassword(mail, password);
    }

    public void createUserWithMail (String name, String mail, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid());
            User user = new User(userId, name, "https://picsum.photos/200");
        });
    }

    public void logOut() {
        mFirebaseAuth.signOut();
    }
}
