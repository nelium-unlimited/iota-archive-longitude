package io.nelium.archive.processor;

import io.nelium.archive.model.SignatureFragment;
import io.nelium.archive.repository.SignatureFragmentRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
/**
 * This class processes signature fragments. They are queued temporarily and sent to the 
 * underlying repository in batch.
 * 
 * @author pfenkam
 *
 */
@Component
public class SignatureFragmentProcessor {
	 	@Autowired
	    private SignatureFragmentRepository signatureFragmentRepository;
		@Autowired
		protected TaskExecutor taskExecutor;
	    protected Logger logger = LoggerFactory.getLogger(this.getClass());

	    private List<SignatureFragment>signatureFragmentQueue= new ArrayList<SignatureFragment>();

		public void process(SignatureFragment fragment) throws Exception {
			signatureFragmentQueue.add(fragment);
		}
		

		@PostConstruct
		public void executeAsynchronously() {
			Thread t = new Thread(() -> {
				while(true){
					try{
						List<SignatureFragment>signatureFragments= new ArrayList<SignatureFragment>();
						int end=signatureFragmentQueue.size();
						for(int i =0; i<end; i++){
							SignatureFragment transaction=signatureFragmentQueue.remove(0);
							if(transaction!=null){
								signatureFragments.add(transaction);
							}
						}
						if(signatureFragments.size()>0){
							signatureFragmentRepository.saveAll(signatureFragments);
						}
						if(signatureFragments.size()<5){
							Thread.sleep(1000);
						}
					}catch(Exception e){
						logger.warn(e.getMessage(),e);
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
