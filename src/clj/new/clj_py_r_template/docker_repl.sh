#!/usr/bin/env bash
set -euo pipefail

docker run -it --rm -v -v "$(pwd):/workdir" -p  12345:12345 -p 7777:7777 -w /workdir appcompany.funapp
