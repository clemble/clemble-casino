Scenario: User has incorrect email

Given A
And B
And A has invalid email
When A validated
Then error happened


