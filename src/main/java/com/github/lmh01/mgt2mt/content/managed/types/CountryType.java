package com.github.lmh01.mgt2mt.content.managed.types;

import com.github.lmh01.mgt2mt.util.I18n;

public enum CountryType implements TypeEnum {

    USA("usa", 0),
    JAPAN("japan", 1),
    UNITED_KINGDOM("uk", 2),
    CHINA("china", 3),
    SOUTH_KOREA("south_korea", 4),
    GERMANY("germany", 5),
    FRANCE("france", 6),
    CANADA("canada", 7),
    RUSSIA("russia", 8),
    SPAIN("spain", 9),
    ITALY("italy", 10),
    POLAND("poland", 11),
    SWEDEN("sweden", 12),
    AUSTRALIA("australia", 13),
    AUSTRIA("austria", 14),
    FINLAND("finland", 15),
    SWITZERLAND("switzerland", 16),
    TURKEY("turkey", 17),
    NETHERLANDS("netherlands", 18),
    UKRAINE("ukraine", 19),
    CZECH_REPUBLIC("czech_republic", 20),
    SLOVAKIA("slovakia", 21),
    MEXICO("mexico", 22),
    HUNGARY("hungary", 23),
    BRAZIL("brazil", 24),
    PORTUGAL("portugal", 25),
    ARGENTINA("argentina", 26),
    ROMANIA("romania", 27),
    THAILAND("thailand", 28),
    PHILIPPINES("philippines", 29),
    BULGARIA("bulgaria", 30),
    DENMARK("denmark", 31),
    EGYPT("egypt", 32),
    GREECE("greece", 33),
    INDIA("india", 34),
    ISRAEL("israel", 35),
    NEW_ZEALAND("new_zealand", 36),
    NORWAY("norway", 37),
    PAKISTAN("pakistan", 38),
    GEORGIA("georgia", 39),
    IRELAND("ireland", 40),
    SOUTH_AFRICA("south_africa", 41),
    IRAN("iran", 42),
    MALAYSIA("malaysia", 43),
    ICELAND("iceland", 44),
    INDONESIA("indonesia", 45),
    COLOMBIA("colombia", 46),
    BELGIUM("belgium", 47),
    LUXEMBOURG("luxembourg", 48),
    BANGLADESH("bangladesh", 49),
    VIETNAM("vietnam", 50),
    PERU("peru", 51),
    VENEZUELA("venezuela", 52),
    CHILE("chile", 53),
    ETHIOPIA("ethiopia", 54),
    NIGERIA("nigeria", 55),
    KENYA("kenya", 56),
    ESTONIA("estonia", 57),
    LATVIA("latvia", 58),
    LITHUANIA("lithuania", 59),
    ARMENIA("armenia", 60),
    AZERBAIJAN("azerbaijan", 61),
    SLOVENIA("slovenia", 62),
    CROATIA("croatia", 63);

    private final String name;
    private final int id;

    CountryType(String translationKey, int id) {
        this.name = I18n.INSTANCE.get("commonText.country." + translationKey);
        this.id = id;
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
     * Returns the country that corresponds to the name.
     *
     * @param name The translated name.
     */
    public static CountryType getFromName(String name) {
        for (CountryType c : CountryType.values()) {
            if (c.getTypeName().equals(name)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unable to return country type: Name is invalid");
    }

    /**
     * Returns the country that corresponds to the id
     */
    public static CountryType getFromId(int id) {
        for (CountryType c : CountryType.values()) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unable to return country type: Id is invalid");
    }
}
