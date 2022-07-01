package di;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import repositories.UserRepository;
import repositories.UserRepositoryImpl;

/**
*Hilt module for injections for User
*/
@InstallIn(SingletonComponent.class)
@Module
public abstract class UserModule {
    @Binds
    public abstract UserRepository bindUserRepository(UserRepositoryImpl impl);
}
