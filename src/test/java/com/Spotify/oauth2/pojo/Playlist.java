package com.Spotify.oauth2.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@com.fasterxml.jackson.databind.annotation.JsonDeserialize(builder = Playlist.PlaylistBuilder.class)
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Playlist {

    @JsonProperty("collaborative")
    private final
    Boolean collaborative;

    @JsonProperty("description")
    private final
    String description;

    @JsonProperty("external_urls")
    private final
    ExternalUrls externalUrls;

    @JsonProperty("followers")
    private final
    Followers followers;

    @JsonProperty("href")
    private final
    String href;

    @JsonProperty("id")
    private final
    String id;

    @JsonProperty("images")
    private final
    List<Object> images;

    @JsonProperty("name")
    private final
    String name;

    @JsonProperty("owner")
    private final
    Owner owner;

    @JsonProperty("primary_color")
    private final
    Object primaryColor;

    @JsonProperty("public")
    private final
    Boolean isPublic;

    @JsonProperty("snapshot_id")
    private final
    String snapshotId;

    @JsonProperty("tracks")
    private final
    Tracks tracks;

    @JsonProperty("type")
    private final
    String type;

    @JsonProperty("uri")
    private final
    String uri;

    Playlist(Boolean collaborative, String description, ExternalUrls externalUrls, Followers followers, String href, String id, List<Object> images, String name, Owner owner, Object primaryColor, Boolean isPublic, String snapshotId, Tracks tracks, String type, String uri) {
        this.collaborative = collaborative;
        this.description = description;
        this.externalUrls = externalUrls;
        this.followers = followers;
        this.href = href;
        this.id = id;
        this.images = images;
        this.name = name;
        this.owner = owner;
        this.primaryColor = primaryColor;
        this.isPublic = isPublic;
        this.snapshotId = snapshotId;
        this.tracks = tracks;
        this.type = type;
        this.uri = uri;
    }

    public static PlaylistBuilder builder() {
        return new PlaylistBuilder();
    }

    @com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
    public static class PlaylistBuilder {
        private Boolean collaborative;
        private String description;
        private ExternalUrls externalUrls;
        private Followers followers;
        private String href;
        private String id;
        private List<Object> images;
        private String name;
        private Owner owner;
        private Object primaryColor;
        private Boolean isPublic;
        private String snapshotId;
        private Tracks tracks;
        private String type;
        private String uri;

        PlaylistBuilder() {
        }

        @JsonProperty("collaborative")
        public PlaylistBuilder collaborative(Boolean collaborative) {
            this.collaborative = collaborative;
            return this;
        }

        @JsonProperty("description")
        public PlaylistBuilder description(String description) {
            this.description = description;
            return this;
        }

        @JsonProperty("external_urls")
        public PlaylistBuilder externalUrls(ExternalUrls externalUrls) {
            this.externalUrls = externalUrls;
            return this;
        }

        @JsonProperty("followers")
        public PlaylistBuilder followers(Followers followers) {
            this.followers = followers;
            return this;
        }

        @JsonProperty("href")
        public PlaylistBuilder href(String href) {
            this.href = href;
            return this;
        }

        @JsonProperty("id")
        public PlaylistBuilder id(String id) {
            this.id = id;
            return this;
        }

        @JsonProperty("images")
        public PlaylistBuilder images(List<Object> images) {
            this.images = images;
            return this;
        }

        @JsonProperty("name")
        public PlaylistBuilder name(String name) {
            this.name = name;
            return this;
        }

        @JsonProperty("owner")
        public PlaylistBuilder owner(Owner owner) {
            this.owner = owner;
            return this;
        }

        @JsonProperty("primary_color")
        public PlaylistBuilder primaryColor(Object primaryColor) {
            this.primaryColor = primaryColor;
            return this;
        }

        @JsonProperty("public")
        public PlaylistBuilder isPublic(Boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        @JsonProperty("snapshot_id")
        public PlaylistBuilder snapshotId(String snapshotId) {
            this.snapshotId = snapshotId;
            return this;
        }

        @JsonProperty("tracks")
        public PlaylistBuilder tracks(Tracks tracks) {
            this.tracks = tracks;
            return this;
        }

        @JsonProperty("type")
        public PlaylistBuilder type(String type) {
            this.type = type;
            return this;
        }

        @JsonProperty("uri")
        public PlaylistBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Playlist build() {
            return new Playlist(this.collaborative, this.description, this.externalUrls, this.followers, this.href, this.id, this.images, this.name, this.owner, this.primaryColor, this.isPublic, this.snapshotId, this.tracks, this.type, this.uri);
        }

        public String toString() {
            return "Playlist.PlaylistBuilder(collaborative=" + this.collaborative + ", description=" + this.description + ", externalUrls=" + this.externalUrls + ", followers=" + this.followers + ", href=" + this.href + ", id=" + this.id + ", images=" + this.images + ", name=" + this.name + ", owner=" + this.owner + ", primaryColor=" + this.primaryColor + ", isPublic=" + this.isPublic + ", snapshotId=" + this.snapshotId + ", tracks=" + this.tracks + ", type=" + this.type + ", uri=" + this.uri + ")";
        }
    }
}
