package com.dxw.flfs.data.dal;

import com.dxw.flfs.data.models.erp.Medicine;
import com.dxw.flfs.data.models.erp.Pig;
import com.dxw.flfs.data.models.erp.Privilege;
import com.dxw.flfs.data.models.erp.User;
import com.dxw.flfs.data.models.mes.PigletPlan;
import com.dxw.flfs.data.models.mes.Shed;
import com.dxw.flfs.data.models.mes.Site;
import com.dxw.flfs.data.models.mes.Sty;
import org.hibernate.Session;

/**
 * Created by zhang on 2016-04-28.
 */
public class UnitOfWork implements AutoCloseable {

    private Session session;

    private DefaultGenericRepository<User> userRepository;
    private DefaultGenericRepository<Privilege> privilegeRepository;
    private DefaultGenericRepository<Shed> shedRepository;
    private DefaultGenericRepository<Sty> styRepository;
    private DefaultGenericRepository<Site> siteConfigRepository;
    private DefaultGenericRepository<PigletPlan> pigletPlanRepository;
    private DefaultGenericRepository<Pig> pigRepository;
    private DefaultGenericRepository<Medicine> medicineRepository;

    public UnitOfWork(Session session){
        this.session = session;
    }

    /*public void flush(){
        session.flush();
    }*/
    public DefaultGenericRepository<User> getUserRepository() {
        if( userRepository == null)
            userRepository = new DefaultGenericRepository<>(session, User.class);
        return userRepository;
    }

    public DefaultGenericRepository<Privilege> getPrivilegeRepository() {
        if( privilegeRepository == null)
            privilegeRepository = new DefaultGenericRepository<>(session, Privilege.class);
        return privilegeRepository;
    }

    public DefaultGenericRepository<Site> getSiteRepository() {
        if(siteConfigRepository==null )
            siteConfigRepository = new DefaultGenericRepository<>(session, Site.class);
        return siteConfigRepository;
    }
    public DefaultGenericRepository<Shed> getShedRepository() {
        if( shedRepository == null)
            shedRepository = new DefaultGenericRepository<>(session, Shed.class);
        return shedRepository;
    }

    public DefaultGenericRepository<Sty> getStyRepository() {
        if( styRepository == null)
            styRepository = new DefaultGenericRepository<>(session, Sty.class);
        return styRepository;
    }

    public DefaultGenericRepository<PigletPlan> getPigletPlanRepository() {
        if( pigletPlanRepository == null)
            pigletPlanRepository = new DefaultGenericRepository<>(session, PigletPlan.class);
        return pigletPlanRepository;
    }

    public DefaultGenericRepository<Pig> getPigRepository() {
        if( pigRepository == null)
            pigRepository = new DefaultGenericRepository<>(session, Pig.class);
        return pigRepository;
    }

    public DefaultGenericRepository<Medicine> getMedicineRepository() {
        if( medicineRepository == null)
            medicineRepository = new DefaultGenericRepository<>(session, Medicine.class);
        return medicineRepository;
    }

    @Override
    public void close() throws Exception {
        if( this.session!= null)
            session.close();
    }

    public void begin(){
        if( this.session != null)
            session.beginTransaction();
    }

    public void commit(){
        if( this.session != null)
            session.getTransaction().commit();
    }

    public void rollback(){
        if( this.session != null)
            session.getTransaction().rollback();
    }



}
