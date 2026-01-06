package ma.projet.grpc.services;

import ma.projet.grpc.entities.Compte;
import ma.projet.grpc.repositories.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
public class CompteService {
    private final CompteRepository compteRepository;
    
    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }
    
    public List<Compte> findAllComptes() {
        List<Compte> comptes = compteRepository.findAll();
        System.out.println("Found " + comptes.size() + " comptes in database"); // Debug
        return comptes;
    }
    
    public Compte findCompteById(String id) {
        return compteRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Compte saveCompte(Compte compte) {
        System.out.println("Saving compte: " + compte); // Debug
        System.out.println("Transaction active: " + TransactionSynchronizationManager.isActualTransactionActive());
        
        Compte saved = compteRepository.save(compte);
        compteRepository.flush(); // Force l'écriture immédiate en base
        
        System.out.println("Compte saved with ID: " + saved.getId()); // Debug
        
        // Vérifier que le compte est bien en base immédiatement
        Compte verify = compteRepository.findById(saved.getId()).orElse(null);
        if (verify != null) {
            System.out.println("✅ Compte vérifié en base: " + verify.getId());
        } else {
            System.out.println("❌ ERREUR: Compte non trouvé en base après sauvegarde!");
        }
        
        return saved;
    }
    
    public float getTotalSolde() {
        return compteRepository.findAll().stream()
            .map(Compte::getSolde)
            .reduce(0.0f, Float::sum);
    }
    
    public int getCount() {
        return (int) compteRepository.count();
    }
}

