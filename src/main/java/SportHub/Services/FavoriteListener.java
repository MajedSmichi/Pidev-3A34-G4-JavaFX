package SportHub.Services;

import SportHub.Entity.Product;

public interface FavoriteListener {
    void onFavoriteToggle(Product product, boolean isFavorite);
}