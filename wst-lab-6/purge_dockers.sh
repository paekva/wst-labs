#!/bin/bash
docker ps -a | grep wst | awk '{print $1}' | xargs docker rm
docker image list | grep wst | grep postgre | awk '{print $3}' | xargs docker image rm
docker image list | grep wst | grep glass | awk '{print $3}' | xargs docker image rm
