package aleksandarskachkov.simracingacademy.wallet.repository;

import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findAllByOwnerUsername(String username);
}
