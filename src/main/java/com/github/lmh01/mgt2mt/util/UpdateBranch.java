package com.github.lmh01.mgt2mt.util;

public enum UpdateBranch {
    RELEASE("Release"), ALPHA("Alpha");

    String name;

    UpdateBranch(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Transforms the name to an update branch.
     * If the name is not found {@link UpdateBranch#RELEASE} is returned.
     * @return The update branch for the name
     */
    public static UpdateBranch getUpdateBranch(String name) {
        if (name.equals(UpdateBranch.ALPHA.getName())) {
            return UpdateBranch.ALPHA;
        } else {
            return UpdateBranch.RELEASE;
        }
    }
}
