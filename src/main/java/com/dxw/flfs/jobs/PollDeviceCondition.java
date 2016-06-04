package com.dxw.flfs.jobs;

import com.dxw.flfs.communication.protocol.PlcDelegate;
import com.dxw.flfs.communication.protocol.PlcDelegateFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by zhang on 2016-06-04.
 */
public class PollDeviceCondition extends AbstractJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();

        delegate.getFlowValues();

        delegate.getValveAndPumpCondition();

        delegate.getPumpCondition();
    }
}
