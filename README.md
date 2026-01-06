# TP gRPC avec Spring Boot - Gestion de Comptes Bancaires

Ce projet implémente un service gRPC avec Spring Boot pour gérer des comptes bancaires.

## Structure du Projet

```
grpc2/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ma/projet/grpc/
│   │   │       ├── Grpc2Application.java
│   │   │       ├── controllers/
│   │   │       │   └── CompteServiceImpl.java
│   │   │       ├── entities/
│   │   │       │   └── Compte.java
│   │   │       ├── repositories/
│   │   │       │   └── CompteRepository.java
│   │   │       └── services/
│   │   │           └── CompteService.java
│   │   └── resources/
│   │       ├── compte.proto
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Prérequis

- Java 20
- Maven 3.6+
- IntelliJ IDEA (ou autre IDE Java)

## Installation et Configuration

### 1. Compiler le projet

```bash
mvn clean compile
```

Cette commande va :
- Télécharger les dépendances
- Générer les classes Java à partir du fichier `.proto`
- Compiler le projet

### 2. Exécuter l'application

```bash
mvn spring-boot:run
```

Ou depuis IntelliJ IDEA :
- Ouvrir le projet
- Exécuter la classe `Grpc2Application`

## Configuration

Le serveur gRPC écoute sur le port **9090** (configuré dans `application.properties`).

La base de données H2 est utilisée en mémoire. Vous pouvez accéder à la console H2 à l'adresse :
```
http://localhost:8080/h2-console
```

## Services gRPC Disponibles

### 1. AllComptes
Récupère tous les comptes bancaires.

**Requête :**
```json
{}
```

### 2. CompteById
Récupère un compte par son ID.

**Requête :**
```json
{
  "id": "uuid-du-compte"
}
```

### 3. TotalSolde
Calcule les statistiques sur les soldes (nombre, somme, moyenne).

**Requête :**
```json
{}
```

### 4. SaveCompte
Crée ou met à jour un compte.

**Requête :**
```json
{
  "compte": {
    "solde": 1000.0,
    "dateCreation": "24-02-15",
    "type": "COURANT"
  }
}
```

## Tester avec BloomRPC

1. **Ouvrir BloomRPC**
2. **Importer le fichier proto** :
   - Cliquer sur "Import Protobuf"
   - Sélectionner `src/main/resources/compte.proto`
3. **Configurer le serveur** :
   - Adresse : `localhost:9090`
4. **Tester les méthodes** :
   - Sélectionner une méthode
   - Remplir les données JSON si nécessaire
   - Cliquer sur le bouton play pour exécuter

## Structure des Données

### Type de Compte (Enum)
- `COURANT` : Compte courant
- `EPARGNE` : Compte épargne

### Message Compte
- `id` : Identifiant unique (String, généré automatiquement)
- `solde` : Solde du compte (float)
- `dateCreation` : Date de création (String, format "jj-MM-aa")
- `type` : Type de compte (TypeCompte enum)

## Technologies Utilisées

- **Spring Boot** 3.2.0
- **gRPC** 1.53.0
- **Protocol Buffers** 3.22.0
- **Spring Data JPA**
- **H2 Database** (base de données en mémoire)
- **Lombok**

## Commandes Maven Utiles

```bash
# Compiler le projet
mvn clean compile

# Exécuter l'application
mvn spring-boot:run

# Générer les classes Proto uniquement
mvn protobuf:compile

# Nettoyer et reconstruire
mvn clean install
```

## Notes Importantes

- Les classes générées depuis le fichier `.proto` se trouvent dans `target/generated-sources/protobuf`
- Assurez-vous que ce répertoire est marqué comme "Generated Sources Root" dans IntelliJ
- Si les classes générées ne sont pas reconnues, exécutez `mvn clean compile` pour les régénérer

