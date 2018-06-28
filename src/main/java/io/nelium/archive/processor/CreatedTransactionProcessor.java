package io.nelium.archive.processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import io.nelium.archive.model.Transaction;
import io.nelium.archive.repository.TransactionRepository;

/**
 * This class processes new transactions. Transactions are queued temporarily and sent to the 
 * underlying repository in batch.
 * 
 * @author pfenkam
 *
 */
@Component
public class CreatedTransactionProcessor {
    
    @Autowired
    private TransactionRepository transactionRepository;
	@Autowired
	protected TaskExecutor taskExecutor;

    private List<Transaction>transactionQueue= new ArrayList<Transaction>();
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public void process(Transaction transaction) throws Exception {
		transactionQueue.add(transaction);
	}
	

	@PostConstruct
	public void executeAsynchronously() {
		Thread t = new Thread(() -> {
			while(true){
				try{
					List<Transaction>transactions= new ArrayList<Transaction>();
					int end=transactionQueue.size();
					for(int i =0; i<end; i++){
						Transaction transaction=transactionQueue.remove(0);
						if(transaction!=null){
							transactions.add(transaction);
						}
					}
					if(transactions.size()>0){
						transactionRepository.saveAll(transactions);
					}
					if(transactions.size()<5){
						Thread.sleep(1000);
					}
				}catch(Exception e){
					logger.warn(e.getMessage(),e);;
					try {
						Thread.sleep(1000);
					} catch (Exception e1) {
					}
				}
			}
		});
		t.start();
	}

}
