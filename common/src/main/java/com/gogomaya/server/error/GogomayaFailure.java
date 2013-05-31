package com.gogomaya.server.error;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;

public class GogomayaFailure {

    final private Set<GogomayaError> errors;

    final public static GogomayaFailure ServerError = new GogomayaFailure(Collections.singleton(GogomayaError.ServerError));

    public GogomayaFailure(GogomayaError gogomayaError) {
        errors = ImmutableSet.<GogomayaError> of(gogomayaError);
    }

    public GogomayaFailure(Iterable<GogomayaError> gogomayaErrors) {
        errors = ImmutableSet.<GogomayaError> copyOf(gogomayaErrors);
    }

    @JsonCreator
    public GogomayaFailure(@JsonProperty("errors") Collection<GogomayaError> errors) {
        this.errors = ImmutableSet.<GogomayaError> copyOf(errors);
    }

    public Set<GogomayaError> getErrors() {
        return errors;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((errors == null) ? 0 : errors.hashCode());
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
        GogomayaFailure other = (GogomayaFailure) obj;
        if (errors == null) {
            if (other.errors != null)
                return false;
        } else if (!errors.equals(other.errors))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GogomayaFailure [errors=" + errors + "]";
    }

}
