package ir.tiroon.mock.news;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequestMapping("/api/news")
@RestController
class NewsResourcesMock {

    News news1 = News.builder()
            .withAttachmentsAssetIds(Set.of(12L, 13L))
            .withAuthor(new News.Author("firsName", "lastName", "email"))
            .withBody("body")
            .withFleetId(111L)
            .withId("1")
            .withPictureAssetId(13123L)
            .withTitle("title")
            .build();
    News news2 = News.builder()
            .withAttachmentsAssetIds(Set.of(12L, 13L))
            .withAuthor(new News.Author("firsName", "lastName", "email"))
            .withBody("body2")
            .withFleetId(222L)
            .withId("2")
            .withPictureAssetId(13123L)
            .withTitle("title2")
            .build();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Mono<News> createNews(@RequestBody Mono<News> newsToSave) {
        return Mono.just(news1);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    Mono<News> updateNews(@RequestBody Mono<News> newsToUpdate) {
        return newsToUpdate;
    }

    @GetMapping
    @ResponseBody
    Flux<News> getList(@RequestParam(value = "fleetId", required = false) Optional<String> fleetIdOptional, @RequestParam(value = "published", required = false) Optional<Boolean> publishedOptional) {
        return Flux.fromIterable(List.of(news1, news2));
    }

    @GetMapping("/{id}")
    @ResponseBody
    Mono<News> getNewsById(@PathVariable String id) {
        return Mono.just(news1);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    Mono<Void> deleteNewsById(@PathVariable String id) {
        return Mono.empty();
    }
}