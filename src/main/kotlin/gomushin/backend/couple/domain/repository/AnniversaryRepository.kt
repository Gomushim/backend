package gomushin.backend.couple.domain.repository

import gomushin.backend.couple.domain.entity.Anniversary
import org.springframework.data.jpa.repository.JpaRepository

interface AnniversaryRepository : JpaRepository<Anniversary, Long>
