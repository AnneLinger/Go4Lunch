package di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import repositories.PlaceDetailsRepository;
import repositories.PlaceDetailsRepositoryImpl;

/**
 * Hilt module for injections for place details
 */

@InstallIn(SingletonComponent.class)
@Module
public abstract class PlaceDetailsModule {
    @Binds
    public abstract PlaceDetailsRepository bindApiRepository(PlaceDetailsRepositoryImpl impl);
}
