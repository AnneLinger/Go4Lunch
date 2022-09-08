package di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import repositories.AutocompleteRepository;
import repositories.AutocompleteRepositoryImpl;

/**
 * Hilt module for injections for autocomplete
 */

@InstallIn(SingletonComponent.class)
@Module
public abstract class AutocompleteModule {
    @Binds
    public abstract AutocompleteRepository bindAutocompleteRepository(AutocompleteRepositoryImpl impl);
}
