package id.co.bippo.camelxmppredo;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.ProxyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static Logger logger = LoggerFactory.getLogger(App.class);
	private static LoggerInvoiceListener loggerInvoiceListener = new LoggerInvoiceListener();
	private static InvoiceListener invoiceListener;
	
	public static void main(String[] args) throws Exception {
		CamelContext camelContext = new DefaultCamelContext();
		loggerInvoiceListener = new LoggerInvoiceListener();
		final String xmppUri = "xmpp://abispulsabot@localhost/?room=abispulsa.refill&password=test";
		camelContext.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("direct:invoice").threads().marshal().json().to(xmppUri);
				from(xmppUri).threads().choice()
					.when(body().startsWith("{")).unmarshal().json().bean(loggerInvoiceListener)
					.otherwise().to("log:xmpp");
			}
		});
		camelContext.start();
		
		invoiceListener = new ProxyBuilder(camelContext).endpoint("direct:invoice").build(InvoiceListener.class);
		invoiceListener.invoiceCreated(243, "Sumba Enterprise");
		logger.info("first invoice sent");
		invoiceListener.invoiceCreated(938, "Mina Co.");
		logger.info("second invoice sent");
		invoiceListener.invoiceCreated(312, "Crux Market");
		logger.info("third invoice sent");

		try {
			while (true) { // event loop so you can send messages
				Thread.sleep(1000);
			}
		} finally {
			camelContext.stop();
		}
	}

}
