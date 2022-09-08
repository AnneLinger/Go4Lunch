package di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import repositories.NearbySearchRepository;
import repositories.NearbySearchRepositoryImpl;

/**
 * Hilt module for injections for nearbysearch places
 */

@InstallIn(SingletonComponent.class)
@Module
public abstract class NearbySearchModule {
    @Binds
    public abstract NearbySearchRepository bindApiRepository(NearbySearchRepositoryImpl impl);
}
