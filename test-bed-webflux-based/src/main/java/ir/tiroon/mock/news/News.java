package ir.tiroon.mock.news;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@JsonDeserialize(builder = News.Builder.class)
@Document(collection = "news")
public class News implements Serializable{

    @Id
    String id;

    String title;

    String body;

    Set<Long> attachmentsAssetIds;

    Long pictureAssetId;

    LocalDateTime publishTime;

    boolean published;

    @Indexed
    long fleetId;

//    @DBRef
//    @RelatedDocument
    Author author;

    @PersistenceConstructor
    public News(Author author, String id, @NotNull(message = "News title should not be null") String title, String body, Set<Long> attachmentsAssetIds, Long pictureAssetId, LocalDateTime publishTime, boolean published, @NotNull(message = "News Fleet Id should not be null") long fleetId) {
        this.author = author;
        this.id = id;
        this.title = title;
        this.body = body;
        this.attachmentsAssetIds = attachmentsAssetIds;
        this.pictureAssetId = pictureAssetId;
        this.publishTime = publishTime;
        this.published = published;
        this.fleetId = fleetId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Set<Long> getAttachmentsAssetIds() {
        return attachmentsAssetIds;
    }

    public Long getPictureAssetId() {
        return pictureAssetId;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public boolean isPublished() {
        return published;
    }

    public long getFleetId() {
        return fleetId;
    }

    private News(Builder builder) {
        id = builder.id;
        title = builder.title;
        body = builder.body;
        attachmentsAssetIds = builder.attachmentsAssetIds;
        pictureAssetId = builder.pictureAssetId;
        publishTime = builder.publishTime;
        published = builder.published;
        fleetId = builder.fleetId;
        author = builder.author;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder cloneBuilder() {
        return builder()
                .withId(id)
                .withPublishTime(publishTime)
                .withAttachmentsAssetIds(attachmentsAssetIds)
                .withBody(body)
                .withFleetId(fleetId)
                .withPictureAssetId(pictureAssetId)
                .withPublished(published)
                .withTitle(title)
                .withAuthor(author);
    }

    public static final class Builder {
        String id;
        String title;
        String body;
        Set<Long> attachmentsAssetIds;
        Long pictureAssetId;
        LocalDateTime publishTime;
        boolean published;
        long fleetId;
        Author author;

        private Builder() {
        }

        public static Builder aNews() {
            return new Builder();
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withBody(String body) {
            this.body = body;
            return this;
        }

        public Builder withAttachmentsAssetIds(Set<Long> attachmentsAssetIds) {
            this.attachmentsAssetIds = attachmentsAssetIds;
            return this;
        }

        public Builder withPictureAssetId(Long pictureAssetId) {
            this.pictureAssetId = pictureAssetId;
            return this;
        }

        public Builder withPublishTime(LocalDateTime publishTime) {
            this.publishTime = publishTime;
            return this;
        }

        public Builder withPublished(boolean published) {
            this.published = published;
            return this;
        }

        public Builder withFleetId(long fleetId) {
            this.fleetId = fleetId;
            return this;
        }

        public Builder withAuthor(Author author) {
            this.author = author;
            return this;
        }

        public News build() {
            return new News(this);
        }
    }

    public Author getAuthor() {
        return author;
    }

    public static class Author implements Serializable {

        public String firstName;
        public String lastName;
        public String email;

//        @PersistenceConstructor
        @JsonCreator
        public Author(@JsonProperty("firstName")String firstName, @JsonProperty("lastName") String lastName,
                      @JsonProperty("email") String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }
    }
}