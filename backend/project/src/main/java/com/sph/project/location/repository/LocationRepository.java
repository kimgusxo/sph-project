package com.sph.project.location.repository;

import com.sph.project.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT * " +
            "FROM location l " +
            "WHERE ST_DWithin(l.geometry::geography, ST_SetSRID(ST_MakePoint(:x, :y), 4326)::geography,:radius)", nativeQuery = true)
    List<Location> findAround(@Param("x") double x,
                              @Param("y") double y,
                              @Param("radius") double radius);
}
