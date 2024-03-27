    package org.algonquin.cst2355.finalproject.songsearch;

    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;

    @Entity
    public class Song {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name="id")
        private int id;

        //private String word;
        @ColumnInfo(name="duration")
        private String duration;
        @ColumnInfo(name="title")
        private String title;
        @ColumnInfo(name="artist")
        private String artist;
        @ColumnInfo(name="albumName")
        private String albumName;
        @ColumnInfo(name="albumCover")
        private String albumCover;
        @ColumnInfo(name="picture")
        private String picture;

        private String Song;


        public int getId() {

            return id;
        }

        public void setId(int id) {

            this.id = id;
        }

        public String getArtist(){
            return artist;
        }
        public void setArtist(String artist){
            this.artist=artist;
        }



        public String getSong() {

            return Song;
        }

        public void setSong(String Song) {

            this.Song = Song;
        }

        public String getDuration() {
            return duration;
        }
        public void setDuration(String duration) {
            this.duration = duration;
        }
        public String getAlbumName() {
            return albumName;
        }
        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getAlbumCover() {
            return albumCover;
        }
        public void setAlbumCover(String albumCover) {
            this.albumCover = albumCover;
        }
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPicture(){
            return picture;
        }

        public void setPicture(String picture){
            this.picture=picture;
        }








    }
