#!/bin/sh
mkdir -p ./shippable/testresults
cp -R ./public/api/api-common/target/surefire-reports/* ./shippable/testresults/
cp -R ./public/api/api-test/target/surefire-reports/* ./shippable/testresults/

#cp -R ./public/client/client-common/target/surefire-reports/* ./shippable/testresults/
cp -R ./public/client/client-android/target/surefire-reports/* ./shippable/testresults/

cp -R ./server/common/target/surefire-reports/* ./shippable/testresults/
cp -R ./server/common-web/target/surefire-reports/* ./shippable/testresults/

#cp -R ./server/game/game-common/target/surefire-reports/* ./shippable/testresults/
cp -R ./server/game/game-management/target/surefire-reports/* ./shippable/testresults/
cp -R ./server/game/game-web/target/surefire-reports/* ./shippable/testresults/

#cp -R ./server/payment/payment-common/target/surefire-reports/* ./shippable/testresults/
cp -R ./server/payment/payment-management/target/surefire-reports/* ./shippable/testresults/
cp -R ./server/payment/payment-web/target/surefire-reports/* ./shippable/testresults/

cp -R ./server/player/player-common/target/surefire-reports/* ./shippable/testresults/
cp -R ./server/player/player-management/target/surefire-reports/* ./shippable/testresults/
cp -R ./server/player/player-social/target/surefire-reports/* ./shippable/testresults/
cp -R ./server/player/player-web/target/surefire-reports/* ./shippable/testresults/

#cp -R ./integration/integration-base/target/surefire-reports/* ./shippable/testresults/
#cp -R ./integration/integration-game/target/surefire-reports/* ./shippable/testresults/
cp -R ./integration/integration-test/target/surefire-reports/* ./shippable/testresults/
