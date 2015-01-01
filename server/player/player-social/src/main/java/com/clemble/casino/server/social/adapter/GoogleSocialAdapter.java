package com.clemble.casino.server.social.adapter;

import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.social.SocialConnectionAdapter;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.model.PeopleFeed;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import com.google.api.services.plusDomains.model.Circle;
import com.google.api.services.plusDomains.model.CircleFeed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mavarazy on 11/30/14.
 */
public class GoogleSocialAdapter extends SocialConnectionAdapter<Google>{

    final private String clientId;
    final private String clientSecret;
    final private GoogleConnectionFactory googleFactory;

    public GoogleSocialAdapter(String clientId, String clientSecret, GoogleConnectionFactory googleFactory) {
        super("google");
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.googleFactory = googleFactory;
    }

    @Override
    public PlayerProfile fetchPlayerProfile(Google api) {
        // Step 1. Fetching google profile
        Person profile = api.plusOperations().getGoogleProfile();
        // Step 2. Converting google profile to Player Profile
        return new PlayerProfile().
            addSocialConnection(new ConnectionKey("google", profile.getId())).
            setBirthDate(profile.getBirthday()).
            setFirstName(profile.getGivenName()).
            setGender(PlayerGender.parse(profile.getGender())).
            setLastName(profile.getFamilyName()).
            setNickName(profile.getDisplayName());
    }

    @Override
    public String getEmail(Google api) {
        return api.plusOperations().getGoogleProfile().getAccountEmail();
    }

    @Override
    public Collection<ConnectionKey> fetchConnections(Google api) {
        // Step 1. Fetching plus domains
        PlusDomains plusDomains = plusDomains(api);
        try {
            // Step 2. Fetching circles
            List<String> circles = fetchCircles(plusDomains);
            // Step 3. Fetching google ids
            List<String> googleIds = fetchConnections(plusDomains, circles);
            // Step 4. Returning connections
            return googleIds.stream().map((googleId) -> new ConnectionKey("google", googleId)).collect(Collectors.toList());
        } catch (Throwable throwable) {

        }
        return Collections.emptyList();
    }

    @Override
    public Pair<String, String> toImageUrl(Connection<Google> connection) {
        // Step 1. Fetching API's
        Google api = connection.getApi();
        // Step 2. Fetching image URL
        Person profile = api.plusOperations().getGoogleProfile();
        String imageUrl = profile.getImageUrl();
        return new ImmutablePair<String, String>(imageUrl, imageUrl);
    }

    @Override
    public ConnectionData toConnectionData(SocialAccessGrant accessGrant) {
        Connection<Google> google = googleFactory.createConnection(accessGrant.toAccessGrant());
        return google.createData();
    }

    @Override
    public ConnectionData toConnectionData(SocialConnectionData connectionData) {
        return new ConnectionData(connectionData.getProviderId(),
            connectionData.getProviderUserId(),
            "",
            "",
            "",
            connectionData.getAccessToken(),
            connectionData.getSecret(),
            connectionData.getRefreshToken(),
            connectionData.getExpireTime());
    }

    public PlusDomains plusDomains(Google google) {
        GoogleCredential credential = new GoogleCredential.Builder()
            .setTransport(new NetHttpTransport())
            .setJsonFactory(new JacksonFactory())
            .setClientSecrets(clientId, clientSecret)
            // You can also add a credential store listener to have credentials
            // stored automatically.
            //.addRefreshListener(new CredentialStoreRefreshListener(userId, credentialStore))
            .build();

         // Set authorized credentials.
        // Though not necessary when first created, you can manually refresh the
        // token, which is needed after 60 minutes.
        credential.setAccessToken(google.getAccessToken());
        // Create a new authorized API client
        return new PlusDomains.Builder(new NetHttpTransport(), new JacksonFactory(), credential).build();
    }

    public List<String> fetchCircles(PlusDomains plusDomains) throws IOException {
        List<String> circleIDs = new ArrayList<>();

        PlusDomains.Circles.List listCircles = plusDomains.circles().list("me");
        listCircles.setMaxResults(5L);
        CircleFeed circleFeed = listCircles.execute();
        List<Circle> circles = circleFeed.getItems();

        // Loop until no additional pages of results are available.
        while (circles != null) {
            for (Circle circle : circles) {
                circleIDs.add(circle.getId());
            }

            // When the next page token is null, there are no additional pages of
            // results. If this is the case, break.
            if (circleFeed.getNextPageToken() != null) {
                // Prepare the next page of results
                listCircles.setPageToken(circleFeed.getNextPageToken());

                // Execute and process the next page request
                circleFeed = listCircles.execute();
                circles = circleFeed.getItems();
            } else {
                circles = null;
            }
        }

        return circleIDs;
    }

    public List<String> fetchConnections(PlusDomains plusDomains, List<String> circles) throws IOException {
        List<String> googleIds = new ArrayList<>();

        for (String circleId : circles) {
            PlusDomains.People.ListByCircle listPeople = plusDomains.people().listByCircle(circleId);
            listPeople.setMaxResults(100L);

            PeopleFeed peopleFeed = listPeople.execute();
            System.out.println("Google+ users circled:");

            // This example only displays one page of results.
            while (peopleFeed.getItems() != null && peopleFeed.getItems().size() > 0 ) {
                for(com.google.api.services.plusDomains.model.Person person : peopleFeed.getItems()) {
                    googleIds.add(person.getId());
                }

                if (peopleFeed.getNextPageToken() != null) {
                    listPeople.setPageToken(peopleFeed.getNextPageToken());

                    // Execute and process the next page request
                    peopleFeed = listPeople.execute();
                }
            }
        }

        return googleIds;
    }

}
