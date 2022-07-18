package di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import repositories.ApiRepository;
import repositories.ApiRepositoryImpl;

/**
*Hilt module for injections for API
*/

@InstallIn(SingletonComponent.class)
@Module
public abstract class ApiModule {
    @Binds
    public abstract ApiRepository bindApiRepository(ApiRepositoryImpl impl);
}
