    DELETE FROM GAME_CONFIGURATION WHERE GAME_NAME = 'num';

    INSERT INTO GAME_CONFIGURATION (GAME_NAME, SPECIFICATION_NAME, CONFIGURATION)
    VALUES ('num', 'low', '{"type":"match","configurationKey":{"game":"num","specificationName":"low"},"price":{"currency":"FakeMoney","amount":50},"betRule":{"betType":"unlimited"},"giveUpRule":{"giveUp":"all"},"moveTimeRule":{"rule":"moveTime","limit":2000,"punishment":"loose"},"totalTimeRule":{"rule":"totalTime","limit":4000,"punishment":"loose"},"privacyRule":["privacy","everybody"],"numberRule":["participants","two"],"visibilityRule":"visible","drawRule":["DrawRule","owned"],"wonRule":["WonRule","price"],"roles":["A","B"]}');

    INSERT INTO GAME_CONFIGURATION (GAME_NAME, SPECIFICATION_NAME, CONFIGURATION)
    VALUES ('pot', 'pot', '{"type":"pot","configurationKey":{"game":"pot","specificationName":"pot"},"price":{"currency":"FakeMoney","amount":200},"privacyRule":["privacy","everybody"],"numberRule":["participants","two"],"potFillRule":"maxcommon","moveTimeRule":{"rule":"moveTime","limit":50000,"punishment":"loose"},"totalTimeRule":{"rule":"totalTime","limit":500000,"punishment":"loose"},"configurations":[{"type":"match","configurationKey":{"game":"num","specificationName":"low"},"price":{"currency":"FakeMoney","amount":50},"betRule":{"betType":"unlimited"},"giveUpRule":{"giveUp":"all"},"moveTimeRule":{"rule":"moveTime","limit":2000,"punishment":"loose"},"totalTimeRule":{"rule":"totalTime","limit":4000,"punishment":"loose"},"privacyRule":["privacy","everybody"],"numberRule":["participants","two"],"visibilityRule":"visible","drawRule":["DrawRule","owned"],"wonRule":["WonRule","price"],"roles":["A","B"]},{"type":"match","configurationKey":{"game":"num","specificationName":"low"},"price":{"currency":"FakeMoney","amount":50},"betRule":{"betType":"unlimited"},"giveUpRule":{"giveUp":"all"},"moveTimeRule":{"rule":"moveTime","limit":2000,"punishment":"loose"},"totalTimeRule":{"rule":"totalTime","limit":4000,"punishment":"loose"},"privacyRule":["privacy","everybody"],"numberRule":["participants","two"],"visibilityRule":"visible","drawRule":["DrawRule","owned"],"wonRule":["WonRule","price"],"roles":["A","B"]},{"type":"match","configurationKey":{"game":"num","specificationName":"low"},"price":{"currency":"FakeMoney","amount":50},"betRule":{"betType":"unlimited"},"giveUpRule":{"giveUp":"all"},"moveTimeRule":{"rule":"moveTime","limit":2000,"punishment":"loose"},"totalTimeRule":{"rule":"totalTime","limit":4000,"punishment":"loose"},"privacyRule":["privacy","everybody"],"numberRule":["participants","two"],"visibilityRule":"visible","drawRule":["DrawRule","owned"],"wonRule":["WonRule","price"],"roles":["A","B"]}]}');
