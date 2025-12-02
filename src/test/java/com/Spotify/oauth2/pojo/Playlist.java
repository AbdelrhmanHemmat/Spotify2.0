package com.Spotify.oauth2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@com.fasterxml.jackson.databind.annotation.JsonDeserialize(builder = Playlist.PlaylistBuilder.class)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Playlist {

    private String name;
    private String description;
    private String ID;

    @JsonProperty("public") // maps Spotify API "public" field to isPublic
    private Boolean isPublic;

    Playlist(String name, String description, String ID, Boolean isPublic) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.isPublic = isPublic;
    }

    public static PlaylistBuilder builder() {
        return new PlaylistBuilder();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
    public static class PlaylistBuilder {
        private String name;
        private String description;
        private String ID;
        private Boolean isPublic;

        PlaylistBuilder() {
        }

        public PlaylistBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PlaylistBuilder description(String description) {
            this.description = description;
            return this;
        }

        public PlaylistBuilder ID(String ID) {
            this.ID = ID;
            return this;
        }

        @JsonProperty("public")
        public PlaylistBuilder isPublic(Boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        public Playlist build() {
            return new Playlist(this.name, this.description, this.ID, this.isPublic);
        }

    }
}
