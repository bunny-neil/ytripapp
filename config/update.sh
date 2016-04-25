#!/usr/bin/env bash

curl -XPUT --data-binary @application.yml http://localhost:8500/v1/kv/config/application/data