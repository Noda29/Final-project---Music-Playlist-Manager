package model;

public class PodcastEpisode extends Song{
    private String podcastName;
    private int episodeNumber;

    public PodcastEpisode(String title, String artist, int duration, String genre, String podcastName, int episodeNumber) {
        super(title, artist, duration, genre);
        this.podcastName = podcastName;
        this.episodeNumber = episodeNumber;
    }

    public String getPodcastName() {
        return podcastName;
    }
    public int getEpisodeNumber() {
        return episodeNumber;
    }
    public void setPodcastName(String podcastName) {
        this.podcastName = podcastName;
    }
    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String toString() {
        return "[Podcast] " + podcastName + " #" + episodeNumber + " — " + getTitle();
    }
}
