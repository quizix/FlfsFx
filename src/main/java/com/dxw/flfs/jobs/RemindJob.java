package com.dxw.flfs.jobs;

import com.dxw.common.messages.Message;
import com.dxw.common.messages.MessageTags;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.mes.Site;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by zhang on 2016-04-30.
 */
public class RemindJob extends AbstractJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        HibernateService hibernateService = (HibernateService)
                ServiceRegistryImpl.getInstance().getService(Services.HIBERNATE_SERVICE);
        try (UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession())) {
            String siteCode = FlfsApp.getContext().getSiteCode();
            unitOfWork.begin();

            Site site = unitOfWork.getSiteRepository().findByNaturalId(siteCode);

            unitOfWork.commit();
            if( site != null){
                //stage==0表示还是处于入栏阶段
                if( site.getStage() ==0){
                    if(notificationManager != null){
                        Message n = new Message();
                        n.setContent("系统提示：请输入明天的入栏计划");
                        n.setWhen( System.currentTimeMillis());
                        notificationManager.notify(MessageTags.Remind, n);
                    }
                }
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
