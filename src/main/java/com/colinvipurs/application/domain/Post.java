package com.colinvipurs.application.domain;

import java.time.Instant;

/**
 * An existing post
 */
public class Post {
    private String user;
    private final String body;
    private final Instant time;

    private Post(String user, String body, Instant time) {
        this.user = user;
        this.body = body;
        this.time = time;
    }

    public static Post of(String user, String body, Instant time) {
        return new Post(user, body, time);
    }

    public String body() {
        return body;
    }

    public String user() {
        return user;
    }

    public String formattedTime() {
        long timeDifferenceInSeconds = Instant.now().getEpochSecond() - time.getEpochSecond();
        TimeDifference difference = TimeDifference.fromSeconds(timeDifferenceInSeconds);
        if (timeDifferenceInSeconds < 1) {
            return "Just now";
        } else {
            return String.format("%d %s ago", difference.quantity(), difference.unit());
        }
    }

    private static class TimeDifference {
        private final String unit;
        private final long quantity;

        public TimeDifference(long quantity, String unit) {
            this.quantity = quantity;
            this.unit = quantity > 1 ? unit+"s" : unit;
        }

        public long quantity() {
            return quantity;
        }

        public String unit() {
            return unit;
        }

        public static TimeDifference fromSeconds(long timeDifferenceInSeconds) {
            if (timeDifferenceInSeconds < 60) {
                return new TimeDifference(timeDifferenceInSeconds, "second");
            }
            long timeDifferenceInMinutes = timeDifferenceInSeconds / 60;
            if (timeDifferenceInMinutes < 60) {
                return new TimeDifference(timeDifferenceInMinutes, "minute");
            }
            long timeDifferenceInHours = timeDifferenceInMinutes / 60;
            if (timeDifferenceInHours < 24) {
                return new TimeDifference(timeDifferenceInHours, "hour");
            }
            long timeDifferenceInDays = timeDifferenceInHours / 24;
            if (timeDifferenceInDays < 7) {
                return new TimeDifference(timeDifferenceInDays, "day");
            }
            return new TimeDifference(timeDifferenceInDays / 7, "week");
        }
    }
}
