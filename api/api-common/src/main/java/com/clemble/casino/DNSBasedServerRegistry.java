package com.clemble.casino;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("dns")
public class DNSBasedServerRegistry implements ServerRegistry {

    /**
     * 
     */
    private static final long serialVersionUID = 4733522210345964111L;

    final private int prefixLength;
    final private String baseUrl;
    final private String idUrl;
    final private String idAndTypeUrl;

    @JsonCreator
    public DNSBasedServerRegistry(@JsonProperty("prefixLength") int prefixLength, @JsonProperty("baseUrl") String baseUrl, @JsonProperty("idUrl") String idUrl, @JsonProperty("idAndTypeUrl") String idAndTypeUrl) {
        this.prefixLength = prefixLength;
        this.baseUrl = baseUrl;
        this.idUrl = idUrl;
        this.idAndTypeUrl = idAndTypeUrl;
    }

    @Override
    public String findBase() {
        return baseUrl;
    }

    @Override
    public String findById(String identifier) {
        return String.format(idUrl, identifier.substring(0, prefixLength));
    }

    @Override
    public String findByIdAndType(String identifier, Object type) {
        return String.format(idAndTypeUrl, identifier.substring(0, prefixLength), String.valueOf(type));
    }

    public int getPrefixLength() {
        return prefixLength;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getIdUrl() {
        return idUrl;
    }

    public String getIdAndTypeUrl() {
        return idAndTypeUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((baseUrl == null) ? 0 : baseUrl.hashCode());
        result = prime * result + ((idAndTypeUrl == null) ? 0 : idAndTypeUrl.hashCode());
        result = prime * result + ((idUrl == null) ? 0 : idUrl.hashCode());
        result = prime * result + prefixLength;
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
        DNSBasedServerRegistry other = (DNSBasedServerRegistry) obj;
        if (baseUrl == null) {
            if (other.baseUrl != null)
                return false;
        } else if (!baseUrl.equals(other.baseUrl))
            return false;
        if (idAndTypeUrl == null) {
            if (other.idAndTypeUrl != null)
                return false;
        } else if (!idAndTypeUrl.equals(other.idAndTypeUrl))
            return false;
        if (idUrl == null) {
            if (other.idUrl != null)
                return false;
        } else if (!idUrl.equals(other.idUrl))
            return false;
        if (prefixLength != other.prefixLength)
            return false;
        return true;
    }

}
