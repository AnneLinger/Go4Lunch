package di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import repositories.UserRepositoryImpl;
import viewmodel.UserViewModel;

/**
*Constructor for ViewModels
*/
public class ViewModelFactory implements ViewModelProvider.Factory {
    //For data
    private final UserRepositoryImpl mUserRepositoryImpl;

    //for threads
    //private final Executor mExecutor;

    //For constructor
    private static ViewModelFactory sViewModelFactory;

    //Instance
    public static ViewModelFactory getInstance() {
        if (sViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (sViewModelFactory == null) {
                    sViewModelFactory = new ViewModelFactory(new UserRepositoryImpl());
                }
            }
        }
        return sViewModelFactory;
    }

    //Factory
    private ViewModelFactory(UserRepositoryImpl userRepositoryImpl) {
        mUserRepositoryImpl = new UserRepositoryImpl();
        //mExecutor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mUserRepositoryImpl);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
