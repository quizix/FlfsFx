package com.dxw.flfs.app;

import com.dxw.common.services.ServiceRegistry;

/**
 * Created by zhang on 2016-05-26.
 */
public class DbInitiator {
    ServiceRegistry registry;

    public DbInitiator(ServiceRegistry registry){
        this.registry = registry;
    }

    public void prepareData(){
//        HibernateService hibernateService = (HibernateService)
//                this.registry.getService(Services.HIBERNATE_SERVICE);
//        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
//            dao.begin();
//
//            Shed shed = new Shed();
//            shed.setCreateTime(new Date());
//            shed.setModifyTime(new Date());
//            shed.setAddress("江西鄱阳");
//            shed.setCode("12345678");
//            shed.setActive(true);
//            shed.setName("猪舍1");
//            dao.update(shed);
//
//            Date now = new Date();
//            LocalDate later = TimeUtil.toLocalDate(now);
//            later.plusDays(10);
//            Batch batch = new Batch();
//            batch.setCode("1");
//            batch.setInStockNumber(100);
//            batch.setStartDate(now);
//            batch.setEndDate(now);
//            batch.setCreateTime(now);
//            batch.setModifyTime(now);
//
//            dao.update(batch);
//
//            Set<Batch> batches = new HashSet<>();
//            batches.add(batch);
//
//            Set<Sty> sties = new HashSet<>();
//            for (int i = 0; i < 24; i++) {
//                Sty sty = new Sty();
//                sty.setCreateTime(new Date());
//                sty.setModifyTime(new Date());
//                sty.setCode(Integer.toString(i));
//                sty.setName("Sty" + i);
//                sty.setLastNumber(80 + i);
//                sty.setCurrentNumber(100 + i);
//                sty.setShed(shed);
//                sties.add(sty);
//                sty.setNo(i);
//
//                dao.update(sty);
//            }
//
//            batch.setSties(sties);
//
//            dao.update(batch);
//
//            Site config = new Site();
//            config.setCreateTime(new Date());
//            config.setModifyTime(new Date());
//            //config.setBatchCode("1");
//            config.setSiteCode(FlfsApp.getContext().getSiteCode());
//            config.setHost("192.168.1.10");
//            dao.update(config);
//
//            dao.commit();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
