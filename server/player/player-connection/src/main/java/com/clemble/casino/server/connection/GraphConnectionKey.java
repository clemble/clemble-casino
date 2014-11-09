package com.clemble.casino.server.connection;

import com.clemble.casino.social.ClembleSocialUtils;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.social.connect.ConnectionKey;

@NodeEntity
public class GraphConnectionKey {

    @GraphId
    private Long id;

    @Indexed(unique = true, indexName = "connectionKeyIndex", indexType= IndexType.FULLTEXT)
    private String connectionKey;

    public GraphConnectionKey() {
    }

    public GraphConnectionKey(String connectionKey) {
        this.connectionKey = connectionKey;
    }

    public GraphConnectionKey(ConnectionKey connectionKey) {
       this.connectionKey = ClembleSocialUtils.toString(connectionKey);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConnectionKey() {
        return connectionKey;
    }

    public void setConnectionKey(String connectionKey) {
        this.connectionKey = connectionKey;
    }

    public ConnectionKey toConnectionKey(){
        return ClembleSocialUtils.fromString(connectionKey);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((connectionKey == null) ? 0 : connectionKey.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GraphConnectionKey other = (GraphConnectionKey) obj;
        if (connectionKey == null) {
            if (other.connectionKey != null)
                return false;
        } else if (!connectionKey.equals(other.connectionKey))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
