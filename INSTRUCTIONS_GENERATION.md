# ⚠️ IMPORTANT : Générer les classes Protobuf avant de compiler

Les classes Java doivent être générées depuis `compte.proto` avant que le projet compile.

## ✅ Solution rapide dans IntelliJ IDEA

### Étape 1 : Ouvrir la vue Maven
- Menu : `View → Tool Windows → Maven`
- **OU** cliquez sur l'icône "Maven" dans la barre latérale droite
- **OU** raccourci : `Alt+1` puis tapez "Maven"

### Étape 2 : Recharger le projet Maven
- Dans la vue Maven, cliquez sur l'icône **"Reload All Maven Projects"** (deux flèches circulaires) en haut
- Attendez que le rechargement soit terminé

### Étape 3 : Générer les classes Protobuf
Dans la vue Maven, développez l'arborescence suivante :
```
grpc2
  └── Plugins
      └── protobuf
```

Vous verrez deux goals :
1. **Double-cliquez sur `protobuf:compile`**
   - Cela génère les classes de messages (Compte, GetAllComptesRequest, etc.)
   - Attendez que la tâche se termine (regardez la fenêtre "Build" en bas)

2. **Double-cliquez sur `protobuf:compile-custom`**
   - Cela génère les classes de service gRPC (CompteServiceGrpc)
   - Attendez que la tâche se termine

### Étape 4 : Marquer le dossier généré comme source
1. Dans l'explorateur de fichiers d'IntelliJ, naviguez vers :
   ```
   target/generated-sources/protobuf/java
   ```

2. **Clic droit** sur le dossier `java`

3. Sélectionnez : **`Mark Directory as → Generated Sources Root`**
   - Le dossier devrait devenir **bleu** (couleur des sources générées)

### Étape 5 : Vérifier que les classes sont générées
Ouvrez dans l'explorateur :
```
target/generated-sources/protobuf/java/ma/projet/grpc/stubs/
```

Vous devriez voir :
- ✅ `CompteServiceGrpc.java`
- ✅ `Compte.java`
- ✅ `GetAllComptesRequest.java`
- ✅ `GetAllComptesResponse.java`
- ✅ Et d'autres classes...

### Étape 6 : Rebuild le projet
- Menu : `Build → Rebuild Project`
- **OU** raccourci : `Ctrl+Shift+F9`

## 🔍 Vérification après génération

Si tout s'est bien passé, vous ne devriez plus avoir d'erreurs de compilation.

## ❌ Si cela ne fonctionne pas

### Vérification 1 : Le fichier proto existe-t-il ?
Vérifiez que `src/main/resources/compte.proto` existe bien.

### Vérification 2 : Maven est-il configuré ?
- `File → Settings → Build, Execution, Deployment → Build Tools → Maven`
- Vérifiez que "Maven home path" pointe vers une installation Maven valide

### Vérification 3 : Vérifier les logs
- Ouvrez la fenêtre "Build" en bas d'IntelliJ
- Regardez les erreurs dans les logs lors de l'exécution de `protobuf:compile`

### Vérification 4 : Nettoyer et réessayer
Dans la vue Maven :
1. `grpc2 → Lifecycle → clean` (double-clic)
2. Puis `grpc2 → Plugins → protobuf → protobuf:compile` (double-clic)
3. Puis `grpc2 → Plugins → protobuf → protobuf:compile-custom` (double-clic)

## 📝 Note importante

Les classes protobuf sont générées dans `target/` qui est généralement ignoré par Git. 
Ces classes sont **régénérées automatiquement** à chaque compilation Maven.

