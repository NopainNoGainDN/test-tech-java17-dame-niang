Test Technique Java17 - Backend Panier

## Lancement
- Java 17 requis
- `mvn spring-boot:run`
- Application disponible sur `http://localhost:8080`

## Endpoints
- `POST /users/{userId}/cart/items` : Ajouter un article
- `GET /users/{userId}/cart` : Consulter le panier
- `POST /users/{userId}/checkout` : Valider le panier

## Choix techniques
- Stockage en mémoire (`ConcurrentHashMap`)
- BigDecimal pour les prix
- Domain clair et séparé des services et controllers
- Exceptions métier explicites
