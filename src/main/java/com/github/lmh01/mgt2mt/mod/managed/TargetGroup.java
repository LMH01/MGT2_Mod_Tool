package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Symbolizes the target group for something
 */
public enum TargetGroup implements TypeEnum {
    KID(0, "kid"),
    TEEN(1, "teen"),
    ADULT(2, "adult"),
    OLD(3, "senior"),
    ALL(4, "all");

    private final int id;
    private final String name;

    TargetGroup(int id, String translationKey) {
        this.id = id;
        this.name = I18n.INSTANCE.get("commonText." + translationKey);
    }

    @Override
    public String getTypeName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    /**
     * Returns the corresponding target group for the name
     */
    public static TargetGroup getTargetGroup(String name) throws IllegalArgumentException {
        for (TargetGroup tg : TargetGroup.values()) {
            if (Objects.equals(tg.name, name)) {
                return tg;
            }
        }
        throw new IllegalArgumentException("Unable to resolve target group: Input string is invalid. Was: " + name);
    }
}
