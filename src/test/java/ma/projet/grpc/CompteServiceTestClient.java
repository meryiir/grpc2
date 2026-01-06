package ma.projet.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ma.projet.grpc.stubs.*;

import java.util.UUID;

/**
 * Client de test pour vérifier le fonctionnement end-to-end du service gRPC
 */
public class CompteServiceTestClient {
    
    private static final String HOST = "localhost";
    private static final int PORT = 9090;
    
    public static void main(String[] args) {
        System.out.println("=== Test End-to-End du Service gRPC CompteService ===\n");
        
        // Créer un channel gRPC
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext()
                .build();
        
        try {
            // Créer un stub pour le service
            CompteServiceGrpc.CompteServiceBlockingStub stub = CompteServiceGrpc.newBlockingStub(channel);
            
            // Test 1: Créer un compte avec SaveCompte
            System.out.println("📝 Test 1: Création d'un compte...");
            SaveCompteRequest saveRequest = SaveCompteRequest.newBuilder()
                    .setCompte(CompteRequest.newBuilder()
                            .setSolde(1.1f)
                            .setDateCreation("24-02-15")
                            .setType(TypeCompte.COURANT)
                            .build())
                    .build();
            
            SaveCompteResponse saveResponse = stub.saveCompte(saveRequest);
            String compteId = saveResponse.getCompte().getId();
            System.out.println("✅ Compte créé avec succès!");
            System.out.println("   ID: " + compteId);
            System.out.println("   Solde: " + saveResponse.getCompte().getSolde());
            System.out.println("   Type: " + saveResponse.getCompte().getType());
            System.out.println();
            
            // Test 2: Appeler AllComptes pour vérifier que le compte apparaît
            System.out.println("📋 Test 2: Récupération de tous les comptes...");
            GetAllComptesRequest allRequest = GetAllComptesRequest.newBuilder().build();
            GetAllComptesResponse allResponse = stub.allComptes(allRequest);
            
            System.out.println("✅ Liste des comptes récupérée!");
            System.out.println("   Nombre de comptes: " + allResponse.getComptesCount());
            
            if (allResponse.getComptesCount() > 0) {
                System.out.println("   Comptes trouvés:");
                for (Compte compte : allResponse.getComptesList()) {
                    System.out.println("   - ID: " + compte.getId() + 
                                     ", Solde: " + compte.getSolde() + 
                                     ", Type: " + compte.getType());
                }
            } else {
                System.out.println("   ⚠️  Aucun compte trouvé dans la liste!");
            }
            System.out.println();
            
            // Test 3: Appeler TotalSolde pour vérifier les statistiques
            System.out.println("📊 Test 3: Calcul des statistiques de solde...");
            GetTotalSoldeRequest totalRequest = GetTotalSoldeRequest.newBuilder().build();
            GetTotalSoldeResponse totalResponse = stub.totalSolde(totalRequest);
            
            SoldeStats stats = totalResponse.getStats();
            System.out.println("✅ Statistiques calculées!");
            System.out.println("   Nombre de comptes: " + stats.getCount());
            System.out.println("   Somme des soldes: " + stats.getSum());
            System.out.println("   Moyenne des soldes: " + stats.getAverage());
            
            // Vérification des valeurs attendues
            if (stats.getCount() == 1 && 
                Math.abs(stats.getSum() - 1.1f) < 0.001 && 
                Math.abs(stats.getAverage() - 1.1f) < 0.001) {
                System.out.println("   ✅ Valeurs correctes! (count=1, sum=1.1, average=1.1)");
            } else {
                System.out.println("   ⚠️  Valeurs différentes de celles attendues");
            }
            System.out.println();
            
            // Test 4: Utiliser CompteById avec l'ID créé
            System.out.println("🔍 Test 4: Récupération du compte par ID...");
            GetCompteByIdRequest byIdRequest = GetCompteByIdRequest.newBuilder()
                    .setId(compteId)
                    .build();
            
            try {
                GetCompteByIdResponse byIdResponse = stub.compteById(byIdRequest);
                Compte compte = byIdResponse.getCompte();
                
                System.out.println("✅ Compte trouvé par ID!");
                System.out.println("   ID: " + compte.getId());
                System.out.println("   Solde: " + compte.getSolde());
                System.out.println("   Date de création: " + compte.getDateCreation());
                System.out.println("   Type: " + compte.getType());
                
                // Vérification que c'est le bon compte
                if (compte.getId().equals(compteId)) {
                    System.out.println("   ✅ C'est bien le compte créé précédemment!");
                }
            } catch (Exception e) {
                System.out.println("   ❌ Erreur lors de la récupération: " + e.getMessage());
            }
            System.out.println();
            
            // Résumé des tests
            System.out.println("=== Résumé des Tests ===");
            System.out.println("✅ SaveCompte: Compte créé avec succès");
            System.out.println("✅ AllComptes: Liste récupérée (" + allResponse.getComptesCount() + " compte(s))");
            System.out.println("✅ TotalSolde: Statistiques calculées (count=" + stats.getCount() + 
                             ", sum=" + stats.getSum() + ", average=" + stats.getAverage() + ")");
            System.out.println("✅ CompteById: Test effectué");
            System.out.println("\n🎉 Tous les tests end-to-end sont terminés!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors des tests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermer le channel
            channel.shutdown();
        }
    }
}

