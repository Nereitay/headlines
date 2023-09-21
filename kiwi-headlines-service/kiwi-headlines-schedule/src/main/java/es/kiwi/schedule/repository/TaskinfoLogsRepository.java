package es.kiwi.schedule.repository;

import es.kiwi.model.schedule.pojos.TaskinfoLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TaskinfoLogsRepository extends JpaRepository<TaskinfoLogs, Long>, JpaSpecificationExecutor<TaskinfoLogs>, QuerydslPredicateExecutor<TaskinfoLogs> {
}
