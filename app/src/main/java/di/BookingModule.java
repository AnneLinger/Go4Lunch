package di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import repositories.BookingRepository;
import repositories.BookingRepositoryImpl;

/**
*Hilt module for injections for booking
*/

@InstallIn(SingletonComponent.class)
@Module
public abstract class BookingModule {
    @Binds
    public abstract BookingRepository bindBookingRepository(BookingRepositoryImpl impl);
}
