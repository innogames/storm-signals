// Copyright (c) P. Taylor Goetz (ptgoetz@gmail.com)

package org.apache.storm.contrib.signals.bolt;

import org.apache.storm.contrib.signals.SignalListener;
import org.apache.storm.contrib.signals.StormSignalConnection;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.base.BaseRichBolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@SuppressWarnings("serial")
public abstract class BaseSignalBolt extends BaseRichBolt implements SignalListener {

    private static final Logger LOG = LoggerFactory.getLogger(BaseSignalBolt.class);
    private String name;
    private StormSignalConnection signalConnection;

    public BaseSignalBolt(String name) {
        this.name = name;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        try {
            this.signalConnection = new StormSignalConnection(this.name, this);
            this.signalConnection.init(conf);
        } catch (Exception e) {
            LOG.error("Error SignalConnection.", e);
        }
    }


    public void sendSignal(String toPath, byte[] signal)throws Exception {
        this.signalConnection.send(toPath, signal);
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        super.cleanup();
        this.signalConnection.close();

    }

}
