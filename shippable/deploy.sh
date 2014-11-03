#!/bin/sh

  virtualenv ve && source ve/bin/activate && pip install awscli

  aws s3 cp ./server/player/player-registration/target/player-registration-0.12.2-SNAPSHOT.war s3://clemble/production/api/player/registration.war
  aws s3 cp ./server/player/player-social/target/player-social-0.12.2-SNAPSHOT.war s3://clemble/production/api/player/social.war
  aws s3 cp ./server/player/player-presence/target/player-presence-0.12.2-SNAPSHOT.war s3://clemble/production/api/player/presence.war
  aws s3 cp ./server/player/player-profile/target/player-profile-0.12.2-SNAPSHOT.war s3://clemble/production/api/player/profile.war
  aws s3 cp ./server/player/player-connection/target/player-connection-0.12.2-SNAPSHOT.war s3://clemble/production/api/player/connection.war

  aws s3 cp ./server/payment/payment-transaction/target/payment-transaction-0.12.2-SNAPSHOT.war s3://clemble/production/api/payment/payment.war
  aws s3 cp ./server/payment/payment-bonus/target/payment-bonus-0.12.2-SNAPSHOT.war s3://clemble/production/api/payment/bonus.war

  aws s3 cp ./server/goal/goal-configuration/target/goal-configuration-0.12.2-SNAPSHOT.war s3://clemble/production/api/goal/configuration.war
  aws s3 cp ./server/goal/goal-construction/target/goal-construction-0.12.2-SNAPSHOT.war s3://clemble/production/api/goal/construction.war
  aws s3 cp ./server/goal/goal-management/target/goal-management-0.12.2-SNAPSHOT.war s3://clemble/production/api/goal/management.war

  # Deploying apps
  # bonus
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id 30a2ab1e-087b-4d4f-98bf-c77bec3a151a --command '{"Name":"deploy"}'
  # payment
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id eb6333c9-c5fb-4c97-aa66-b7df81ff130f --command '{"Name":"deploy"}'

  # player connection
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id 35996c99-0d1a-4ff7-984f-6ce879a3a4ef --command '{"Name":"deploy"}'
  # player presence
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id 83408159-e492-4b26-a20d-522428322974 --command '{"Name":"deploy"}'
  # player profile
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id f5727102-e7e0-463c-b43f-2a1fce249410 --command '{"Name":"deploy"}'
  # player registration
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id 6277fac9-fd01-4102-b604-e0fa810a6819 --command '{"Name":"deploy"}'
  # player social
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id 7ff88b06-2c03-4793-abc1-57b7ac8ed087 --command '{"Name":"deploy"}'

  # goal goal configuration
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id 74fcbb1d-5a54-4841-a7e8-518cd58b03d0 --command '{"Name":"deploy"}'
  # goal goal construction
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id ce49643b-6ea2-4f74-99c9-ab8d77c6c092 --command '{"Name":"deploy"}'
  # goal goal management
  aws opsworks create-deployment --stack-id $AWS_STACK --app-id 39a8bd2a-cd0a-4775-bb83-1ffb0488fc19 --command '{"Name":"deploy"}'
