package ma.projet.grpc.controllers;

import io.grpc.stub.StreamObserver;
import ma.projet.grpc.services.CompteService;
import ma.projet.grpc.stubs.Compte;
import ma.projet.grpc.stubs.CompteServiceGrpc;
import ma.projet.grpc.stubs.GetAllComptesRequest;
import ma.projet.grpc.stubs.GetAllComptesResponse;
import ma.projet.grpc.stubs.GetCompteByIdRequest;
import ma.projet.grpc.stubs.GetCompteByIdResponse;
import ma.projet.grpc.stubs.GetTotalSoldeRequest;
import ma.projet.grpc.stubs.GetTotalSoldeResponse;
import ma.projet.grpc.stubs.SaveCompteRequest;
import ma.projet.grpc.stubs.SaveCompteResponse;
import ma.projet.grpc.stubs.SoldeStats;
import ma.projet.grpc.stubs.TypeCompte;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@GrpcService
public class CompteServiceImpl extends CompteServiceGrpc.CompteServiceImplBase {
    private final CompteService compteService;
    
    public CompteServiceImpl(CompteService compteService) {
        this.compteService = compteService;
    }
    
    @Override
    public void allComptes(GetAllComptesRequest request, StreamObserver<GetAllComptesResponse> responseObserver) {
        System.out.println("=== AllComptes appelé ===");
        var comptes = compteService.findAllComptes().stream()
            .map(compte -> Compte.newBuilder()
                .setId(compte.getId())
                .setSolde(compte.getSolde())
                .setDateCreation(compte.getDateCreation())
                .setType(TypeCompte.valueOf(compte.getType()))
                .build())
            .collect(Collectors.toList());
        
        responseObserver.onNext(GetAllComptesResponse.newBuilder()
            .addAllComptes(comptes).build());
        responseObserver.onCompleted();
    }
    
    @Override
    public void compteById(GetCompteByIdRequest request, StreamObserver<GetCompteByIdResponse> responseObserver) {
        try {
            var compte = compteService.findCompteById(request.getId());
            if (compte != null) {
                var grpcCompte = Compte.newBuilder()
                    .setId(compte.getId())
                    .setSolde(compte.getSolde())
                    .setDateCreation(compte.getDateCreation())
                    .setType(TypeCompte.valueOf(compte.getType()))
                    .build();
                responseObserver.onNext(GetCompteByIdResponse.newBuilder()
                    .setCompte(grpcCompte).build());
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Compte non trouvé avec l'ID: " + request.getId())
                    .asException());
                // Ne pas appeler onCompleted() après onError()
            }
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                .withDescription("Erreur lors de la récupération: " + e.getMessage())
                .asException());
            // Ne pas appeler onCompleted() après onError()
        }
    }
    
    @Override
    public void totalSolde(GetTotalSoldeRequest request, StreamObserver<GetTotalSoldeResponse> responseObserver) {
        int count = compteService.getCount();
        float sum = compteService.getTotalSolde();
        float average = count > 0 ? sum / count : 0;
        
        SoldeStats stats = SoldeStats.newBuilder()
            .setCount(count)
            .setSum(sum)
            .setAverage(average)
            .build();
        
        responseObserver.onNext(GetTotalSoldeResponse.newBuilder().setStats(stats).build());
        responseObserver.onCompleted();
    }
    
    @Override
    @Transactional
    public void saveCompte(SaveCompteRequest request, StreamObserver<SaveCompteResponse> responseObserver) {
        try {
            System.out.println("=== SaveCompte appelé ===");
            var compteReq = request.getCompte();
            System.out.println("Données reçues - solde: " + compteReq.getSolde() + ", type: " + compteReq.getType());
            
            var compte = new ma.projet.grpc.entities.Compte();
            // Générer l'ID manuellement pour garantir qu'il est créé
            String id = java.util.UUID.randomUUID().toString();
            compte.setId(id);
            compte.setSolde(compteReq.getSolde());
            compte.setDateCreation(compteReq.getDateCreation());
            compte.setType(compteReq.getType().name());
            
            System.out.println("Compte avant sauvegarde: ID=" + compte.getId() + ", Solde=" + compte.getSolde());
            
            var savedCompte = compteService.saveCompte(compte);
            
            System.out.println("Compte après sauvegarde: ID=" + savedCompte.getId() + ", Solde=" + savedCompte.getSolde());
            
            // Vérifier immédiatement que le compte est en base
            var verifyAll = compteService.findAllComptes();
            System.out.println("Vérification - Nombre de comptes en base après sauvegarde: " + verifyAll.size());
            
            // S'assurer que l'ID est généré
            if (savedCompte.getId() == null) {
                responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Erreur: ID non généré pour le compte")
                    .asException());
                return; // Ne pas appeler onCompleted() après onError()
            }
            
            var grpcCompte = Compte.newBuilder()
                .setId(savedCompte.getId())
                .setSolde(savedCompte.getSolde())
                .setDateCreation(savedCompte.getDateCreation())
                .setType(TypeCompte.valueOf(savedCompte.getType()))
                .build();
            
            responseObserver.onNext(SaveCompteResponse.newBuilder()
                .setCompte(grpcCompte).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                .withDescription("Erreur lors de la sauvegarde: " + e.getMessage())
                .asException());
            // Ne pas appeler onCompleted() après onError()
        }
    }
}

