# Instructions pour générer les classes Protobuf

Les classes Java doivent être générées depuis le fichier `compte.proto` avant que le projet puisse compiler.

## Dans IntelliJ IDEA :

### Méthode 1 : Via la vue Maven (Recommandé)

1. **Ouvrir la vue Maven**
   - Menu : `View → Tool Windows → Maven`
   - Ou cliquez sur l'icône Maven dans la barre latérale droite

2. **Recharger le projet Maven**
   - Cliquez sur l'icône de rechargement (flèches circulaires) en haut de la vue Maven
   - Ou : `File → Reload Gradle/Maven Projects`

3. **Générer les classes Protobuf**
   - Dans la vue Maven, développez : `grpc2 → Plugins → protobuf`
   - Double-cliquez sur `protobuf:compile` (génère les messages)
   - Puis double-cliquez sur `protobuf:compile-custom` (génère le service gRPC)
   - Attendez que les deux tâches se terminent

4. **Marquer le dossier généré comme source**
   - Naviguez dans l'explorateur de fichiers vers : `target/generated-sources/protobuf/java`
   - Clic droit sur le dossier `java`
   - Sélectionnez : `Mark Directory as → Generated Sources Root`
   - Le dossier devrait devenir bleu (couleur des sources générées)

5. **Rebuild le projet**
   - Menu : `Build → Rebuild Project`
   - Ou raccourci : `Ctrl+Shift+F9`

### Méthode 2 : Via le menu Build

1. **Build automatique**
   - Menu : `Build → Build Project` (`Ctrl+F9`)
   - IntelliJ devrait détecter le plugin protobuf et générer les classes automatiquement

2. **Si les classes ne sont pas générées automatiquement**, suivez la Méthode 1

### Vérification

Après la génération, vous devriez voir dans `target/generated-sources/protobuf/java/ma/projet/grpc/stubs/` :
- `Compte.java`
- `CompteRequest.java`
- `CompteServiceGrpc.java`
- `GetAllComptesRequest.java`
- `GetAllComptesResponse.java`
- Et toutes les autres classes générées

### Si cela ne fonctionne toujours pas

1. Vérifiez que le fichier `compte.proto` est bien dans `src/main/resources/`
2. Vérifiez que Maven a bien téléchargé les plugins (onglet Maven → Lifecycle → clean)
3. Essayez un `mvn clean compile` depuis le terminal intégré d'IntelliJ
4. Vérifiez les logs dans la fenêtre "Build" pour voir les erreurs éventuelles

