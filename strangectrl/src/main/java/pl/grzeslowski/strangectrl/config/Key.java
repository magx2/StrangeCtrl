package pl.grzeslowski.strangectrl.config;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

public class Key {
    private String key;

    private Key() {
    }

    public Key(final String key) {
        this.key = checkNotNull(key);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (key == null ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Key && equals((Key) obj);
    }

    private boolean equals(final Key obj) {
        if (obj == null) {
            return false;
        }

        return Objects.equal(key, obj.key);
    }

    @Override
    public String toString() {
        return "Key[" + key + "]";
    }

    public String getKey() {
        return key;
    }
}
