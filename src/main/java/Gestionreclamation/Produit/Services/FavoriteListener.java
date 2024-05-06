package Gestionreclamation.Produit.Services;

import Gestionreclamation.Produit.Entity.Product;

public interface FavoriteListener {
    void onFavoriteToggle(Product product, boolean isFavorite);
}