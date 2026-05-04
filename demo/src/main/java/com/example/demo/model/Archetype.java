package com.example.demo.model;

public enum Archetype {

    ABOMASNOW_KYOGRE("Mega Abomasnow", "/images/archetypes/ABOMASNOW-KYOGRE.png"),
    ALAKAZAM_DUDUNSPARCE("Alakazam Dudunsparce", "/images/archetypes/ALAKAZAM-DUDUNSPARCE.png"),
    ARBOLIVA_MEGANIUM("Arboliva Meganium", "/images/archetypes/ARBOLIVA-MEGANIUM.png"),
    BABY_BOX("Baby Box", "/images/archetypes/BABY-BOX.png"),
    CRUSTLE_KANGASHAN("Crustle Kangaskhan", "/images/archetypes/CRUSTLE-KANGASKHAN.png"),
    CRUSTLE_MUNKIDORI("Crustle Munkidori", "/images/archetypes/CRUSTLE-MUNKIDORI.png"),
    DRAGAPULT_BLAZIKEN("Dragapult Blaziken", "/images/archetypes/DRAGAPULT-BLAZIKEN.png"),
    DRAGAPULT_DUDUNSPARCE("Dragapult Dudunsparce", "/images/archetypes/DRAGAPULT-DUDUNSPARCE.png"),
    DRAGAPULT_DUSKNOIR("Dragapult Dusknoir", "/images/archetypes/DRAGAPULT-DUSKNOIR.png"),
    DRAGAPULT_STRAIGHT("Dragapult Straight", "/images/archetypes/DRAGAPULT-STRAIGHT.png"),
    FESTIVAL_LEAD("Festival Lead", "/images/archetypes/FESTIVAL-LEAD.png"),
    GARCHOMP("Cynthia's Garchomp", "/images/archetypes/GARCHOMP.png"),
    GRIMMSNARL_FROSLASS("Marnie's Grimmsnarl", "/images/archetypes/GRIMMSNARL-FROSLASS.png"),
    HONCHKROW_PORYGON("Rocket's Honchkrow", "/images/archetypes/HONCHKROW-PORYGON.png"),
    LUCARIO_HARIYAMA("Mega Lucario", "/images/archetypes/LUCARIO-HARIYAMA.png"),
    OGERPON_MEGANIUM("Ogerpon Meganium", "/images/archetypes/OGERPON-MEGANIUM.png"),
    OKIDOGI_BARBARACLE("Okidogi Barbaracle", "/images/archetypes/OKIDOGI-BARBARACLE.png"),
    OTHER("Other", "/images/archetypes/OTHER.png"),
    OUR_BRAINS("Our Brains", "/images/archetypes/OUR-BRAINS.png"),
    RAGING_BOLT_OGERPON("Raging Bolt Ogerpon", "/images/archetypes/RAGING-BOLT-OGERPON.png"),
    ROCKET_BOX("Rocket Box", "/images/archetypes/ROCKET-BOX.png"),
    SLOP_BOX("Slop Box", "/images/archetypes/SLOP-BOX.png"),
    SLOWKING("Slowking", "/images/archetypes/SLOWKING.png"),
    STARMIE_DUSKNOIR("Starmie Dusknoir", "/images/archetypes/STARMIE-DUSKNOIR.png"),
    STARMIE_FROSLASS("Starmie Froslass", "/images/archetypes/STARMIE-FROSLASS.png"),
    ZOROARK("N's Zoroark", "/images/archetypes/ZOROARK.png"),
    ZYGARDE_BARBARACLE("Mega Zygarde", "/images/archetypes/ZYGARDE-BARBARACLE.png");

    private final String displayName;
    private final String iconPath;

    Archetype(String displayName, String iconPath) {
        this.displayName = displayName;
        this.iconPath = iconPath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconPath() {
        return iconPath;
    }
}