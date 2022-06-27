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

    public void createUserWithMail (String name, String mail, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid());
            User user = new User(userId, name, "https://gravatar.com/avatar/f91aabd46a5c3abef7fe229434346b38?s=400&d=identicon&r=x");
        });
    }
}
