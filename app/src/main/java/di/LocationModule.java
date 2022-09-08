package di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import repositories.LocationRepository;
import repositories.LocationRepositoryImpl;

/**
 * Hilt module for injections for Location
 */

@InstallIn(SingletonComponent.class)
@Module
public abstract class LocationModule {
    @Binds
    public abstract LocationRepository bindLocationRepository(LocationRepositoryImpl impl);
}
