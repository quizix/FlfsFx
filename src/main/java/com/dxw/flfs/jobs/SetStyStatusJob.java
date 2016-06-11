/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.communication.protocol.PlcDelegate;
import com.dxw.flfs.communication.protocol.PlcDelegateFactory;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.mes.Site;
import com.dxw.flfs.data.models.mes.Sty;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Optional;
import java.util.Set;

/**
 * 发送栏位空/满信息
 * 每一分钟执行一次
 *
 * @author pronics3
 */
public class SetStyStatusJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        notify("开始发送栏位空/满信息");
        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();

        short[] status = getStyStatus();

        if (status != null) {
            //先设置栏位状态
            delegate.setStyStatus(status);
            //再设置栏位设置更新标志
            delegate.setStyStatusUpdateFlag();
        }
    }

    private short[] getStyStatus() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        HibernateService hibernateService = (HibernateService) registry.getService(Services.HIBERNATE_SERVICE);

        try (UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession())) {
            String siteCode = FlfsApp.getContext().getSiteCode();

            Site site = unitOfWork.getSiteRepository().findByNaturalId(siteCode);

            if (site != null) {
                short[] status = new short[24];

                Set<Sty> sties = site.getSties();
                int size = Math.min(status.length, sties.size());

                for (int i = 0; i < size; i++) {
                    final short no = (short)i;
                    Optional<Sty> sty = sties.stream().filter(s -> s.getNo() == no)
                            .findFirst();
                    if (sty.isPresent()) {
                        status[i] = (short) (sty.get().getCurrentNumber() > 0 ? 0 : 1);
                    }
                }
                return status;
            }
        } catch (Exception ex) {

        }
        return null;
    }
}
