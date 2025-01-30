    package com.example.doan.Repository;

    import com.example.doan.Entity.Order;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface OrderRepository  extends JpaRepository<Order, Integer> {
        Page<Order> findAllByCustomerorderId(Pageable pageable,String customerId);
    }
