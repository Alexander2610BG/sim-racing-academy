package aleksandarskachkov.simracingacademy.wallet.repository;

import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findAllByOwnerUsername(String username);

    Wallet findByOwnerId(UUID id);

    Optional<Wallet> findByIdAndOwnerId(UUID walletId, UUID userId);

}
