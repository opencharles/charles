#!/bin/bash

set -e
set -x

DIR=$(realpath $(dirname "$0"))

printf "jeff:$(openssl passwd -crypt s3cr3t)n" > $DIR/passwords

# Start nginx.
nginx -c "$DIR/nginx.conf"
