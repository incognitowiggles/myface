package acceptancetest.com.colinvipurs.application

import com.colinvipurs.application.domain.Post
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration
import java.time.Instant

class PostWithBodyRenderingSpec extends Specification {
    @Unroll
    def "renders posted at #timeDifference ago as #expectedTime"() {
        given:
            def post = Post.of("ignored", "ignored", Instant.now().minus(timeDifference))
        expect:
            post.formattedTime() == expectedTime
        where:
            timeDifference                       | expectedTime
            Duration.ofSeconds(0)                | "Just now"
            Duration.ofSeconds(1)                | "1 second ago"
            Duration.ofSeconds(59)               | "59 seconds ago"
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
