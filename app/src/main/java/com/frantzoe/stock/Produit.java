package com.frantzoe.stock;

/**
 * Created by Lincoln on 3/18/2015.
 */

public class Produit {

    private String _libelle;
    private int _quantite;

    public Produit (String libelle, int quantite) {
        _libelle = libelle;
        _quantite = quantite;
    }

    public String get_libelle() {
        return _libelle;
    }

    public int get_quantite() {
        return _quantite;
    }
}
