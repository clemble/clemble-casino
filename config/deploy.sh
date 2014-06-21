#!/bin/sh

pip install awscli

aws s3 cp server/player/player-web/target/player-web-0.9.0-SNAPSHOT.war s3://s3-us-west-2.amazonaws.com/clemble/player/player-web-0.9.0-SNAPSHOT.war
