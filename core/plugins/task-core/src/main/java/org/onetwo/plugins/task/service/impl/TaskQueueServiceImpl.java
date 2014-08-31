package org.onetwo.plugins.task.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.hibernate.HibernateCrudServiceImpl;
import org.onetwo.common.hibernate.HibernateUtils;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.plugins.task.TaskPluginConfig;
import org.onetwo.plugins.task.entity.TaskExecLog;
import org.onetwo.plugins.task.entity.TaskQueue;
import org.onetwo.plugins.task.entity.TaskQueueArchived;
import org.onetwo.plugins.task.utils.TaskConstant.TaskExecResult;
import org.onetwo.plugins.task.utils.TaskConstant.TaskStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskQueueServiceImpl extends HibernateCrudServiceImpl<TaskQueue, Long>{
	
	@Resource
	private TaskPluginConfig taskPluginConfig;
	
	public List<TaskQueue> loadAllExecuting(){
		List<TaskQueue> queues = findByProperties("status", TaskStatus.EXECUTING);
		return queues;
	}
	
	public List<TaskQueue> loadAndLockWaiting(int size){
		List<TaskQueue> queues = findByProperties("status", TaskStatus.WAITING, "planTime:<", DateUtil.now(), K.MAX_RESULTS, size);
		for(TaskQueue tq : queues){
			tq.setStatus(TaskStatus.EXECUTING);
		}
		return queues;
	}

	/***
	 * 添加到数据库队列
	 * @param queue
	 */
	public TaskQueue save(TaskQueue queue){
		queue.setTaskCreateTime(DateUtil.now());
		queue.setCurrentTimes(0);
		queue.setStatus(TaskStatus.WAITING);
		if(queue.getTryTimes()==null)
			queue.setTryTimes(taskPluginConfig.getTryTimes());
		return super.save(queue);
	}
	
	public TaskQueueArchived archived(TaskQueue taskQueue, TaskExecResult result){
		TaskQueueArchived archived = new TaskQueueArchived();
		HibernateUtils.copyIgnoreRelationsAndFields(taskQueue, archived, "status");
		archived.setArchivedTime(DateUtil.now());
		archived.setResult(result);
		archived.setTask(taskQueue.getTask());
		getBaseEntityManager().save(archived);
		getBaseEntityManager().remove(taskQueue);
		return archived;
	}
	
	public TaskExecLog logExec(TaskExecLog log){
		getBaseEntityManager().save(log);
		return log;
	}
}