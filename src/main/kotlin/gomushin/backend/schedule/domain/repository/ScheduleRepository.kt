package gomushin.backend.schedule.domain.repository

import gomushin.backend.schedule.domain.entity.Schedule
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository : JpaRepository<Schedule, Long>
