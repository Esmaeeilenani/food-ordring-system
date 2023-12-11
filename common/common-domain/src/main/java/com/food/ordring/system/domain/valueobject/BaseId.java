package com.food.ordring.system.domain.valueobject;

import lombok.Getter;

import java.util.Objects;

@Getter
abstract public class BaseId<T> {
    private final T value;


    protected BaseId(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return value.equals(baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    abstract public String toString();
}
