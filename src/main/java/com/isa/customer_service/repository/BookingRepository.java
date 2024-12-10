package com.isa.customer_service.repository;

import com.isa.customer_service.enums.ServiceStatus;
import com.isa.customer_service.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {

    List<Bookings> findByUserId(Long userId);  // To get all bookings for a specific user
    Optional<Bookings> findByBookingId(Long bookingId);

    @Query("SELECT b FROM Bookings b WHERE b.userId = :userId AND b.vehicleNo = :vehicleNo AND b.serviceId = :serviceId AND " +
            "b.createdAt >= :startDate AND b.createdAt <= :endDate")
    Optional<Bookings> findExistingBookingForUser(
            @Param("userId") Long userId,
            @Param("vehicleNo") String vehicleNo,
            @Param("serviceId") Long serviceId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    List<Bookings> findByServiceAcceptStatus(ServiceStatus serviceStatus);
}
