package org.syracus.gradle.plugin.semflow;


/**
 * Created by snwiem on 4/28/2017.
 */
public class Branch {

    private final String name;
    private final boolean clean;

    public Branch(String name) {
        this(name, true);
    }

    public Branch(String name, boolean clean) {
        if (null == name || name.isEmpty())
            throw new IllegalArgumentException("Branch name can not be empty or null");
        this.name = name;
        this.clean = clean;
    }

    public String getFullName() {
        return name;
    }

    public String getPrefix() {
        String[] tokens = name.split("/", 2);
        return 2 == tokens.length ? tokens[0]+"/" : "";
    }

    public String getName() {
        String[] tokens = name.split("/", 2);
        return 2 == tokens.length ? tokens[1] : tokens[0];
    }

    public boolean isClean() {
        return clean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;

        if (clean != branch.clean) return false;
        return name.equals(branch.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (clean ? 1 : 0);
        return result;
    }
}
