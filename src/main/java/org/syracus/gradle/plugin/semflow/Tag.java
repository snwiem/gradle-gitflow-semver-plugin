package org.syracus.gradle.plugin.semflow;

/**
 * Created by snwiem on 4/28/2017.
 */
class Tag implements Comparable<Tag> {

    private final String  name;

    static final Tag NULL = new NullTag();

    Tag(String name) {
        if (null == name || name.isEmpty())
            throw new IllegalArgumentException("Tag name can not be NULL or empty");
        this.name = name;
    }

    Tag() {
        this.name = null;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return name != null ? name.equals(tag.name) : tag.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Tag other) {
        if (other.equals(Tag.NULL)) {
            return 1;
        }
        return name.compareTo(other.name);
    }

    public static Tag withName(String name) {
        return new Tag(name);
    }

    static class NullTag extends Tag {
        public NullTag() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object other) {
            return other instanceof NullTag;
        }

        @Override
        public int compareTo(Tag other) {
            if (equals(other))
                return 0;
            return -1;
        }

        @Override
        public String getName() {
            throw new IllegalStateException("NullTag does not support tag names");
        }
    }
}
