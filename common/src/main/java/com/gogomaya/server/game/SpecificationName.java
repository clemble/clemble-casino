package com.gogomaya.server.game;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SpecificationName implements Serializable {

    /**
     * Generated 13/04/13
     */
    private static final long serialVersionUID = -4223872939422173928L;

    final public static SpecificationName DEFAULT = new SpecificationName("", "");

    @Column(name = "SPECIFICATION_NAME")
    private String name = "";

    @Column(name = "SPECIFICATION_GROUP")
    private String group = "";

    public SpecificationName() {
    }

    public SpecificationName(String name, String group) {
        this.setName(name);
        this.setGroup(group);
    }

    public String getName() {
        return name;
    }

    public SpecificationName setName(String name) {
        this.name = name;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public SpecificationName setGroup(String group) {
        this.group = group;
        return this;
    }

    public byte[] toByteArray() {
        byte[] nameBytes = name.getBytes();
        byte[] groupBytes = group.getBytes();
        byte[] results = Arrays.copyOf(nameBytes, nameBytes.length + groupBytes.length);

        for (int i = nameBytes.length, j = 0; j < groupBytes.length; i++, j++)
            results[i] = groupBytes[j];

        return results;
    }

    @Override
    public String toString() {
        return "[" + name + ", " + group + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((group == null) ? 0 : group.hashCode());
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
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
