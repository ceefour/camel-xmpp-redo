/**
 * 
 */
package id.co.bippo.camelxmppredo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerInvoiceListener implements InvoiceListener {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	/* (non-Javadoc)
	 * @see id.co.bippo.camelxmppredo.InvoiceListener#invoiceCreated(int, java.lang.String)
	 */
	public void invoiceCreated(int id, String name) {
		logger.info("Invoice #{} name: {} created", id, name);
		// Simulate asynchronous processing by delaying
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			logger.error("Interrupted", e);
		}
		logger.info("{}/{} done!", id, name);
	}

}
