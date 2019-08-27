package com.arzio.arziolib.api.util;

public enum CDGunPaint {
    ASMO(1),
    CYREX(2),
    VULCAN(3),
    GEM(4),
    FADE(5),
    RUBY(6),
    UV(7),
    CANDY_APPLE(8),
    DRAGON(9),
    MULTI_COLOR(10),
    FURY(11),
    SLAUGHTER(12),
    DIAMOND(13),
    INFERNO(14),
    SCORCHED(15);

    private int id;

    private CDGunPaint(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public static CDGunPaint getFromId(int id){
        for (CDGunPaint paint : CDGunPaint.values()){
            if (paint.getId() == id){
                return paint;
            }
        }
        return null;
    }

}