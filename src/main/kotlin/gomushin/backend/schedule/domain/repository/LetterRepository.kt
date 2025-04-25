package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Letter
import org.springframework.data.jpa.repository.JpaRepository

interface LetterRepository : JpaRepository<Letter, Long>
