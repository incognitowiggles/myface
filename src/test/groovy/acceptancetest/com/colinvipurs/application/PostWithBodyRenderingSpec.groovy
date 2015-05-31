package acceptancetest.com.colinvipurs.application

import com.colinvipurs.application.PostWithBody
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalUnit

class PostWithBodyRenderingSpec extends Specification {
    @Unroll
    def "renders #body as as the body"() {
        given:
            def post = PostWithBody.of(body, Instant.now())
        expect:
            post.describe() startsWith "$body ("
        where:
            body << ["I am a post", "I am another post"]
    }

    @Unroll
    def "renders posted at #timeDifference ago as #description"() {
        given:
            def post = PostWithBody.of("ignored", Instant.now().minus(timeDifference))
        expect:
            post.describe() == "ignored ($description)"
        where:
            timeDifference                       | description
            Duration.ofSeconds(0)                | "Just now"
            Duration.ofSeconds(1)                | "Just now"
            Duration.ofSeconds(59)               | "Just now"
            Duration.ofSeconds(60)               | "1 minute ago"
            Duration.ofSeconds(119)              | "1 minute ago"
            Duration.ofSeconds(121)              | "2 minutes ago"
            Duration.ofSeconds(181)              | "3 minutes ago"
            Duration.ofSeconds(59 * 60 + 59)     | "59 minutes ago"
            Duration.ofHours(1)                  | "1 hour ago"
            Duration.ofHours(23).plusMinutes(59) | "23 hours ago"
            Duration.ofHours(24)                 | "1 day ago"
            Duration.ofDays(2)                   | "2 days ago"
            Duration.ofDays(7)                   | "1 week ago"
            Duration.ofDays(14)                  | "2 weeks ago"
    }
}
