package io.nelium.archive.zmq;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;

import io.nelium.archive.filter.ShardingFilter;
import io.nelium.archive.model.SignatureFragment;
import io.nelium.archive.model.Transaction;
import io.nelium.archive.processor.ConfirmedTransactionProcessor;
import io.nelium.archive.processor.CreatedTransactionProcessor;
import io.nelium.archive.processor.SignatureFragmentProcessor;
import io.nelium.iota.utils.TransactionUtils;


@Service
public class ZeroMQService extends Thread {
	@Value("#{'${io.nelium.zmq.urls}'.split(',')}") protected List<String> zmqUrls= new ArrayList<String>();
	@Value("${io.nelium.zmq.enabled}") protected boolean zmqEnabled;
	@Autowired protected TaskExecutor taskExecutor;
	@Autowired protected CreatedTransactionProcessor createdTransactionProcessor;
	@Autowired protected ConfirmedTransactionProcessor confirmedTransactionProcessor;
	@Autowired protected SignatureFragmentProcessor signatureFragmentProcessor;
	@Autowired protected ShardingFilter shardingFilter;
	
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
    
	@PostConstruct
	public void executeAsynchronously() {
		if(!zmqEnabled){
			logger.info("ZMQ not enabled. Skipping ZMQ worker initialization"); 
			return;
		}
		for(String zmqUrl:zmqUrls){
			ZeroMQWorker worker= new ZeroMQWorker(zmqUrl,this);
			taskExecutor.execute(worker);
		}
	}

}

class ZeroMQWorker implements Runnable{
	String iriUrl;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected ZeroMQService service;
    
    public ZeroMQWorker(String iriUrl,ZeroMQService service    		) {
		this.iriUrl=iriUrl.trim();
    	this.iriUrl = ((this.iriUrl.startsWith("tcp") || this.iriUrl.startsWith("udp"))?"":"tcp://")+this.iriUrl;
    	this.service=service;
	}
		@Override
		public void run() {
			
			boolean connected=true;
			while(true){
				try{
					ZMQ.Context context = ZMQ.context(1);
					ZMQ.Socket requester = context.socket(ZMQ.SUB);
					logger.info("Connecting to IRI at " + iriUrl);
					requester.connect(iriUrl);
					requester.subscribe("sn"); 
					requester.subscribe("tx"); 
					connected=true;
					logger.info("Collecting data from IRI via ZMQ");
					while (connected) {
						String string = requester.recvStr(0).trim();
						if (string!=null && string.length()>10) {
							try {
								String topic=string.substring(0, string.indexOf(" ")).trim();
								if(topic.trim().equals("tx")){
									logger.debug("Processing creation:"+string);
									Transaction transaction=TransactionUtils.parseTransaction(string);
									if(service.shardingFilter.accept(transaction)){
										service.createdTransactionProcessor.process(transaction);
									}
								}
								if(topic.trim().equals("sn")){
									logger.debug("Processing confirmation:"+string);
									Transaction transaction=TransactionUtils.parseTransaction(string);
									if(service.shardingFilter.accept(transaction)){
										service.confirmedTransactionProcessor.process(transaction);
									}
								}
								if(topic.trim().equals("tx_trytes")){
									logger.debug("Processing signature fragment:"+string);
									SignatureFragment fragment=TransactionUtils.parseSignatureFragment(string);
									if(service.shardingFilter.accept(fragment)){
										service.signatureFragmentProcessor.process(fragment);
									}
								}
							} catch (Exception e) {
								logger.error(e.getMessage(),e);
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
					}
				}
			}
		}

		
}
