# travis CI <-> nginX

This repo uses [elasticsearch](https://www.elastic.co/products/elasticsearch) in integration tests. 
Basic authentication needs to be tested as well, and since elastic search does not offer any security out of the box, we need
to use [nginx](https://nginx.org/en/) as reverse proxy, in order to enforce basic authentication.