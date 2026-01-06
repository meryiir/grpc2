# Guide de Test End-to-End avec BloomRPC

Ce guide vous montre comment tester le service gRPC de bout en bout avec BloomRPC.

## Prérequis

1. ✅ L'application Spring Boot doit être démarrée (serveur gRPC sur `localhost:9090`)
2. ✅ BloomRPC doit être ouvert avec le fichier `compte.proto` importé
3. ✅ L'adresse du serveur doit être configurée à `localhost:9090`

## Étapes de Test

### Test 1: Créer un compte avec SaveCompte

1. **Sélectionner la méthode `SaveCompte`** dans le panneau de gauche
2. **Dans l'Editor**, entrer le JSON suivant :
   ```json
   {
     "compte": {
       "solde": 1.1,
       "dateCreation": "24-02-15",
       "type": 0
     }
   }
   ```
   - `solde`: 1.1 (montant du compte)
   - `dateCreation`: "24-02-15" (date de création)
   - `type`: 0 = COURANT, 1 = EPARGNE

3. **Vérifier l'adresse** : doit être `localhost:9090`
4. **Cliquer sur le bouton Play** (vert)
5. **Vérifier la réponse** : Vous devriez recevoir un compte avec un `id` généré automatiquement, par exemple :
   ```json
   {
     "compte": {
       "id": "59f7700f-29a3-49e5-abd0-1a26ab865a16",
       "solde": 1.100000023841858,
       "dateCreation": "24-02-15",
       "type": "COURANT"
     }
   }
   ```
6. **📝 NOTER L'ID** généré (vous en aurez besoin pour le Test 4)

---

### Test 2: Appeler AllComptes

1. **Sélectionner la méthode `AllComptes`** dans le panneau de gauche
2. **Dans l'Editor**, laisser `{}` (objet vide - cette méthode ne prend pas de paramètres)
3. **Vérifier l'adresse** : `localhost:9090`
4. **Cliquer sur le bouton Play**
5. **Vérifier la réponse** : Vous devriez voir le compte créé dans la liste :
   ```json
   {
     "comptes": [
       {
         "id": "59f7700f-29a3-49e5-abd0-1a26ab865a16",
         "solde": 1.100000023841858,
         "dateCreation": "24-02-15",
         "type": "COURANT"
       }
     ]
   }
   ```
6. **✅ Vérification** : Le compte créé au Test 1 doit apparaître dans cette liste

---

### Test 3: Appeler TotalSolde

1. **Sélectionner la méthode `TotalSolde`** dans le panneau de gauche
2. **Dans l'Editor**, laisser `{}` (objet vide)
3. **Vérifier l'adresse** : `localhost:9090`
4. **Cliquer sur le bouton Play**
5. **Vérifier la réponse** : Vous devriez recevoir les statistiques :
   ```json
   {
     "stats": {
       "count": 1,
       "sum": 1.1,
       "average": 1.1
     }
   }
   ```
6. **✅ Vérification** :
   - `count` doit être **1** (1 compte créé)
   - `sum` doit être **1.1** (somme des soldes)
   - `average` doit être **1.1** (moyenne des soldes)

---

### Test 4: Utiliser CompteById avec l'ID créé

1. **Sélectionner la méthode `CompteById`** dans le panneau de gauche
2. **Dans l'Editor**, entrer le JSON avec l'ID noté au Test 1 :
   ```json
   {
     "id": "59f7700f-29a3-49e5-abd0-1a26ab865a16"
   }
   ```
   ⚠️ **Remplacez l'ID** par celui que vous avez reçu au Test 1 !

3. **Vérifier l'adresse** : `localhost:9090`
4. **Cliquer sur le bouton Play**
5. **Vérifier la réponse** : Vous devriez recevoir le compte complet :
   ```json
   {
     "compte": {
       "id": "59f7700f-29a3-49e5-abd0-1a26ab865a16",
       "solde": 1.100000023841858,
       "dateCreation": "24-02-15",
       "type": "COURANT"
     }
   }
   ```
6. **✅ Vérification** : Le compte retourné doit correspondre à celui créé au Test 1

---

## Résumé des Tests

Si tous les tests passent, vous avez vérifié :

✅ **SaveCompte** : Création de compte fonctionne  
✅ **AllComptes** : Liste des comptes fonctionne  
✅ **TotalSolde** : Statistiques calculées correctement  
✅ **CompteById** : Récupération par ID fonctionne  

🎉 **Votre application gRPC fonctionne correctement end-to-end !**

---

## Test Bonus: Créer plusieurs comptes

Pour tester avec plusieurs comptes :

1. Créez 2-3 comptes supplémentaires avec `SaveCompte` (avec des soldes différents)
2. Appelez `AllComptes` - vous devriez voir tous les comptes
3. Appelez `TotalSolde` - les statistiques doivent être mises à jour (count=3, sum=somme totale, average=moyenne)

---

## Dépannage

### Erreur "14 UNAVAILABLE"
- Vérifiez que l'application Spring Boot est démarrée
- Vérifiez que l'adresse est bien `localhost:9090`

### Erreur "2 UNKNOWN" sur CompteById
- Vérifiez que vous utilisez un ID valide (celui créé avec SaveCompte)
- Vérifiez que l'ID est bien entre guillemets dans le JSON

### AllComptes retourne une liste vide
- Créez d'abord un compte avec `SaveCompte`
- Vérifiez que la base de données H2 est bien utilisée (l'application doit être redémarrée pour réinitialiser la base en mémoire)

