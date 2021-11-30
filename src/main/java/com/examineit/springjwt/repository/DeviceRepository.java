package com.examineit.springjwt.repository;

import com.examineit.springjwt.models.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends MongoRepository<Device, Long> {

    List<Device> findByUsersPermitted(String username);
    boolean existsByToken(String token);
    boolean existsByName(String name);
    Device findByToken(String token);

    @Override
    void delete(Device device);
}
