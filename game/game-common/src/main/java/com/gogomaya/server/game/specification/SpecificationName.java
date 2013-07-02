package com.gogomaya.server.game.specification;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.cloudfoundry.org.codehaus.jackson.annotate.JsonCreator;
import org.cloudfoundry.org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.game.GameAware;

@Embeddable
public class SpecificationName implements GameAware {

    /**
     * Generated 13/04/13
     */
    private static final long serialVersionUID = -4223872939422173928L;

    final public static SpecificationName DEFAULT = new SpecificationName("", "");

    @Column(name = "GAME_NAME")
    private String name = "";

    @Column(name = "SPECIFICATION_NAME")
    private String specificationName = "";

    public SpecificationName() {
    }

    @JsonCreator
    public SpecificationName(@JsonProperty("name") String name, @JsonProperty("group") String group) {
        this.name = name;
        this.specificationName = group;
    }

    public String getName() {
        return name;
    }

    public SpecificationName setName(String name) {
        this.name = name;
        return this;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public SpecificationName setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
        return this;
    }

    public byte[] toByteArray() {
        byte[] nameBytes = name.getBytes();
        byte[] groupBytes = specificationName.getBytes();
        byte[] results = Arrays.copyOf(nameBytes, nameBytes.length + groupBytes.length);

        for (int i = nameBytes.length, j = 0; j < groupBytes.length; i++, j++)
            results[i] = groupBytes[j];

        return results;
    }

    @Override
    public String toString() {
        return "[" + name + ", " + specificationName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((specificationName == null) ? 0 : specificationName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        SpecificationName other = (SpecificationName) obj;
        if (specificationName == null) {
            if (other.specificationName != null)
                return false;
        } else if (!specificationName.equals(other.specificationName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
