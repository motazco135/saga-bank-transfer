package com.bank.trasfer.domain.repository;

import com.bank.trasfer.domain.TransferEntity;
import com.bank.trasfer.domain.dto.TransferState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, UUID> {

    @Modifying
    @Query("Update TransferEntity t SET  t.feeAmount = :feeAmount where t.paymentId = :paymentId ")
    int updateFeeAmount(@Param("feeAmount") int feeAmount, @Param("paymentId") UUID paymentId);

    @Modifying
    @Query("Update TransferEntity t SET t.state = :state where t.paymentId = :paymentId")
    int updateTransferStatus( @Param("paymentId") UUID paymentId,@Param("state") TransferState state );
}
