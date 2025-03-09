package aleksandarskachkov.simracingacademy.transaction.repository;

import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findAllBySenderOrReceiverOrderByCreatedOnDesc(String sender, String receiver);

    List<Transaction> findAllByOwnerIdOrderByCreatedOnDesc(UUID userId);
}
