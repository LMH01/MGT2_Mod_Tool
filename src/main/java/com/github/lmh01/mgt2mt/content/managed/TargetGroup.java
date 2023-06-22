package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.content.managed.types.TypeEnum;
import com.github.lmh01.mgt2mt.util.I18n;

import java.util.Objects;

/**
 * Symbolizes the target group for something
 */
public enum TargetGroup implements TypeEnum {
    KID(0, "kid", "KID"),
    TEEN(1, "teen", "TEEN"),
    ADULT(2, "adult", "ADULT"),
    OLD(3, "senior", "OLD"),
    ALL(4, "all", "ALL");

    private final int id;
    private final String name;
    private final String dataType;

    TargetGroup(int id, String translationKey, String dataType) {
        this.id = id;
        this.name = I18n.INSTANCE.get("commonText." + translationKey);
        this.dataType = dataType;
    }

    @Override
    public String getTypeName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDataType() {
        return dataType;
    }

    /**
     * Returns the corresponding target group for the name
     */
    public static TargetGroup getTargetGroup(String name) throws IllegalArgumentException {
        for (TargetGroup tg : TargetGroup.values()) {
            if (Objects.equals(tg.name, name)) {
                return tg;
            }
            if (Objects.equals(tg.dataType, name)) {
                return tg;
            }
        }
        if (Objects.equals(name, "SENIOR")) {
            return OLD;
        }
        throw new IllegalArgumentException("Unable to resolve target group: Input string is invalid. Was: " + name);
    }

    /**
     * Returns the corresponding target group for the id
     */
    public static TargetGroup getTargetGroupById(int id) throws IllegalArgumentException {
        for (TargetGroup tg : TargetGroup.values()) {
            if (tg.id == id) {
                return tg;
            }
        }
        throw new IllegalArgumentException("Unable to resolve target group: Input id is invalid. Was: " + id);
    }
}
