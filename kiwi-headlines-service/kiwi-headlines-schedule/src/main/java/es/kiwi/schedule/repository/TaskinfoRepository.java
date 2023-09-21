package es.kiwi.schedule.repository;

import es.kiwi.model.schedule.pojos.Taskinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;
import java.util.List;

public interface TaskinfoRepository extends JpaRepository<Taskinfo, Long>, JpaSpecificationExecutor<Taskinfo>, QuerydslPredicateExecutor<Taskinfo> {

    List<Taskinfo> findByTaskTypeAndPriorityAndExecuteTimeLessThan(int taskType, int priority, Date time);

    List<Taskinfo> findByExecuteTimeLessThan(Date time);
}
