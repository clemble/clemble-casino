package org.eluder.spring.social.mongodb;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.social.connect.*;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.transform;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class MongoUsersConnectionRepository implements UsersConnectionRepository {

    private final MongoOperations mongo;
    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final MongoConnectionTransformers mongoConnectionTransformers;
    private final ConnectionSignUp connectionSignUp;
    private final SystemNotificationService notificationService;


    public MongoUsersConnectionRepository(
        final ConnectionSignUp connectionSignUp,
        final MongoOperations mongo,
        final ConnectionFactoryLocator connectionFactoryLocator,
        final MongoConnectionTransformers mongoConnectionTransformers,
        final SystemNotificationService notificationService) {
        this.mongo = mongo;
        this.connectionSignUp = connectionSignUp;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.mongoConnectionTransformers = mongoConnectionTransformers;
        this.notificationService = notificationService;
    }

    @Override
    public List<String> findUserIdsWithConnection(final Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        Query query = query(where("providerId").is(key.getProviderId()).and("providerUserId").is(key.getProviderUserId()));
        query.fields().include("userId");
        List<String> localUserIds = ImmutableList.copyOf(transform(mongo.find(query, MongoConnection.class), mongoConnectionTransformers.toUserId()));
        if (localUserIds.isEmpty() && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null) {
                createConnectionRepository(newUserId).addConnection(connection);
                return ImmutableList.of(newUserId);
            }
        }
        return localUserIds;
    }

    @Override
    public Set<String> findUserIdsConnectedTo(final String providerId, final Set<String> providerUserIds) {
        Query query = query(where("providerId").is(providerId).and("providerUserId").in(providerUserIds));
        query.fields().include("userId");
        return ImmutableSet.copyOf(transform(mongo.find(query, MongoConnection.class), mongoConnectionTransformers.toUserId()));
    }

    @Override
    public ConnectionRepository createConnectionRepository(final String userId) {
        checkArgument(userId != null, "userId must be defined");
        return new MongoConnectionRepository(userId, mongo, connectionFactoryLocator, mongoConnectionTransformers, notificationService);
    }
}
