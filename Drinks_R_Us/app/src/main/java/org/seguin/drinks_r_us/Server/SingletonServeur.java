package org.seguin.drinks_r_us.Server;

import org.seguin.drinks_r_us.Models.RetroFitUtils;

/**
 * Created by Gabriel on 15/09/2017.
 */

public final class SingletonServeur {
    private static final SingletonServeur INSTANCE = new SingletonServeur();

    public ServiceServeur serveur;
    private SingletonServeur() {
        serveur = RetroFitUtils.get();
    }

    public static SingletonServeur getInstance() {
        return INSTANCE;
    }
}
