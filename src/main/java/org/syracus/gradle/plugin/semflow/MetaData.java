package org.syracus.gradle.plugin.semflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by snwiem on 4/27/2017.
 */
class MetaData implements Comparable<MetaData> {

    private static final Pattern PATTERN = Pattern.compile("^[0-9a-zA-Z-]+$");
    private static final Pattern PATTERN2 = Pattern.compile("^(0*)\\d+$");
    private static String IDENT_SEPARATOR = ".";

    static final MetaData NULL = new NullMetaData();

    private final String[] identifiers;

    MetaData(String[] identifiers) {
        this.identifiers = identifiers;
    }



    @Override
    public int compareTo(final MetaData other) {
        if (other == MetaData.NULL) {
            /**
             * Pre-release versions have a lower precedence than
             * the associated normal version. (SemVer p.9)
             */
            return -1;
        }
        int result = compareIdentifierArrays(other.identifiers);
        if (result == 0) {
            /**
             * A larger set of pre-release fields has a higher
             * precedence than a smaller set, if all of the
             * preceding identifiers are equal. (SemVer p.11)
             */
            result = identifiers.length - other.identifiers.length;
        }
        return result;
    }

    MetaData increment(){
        if (null == identifiers || 0 == identifiers.length)
            return new MetaData(null);
        // semver2 does not explicitely state how to increment Pre-Release versions.
        // We just look for the last digit and increment this.
        for (int i = identifiers.length-1; i >= 0; --i) {
            try {
                int value = Integer.parseInt(identifiers[i]);
                String[] copy = Arrays.copyOf(identifiers, identifiers.length);
                copy[i] = String.valueOf(++value);
                return new MetaData(copy);
            } catch(NumberFormatException e) {
                // unhandled
            }
        }
        // no number found to increment. add one
        String[] copy = Arrays.copyOf(identifiers, identifiers.length+1);
        copy[copy.length-1] = String.valueOf(1);
        return new MetaData(copy);
    }

    /**
     * Compares two arrays of identifiers.
     *
     * @param otherIdents the identifiers of the other version
     * @return integer result of comparison compatible with
     *         the {@code Comparable.compareTo} method
     */
    private int compareIdentifierArrays(final String[] otherIdents) {
        int result = 0;
        int length = getLeastCommonArrayLength(identifiers, otherIdents);
        for (int i = 0; i < length; i++) {
            result = compareIdentifiers(identifiers[i], otherIdents[i]);
            if (result != 0) {
                break;
            }
        }
        return result;
    }

    /**
     * Returns the size of the smallest array.
     *
     * @param arr1 the first array
     * @param arr2 the second array
     * @return the size of the smallest array
     */
    private static int getLeastCommonArrayLength(final String[] arr1, final String[] arr2) {
        return arr1.length <= arr2.length ? arr1.length : arr2.length;
    }

    /**
     * Compares two identifiers.
     *
     * @param ident1 the first identifier
     * @param ident2 the second identifier
     * @return integer result of comparison compatible with
     *         the {@code Comparable.compareTo} method
     */
    private int compareIdentifiers(String ident1, String ident2) {
        if (isInt(ident1) && isInt(ident2)) {
            return Integer.parseInt(ident1) - Integer.parseInt(ident2);
        } else {
            return ident1.compareTo(ident2);
        }
    }

    /**
     * Checks if the specified string is an integer.
     *
     * @param str the string to check
     * @return {@code true} if the specified string is an integer
     *         or {@code false} otherwise
     */
    private static boolean isInt(final String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetaData that = (MetaData) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(identifiers);
    }

    @Override
    public String toString() {
        if (null == identifiers || 0 == identifiers.length)
            return "";
        StringBuilder sb = new StringBuilder();
        for (String ident : identifiers) {
            sb.append(ident).append(IDENT_SEPARATOR);
        }
        return sb.deleteCharAt(sb.lastIndexOf(IDENT_SEPARATOR)).toString();
    }

    static MetaData fromString(String metaVersionStr) {
        if (null == metaVersionStr || metaVersionStr.isEmpty())
            return MetaData.NULL;
        StringTokenizer st = new StringTokenizer(metaVersionStr, IDENT_SEPARATOR, false);
        Matcher m = null;
        ArrayList<String> identifiers = new ArrayList<>();
        while(st.hasMoreTokens()) {
            String identifier = st.nextToken();
            m = PATTERN.matcher(identifier);
            if (false == m.matches())
                throw new IllegalArgumentException(String.format("Invalid pre-release version format at '%s'.", identifier));
            m = PATTERN2.matcher(identifier);
            if (true == m.matches()) {
                String prefix = m.group(1);
                if (0 < prefix.length())
                    throw new IllegalArgumentException(String.format("Invalid pre-release version. Numberic identifiers MUST NOT start with 0"));
            }
            identifiers.add(identifier);
        }
        return new MetaData(identifiers.toArray(new String[identifiers.size()]));
    }

    private static class NullMetaData extends MetaData {

        public NullMetaData() {
            super(null);
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
            return other instanceof NullMetaData;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(MetaData other) {
            if (!equals(other)) {
                /**
                 * Pre-release versions have a lower precedence than
                 * the associated normal version. (SemVer p.9)
                 */
                return 1;
            }
            return 0;
        }

        @Override
        MetaData increment() {
            throw new NullPointerException("MetaData is NULL");
        }
    }
}
