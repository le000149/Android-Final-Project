package org.algonquin.cst2355.finalproject.sunrisesunset;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "latitude")
    private String latitude;
    @ColumnInfo(name = "longitude")
    private String longitude;

    public Location(){}
    public Location(String latitude, String longtitude){
        this.latitude=latitude;
        this.longitude=longtitude;
    }
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getLatitude(){return latitude;}
    public void setLatitude(String lat){
        this.latitude=lat;
    }
    public String getLongitude(){
        return longitude;
    }
    public void setLongitude(String log){
        this.longitude=log;
    }
}
