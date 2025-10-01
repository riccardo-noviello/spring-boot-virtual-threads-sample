# A simple Spring Boot app to test latest JDK features
Requirements: JDK 21+ and SpringBoot 3.2 +

`docker-compose up`

`mvn clean install`

`java -verbose:gc -jar target/refresher-1.0.0.jar` 

## Results
I have experimented with virtualthreads on a typical Rest Api.
In Spring boot 3.2+ you simply enable this behaviour with 

`spring.threads.virtual.enabled=true`

This setting works both for the embedded Tomcat and Jetty.

The Postgres DB backed app showed that in my case throughput almost doubled.

A simple K6 tests told me that:

With Virtual Threads Enabled:
✅ Higher Throughput:
- Iterations per second nearly doubled from ~6.85/s → ~12.53/s.
- Completed 1504 iterations vs 824, in the same test duration (2 minutes).
✅ Faster Response Times:
- HTTP request duration reduced from avg 4.57s → 2.02s.
- The p95 latency improved from 9.42s → 3.74s, a major drop.

 ### Conclusions: 
Enabling virtual threads is worthwhile having said that it is necessary to:
- check for any blocking operations which can still be a bottleneck
- check for third-party libraries compatibility (is ThreadLocal used?)
