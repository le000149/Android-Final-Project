package algonquin.cst2335.testsun;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
interface CityDao {
    @Insert
    void insert(City city);

    @Delete
    void delete(City city);  // For deleting by city object, typically requires a primary key in the city object



    @Query("DELETE FROM city WHERE name = :cityName")
    void deleteCity(String cityName);  // For deleting by city name

    @Query("SELECT * FROM city")
    LiveData<List<City>> getAllCities();


    @Query("DELETE FROM city")
    void deleteAllCities();

    @Query("SELECT * FROM city WHERE name = :cityName")
    List<City> getCitiesByName(String cityName);



}
