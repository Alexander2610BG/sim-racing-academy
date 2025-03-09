package aleksandarskachkov.simracingacademy.user.repository;

import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Wallet getWalletById(UUID id);
}
