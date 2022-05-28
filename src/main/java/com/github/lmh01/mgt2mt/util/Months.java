package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.content.managed.types.TypeEnum;

public enum Months implements TypeEnum {
    JAN("january", "JAN", 0),
    FEB("february", "FEB", 1),
    MAR("march", "MAR", 2),
    APR("april", "APR", 3),
    MAY("may", "MAY", 4),
    JUN("june", "JUN", 5),
    JUL("july", "JUL", 6),
    AUG("august", "AUG", 7),
    SEP("september", "SEP", 8),
    OCT("october", "OCT", 9),
    NOV("november", "NOV", 10),
    DEC("december", "DEC", 11);

    private final String name;
    private final String data;
    private final int id;

    Months(String translationKey, String data, int id) {
        this.name = I18n.INSTANCE.get("month." + translationKey);
        this.data = data;
        this.id = id;
    }

    /**
     * @return The data type that is written in the game files
     */
    public String getDataName() {
        return data;
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
     * @param name The name for which the id should be returned
     * @return The id of the name
     */
    public static int getIdByName(String name) {
        for (Months month : Months.values()) {
            if (name.equals(month.getTypeName()) || name.equals(month.getDataName())) {
                return month.getId();
            }
        }
        throw new IllegalArgumentException("Name is invalid, month id not found for input string: " + name);
    }

    /**
     * @param name The name for which the data name should be returned
     * @return The name of the month
     */
    public static String getDataNameByTypeName(String name) {
        for (Months month : Months.values()) {
            if (month.getTypeName().equals(name)) {
                return month.getDataName();
            }
        }
        throw new IllegalArgumentException("Name is invalid, month data name not found for input string: " + name);
    }
}
