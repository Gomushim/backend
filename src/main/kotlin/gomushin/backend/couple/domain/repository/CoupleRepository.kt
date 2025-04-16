package gomushin.backend.couple.domain.repository

import gomushin.backend.couple.domain.entity.Couple
import org.springframework.data.jpa.repository.JpaRepository

interface CoupleRepository : JpaRepository<Couple, Long>
