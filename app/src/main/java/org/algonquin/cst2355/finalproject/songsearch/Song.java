    package org.algonquin.cst2355.finalproject.songsearch;

    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;
    /**
     * This class represents a song entity in the Room database
     * Author: Hoang Anh Nguyen - 041099695
     */
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
        @ColumnInfo(name="trackList")
        private String trackList;

        @ColumnInfo(name="SongTrackList")
        private String SongTrackList;

        @ColumnInfo(name="SongDuration")
        private String SongDuration;

        private String Song;

        /**
         * Gets the ID of the song.
         * @return The ID of the song.
         */
        public int getId() {

            return id;
        }
        /**
         * Sets the ID of the song.
         * @param id The ID of the song.
         */
        public void setId(int id) {

            this.id = id;
        }
        /**
         * Gets the artist of the song.
         * @return The artist of the song.
         */
        public String getArtist(){
            return artist;
        }
        /**
         * Sets the artist of the song.
         * @param artist The artist of the song.
         */
        public void setArtist(String artist){
            this.artist=artist;
        }


        /**
         * Gets the song.
         * @return The song.
         */
        public String getSong() {

            return Song;
        }

        /**
         * Sets the song.
         * @param Song The song.
         */
        public void setSong(String Song) {
            this.Song = Song;
        }
        /**
         * Gets the duration of the song.
         * @return The duration of the song.
         */
        public String getDuration() {
            return duration;
        }

        /**
         * Sets the duration of the song.
         * @param duration The duration of the song.
         */
        public void setDuration(String duration) {
            this.duration = duration;
        }
        /**
         * Gets the album name of the song.
         * @return The album name of the song.
         */
        public String getAlbumName() {
            return albumName;
        }
        /**
         * Sets the album name of the song.
         * @param albumName The album name of the song.
         */
        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }
        /**
         * Gets the album cover URL of the song.
         * @return The album cover URL of the song.
         */
        public String getAlbumCover() {
            return albumCover;
        }
        /**
         * Sets the album cover URL of the song.
         * @param albumCover The album cover URL of the song.
         */
        public void setAlbumCover(String albumCover) {
            this.albumCover = albumCover;
        }

        /**
         * Gets the title of the song.
         * @return The title of the song.
         */
        public String getTitle() {
            return title;
        }
        /**
         * Sets the title of the song.
         * @param title The title of the song.
         */
        public void setTitle(String title) {
            this.title = title;
        }
        /**
         * Gets the picture URL of the song.
         * @return The picture URL of the song.
         */
        public String getPicture(){
            return picture;
        }

        /**
         * Sets the picture URL of the song.
         * @param picture The picture URL of the song.
         */
        public void setPicture(String picture){
            this.picture=picture;
        }
        /**
         * Gets the tracklist URL of the song.
         * @return The tracklist URL of the song.
         */
        public String getTrackList(){
            return trackList;
        }
        /**
         * Sets the tracklist URL of the song.
         * @param trackList The tracklist URL of the song.
         */
        public void setTrackList(String trackList){
            this.trackList=trackList;
        }
        /**
         * Gets the song tracklist URL.
         * @return The song tracklist URL.
         */
        public String getSongTrackList() {
            return SongTrackList;
        }
        /**
         * Sets the song tracklist URL.
         * @param songTrackList The song tracklist URL.
         */
        public void setSongTrackList(String songTrackList) {
            SongTrackList = songTrackList;
        }
        /**
         * Gets the song duration.
         * @return The song duration.
         */
        public String getSongDuration() {
            return SongDuration;
        }
        /**
         * Sets the song duration.
         * @param songDuration The song duration.
         */
        public void setSongDuration(String songDuration) {
            SongDuration = songDuration;
        }

    }
