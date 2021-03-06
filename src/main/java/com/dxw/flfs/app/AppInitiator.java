package com.dxw.flfs.app;

import com.dxw.common.messages.MessageBus;
import com.dxw.common.messages.MessageBusImpl;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;
import com.dxw.flfs.jobs.JobManager;
import com.dxw.flfs.scheduling.AdaptiveScheduler;
import com.dxw.flfs.scheduling.AdvancedDistributor;
import com.dxw.flfs.scheduling.FlfsScheduler;

/**
 * Created by Administrator on 2016/4/7.
 */
public class AppInitiator {

    ServiceRegistry registry;
    DbInitiator dbInitializer;
    public AppInitiator(ServiceRegistry registry){
        this.registry = registry;
        dbInitializer = new DbInitiator(registry);
    }
    /**
     * 初始化系统服务
     *
     * @throws ServiceException
     */
    public void initServices() throws ServiceException {
        registerNotificationService();

        registerHibernateService();

        registerScheduler();

        registerJobManager();

        //dbInitializer.prepareData();
    }

    private void registerJobManager() {
        try {
            JobManager jobManager = new JobManager();
            jobManager.init();
            registry.register(jobManager);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void registerScheduler() throws ServiceException {
        HibernateService hibernateService = (HibernateService)
                registry.getService(Services.SCHEDULER_SERVICE);
        FlfsScheduler scheduler = new AdaptiveScheduler(hibernateService,
                new AdvancedDistributor()
        );
        registry.register(scheduler);
    }

    private void registerNotificationService() throws ServiceException {
        MessageBus notificationManager = new MessageBusImpl();
        notificationManager.init();
        registry.register(notificationManager);
    }

    private void registerHibernateService() throws ServiceException {
        HibernateService hibernateService = new HibernateServiceImpl();
        hibernateService.init();
        registry.register(hibernateService);
    }

    public void dispose(){
        if( registry!= null){
            try {
                registry.dispose();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

}
