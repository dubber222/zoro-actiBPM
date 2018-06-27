package com.zoro;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * Created on 2018/6/27.
 *
 * @author dubber
 */
public class LeaveActivitiTest {

    /**
     * 会默认按照Resources目录下的activiti.cfg.xml创建流程引擎
     */
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void test() {
        //以下两种方式选择一种创建引擎方式：1.配置写在程序里 2.读对应的配置文件
        //1
        //testCreateProcessEngine();
        //2
        testCreateProcessEngineByCfgXml();

        deployProcess();
        startProcess();
        queryTask();
        //handleTask();
    }

    /**
     * 根据配置文件activiti.cfg.xml创建ProcessEngine
     */
    @Test
    public void testCreateProcessEngineByCfgXml() {

        //1.创建一个流程引擎配置对象
        String resource="activiti.cfg.xml";
        // 1、创建一个流程引擎配置对象
        // 2、 通过 activiti.cfg.xml 配置数据源
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(resource);

        // 3、创建流程引擎对象，如果设置了databaseSchema = true,自动创建表
        ProcessEngine engine = cfg.buildProcessEngine();


    }

    /**
     * 部署流程定义(操作数据表：act_re_deployment、act_re_procdef、act_ge_bytearray)
     *
     */
    @Test
    public void deployProcess() {
        // 获得一个部署构建器对象，用于加载流程定义文件（test1.bpmn,test.png）完成流程定义的部署
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        // 加载流程定义文件
        builder.addClasspathResource("leave.bpmn");

        // 部署流程定义
        Deployment deployment = builder.deploy();
        System.out.println(deployment.getId());
    }


    /**
     * 启动流程
     * RuntimeService
     */
    @Test
    public void startProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //可根据id、key、message启动流程
        runtimeService.startProcessInstanceByKey("myProcess_1");
    }


    /**
     * 查看任务
     * TaskService
     */
    @Test
    public void queryTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据assignee(代理人)查询任务
        String assignee = "emp";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();

        int size = tasks.size();
        for (int i = 0; i < size; i++) {
            Task task = tasks.get(i);
        }

        for (Task task : tasks) {
            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
        }
    }

    /**
     * 办理任务
     */
    @Test
    public void handleTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据上一步生成的taskId执行任务
        String taskId = "2502";
        taskService.complete(taskId);
    }
}
