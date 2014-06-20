#!/bin/sh
mkdir -p ./shippable/testresults
cp ./public/api/api-common/target/surefire-reports/* ./shippable/testresults/
cp ./public/api/api-test/target/surefire-reports/* ./shippable/testresults/
cp ./public/client/client-common/target/surefire-reports/* ./shippable/testresults/
cp ./public/client/client-android/target/surefire-reports/* ./shippable/testresults/

cp ./server/common/target/surefire-reports/* ./shippable/testresults/
cp ./server/common-web/target/surefire-reports/* ./shippable/testresults/

cp ./server/game/game-common/target/surefire-reports/* ./shippable/testresults/
cp ./server/game/game-management/target/surefire-reports/* ./shippable/testresults/
cp ./server/game/game-web/target/surefire-reports/* ./shippable/testresults/

cp ./server/payment/payment-common/target/surefire-reports/* ./shippable/testresults/
cp ./server/payment/payment-management/target/surefire-reports/* ./shippable/testresults/
cp ./server/payment/payment-web/target/surefire-reports/* ./shippable/testresults/

cp ./server/player/player-common/target/surefire-reports/* ./shippable/testresults/
cp ./server/player/player-management/target/surefire-reports/* ./shippable/testresults/
cp ./server/player/player-social/target/surefire-reports/* ./shippable/testresults/
cp ./server/player/player-web/target/surefire-reports/* ./shippable/testresults/

cp ./integration/integration-base/target/surefire-reports/* ./shippable/testresults/
cp ./integration/integration-game/target/surefire-reports/* ./shippable/testresults/
cp ./integration/integration-test/target/surefire-reports/* ./shippable/testresults/
