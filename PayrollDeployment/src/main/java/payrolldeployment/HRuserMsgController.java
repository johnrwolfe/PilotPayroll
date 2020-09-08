package payrolldeployment;

import io.ciera.runtime.summit.application.IApplication;
import io.ciera.runtime.summit.application.IRunContext;
import io.ciera.runtime.summit.application.tasks.GenericExecutionTask;
import io.ciera.runtime.summit.application.tasks.HaltExecutionTask;
import io.ciera.runtime.summit.classes.IModelInstance;
import io.ciera.runtime.summit.components.Component;
import io.ciera.runtime.summit.exceptions.BadArgumentException;
import io.ciera.runtime.summit.exceptions.EmptyInstanceException;
import io.ciera.runtime.summit.exceptions.XtumlException;
import io.ciera.runtime.summit.interfaces.IMessage;
import io.ciera.runtime.summit.interfaces.Message;
import io.ciera.runtime.summit.util.LOG;
import io.ciera.runtime.summit.util.impl.LOGImpl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import payrolldeployment.hruser.HRuserPayroll;             // HRuser-Payroll port
import payrolldeployment.payrollmgmt.PayrollMgmtUSER;      // CarPark-PaymentMachine port
import payrolldeployment.HRuser;                           // Shell component
import payrolldeployment.NotificationMsg;
import payrolldeployment.PayeeDataMsg;
import payrolldeployment.PayrollDataMsg;
import payrolldeployment.DataSentMsg;
import payrolldeployment.RetrievePayrollForReviewMsg;
import payrolldeployment.SubmitItemHoldMsg;
import payrolldeployment.SubmitItemApprovalMsg;
import payrolldeployment.SubmitPayrollApprovalMsg;
import payrolldeployment.SubmitToFinanceMsg;
import payrolldeployment.UpdatesSentMsg;
import payrolldeployment.AvailablePayrollsMsg;
import payrolldeployment.PayrollAvailableMsg;

// The Spring framework arranges for an instance of this class to be
// created, passing an instance of SimpMessagingTemplate as an argument,
// which enables messages to be sent to JavaScript clients.
@Controller
public class HRuserMsgController {
	private static HRuserMsgController singleton;
	
	private SimpMessagingTemplate template;  
	
	@Autowired
	public HRuserMsgController( SimpMessagingTemplate template ) {
        singleton = this;
        this.template = template;
	}
	
	public static HRuserMsgController Singleton() {
		return singleton;
	}
	
    // Begin outgoing (from this component) messages.
    // Each of the following methods is invoked when the (JavaScript) client sends 
    // a message to the corresponding message-broker topic, "/app/<messageName>".


	@MessageMapping( "/AvailablePayrolls" )
    public void AvailablePayrolls( AvailablePayrollsMsg message  ) throws Exception {
    	try {
      	  HRuser.Singleton().Payroll().AvailablePayrolls();
      	}
      	catch ( Exception e ) {
        	  System.out.printf( "Exception, %s, in AvailablePayrolls()\n", e );    			
      	}
    }

	@MessageMapping( "/RetrievePayrollForReview" )
    public void RetrievePayrollForReview( RetrievePayrollForReviewMsg message ) throws Exception {
    	try {
      	  HRuser.Singleton().Payroll().RetrievePayrollForReview( message.getDepartment(), 
      			                                                 Boolean.parseBoolean( message.getHoldsOnly() ) );
      	}
      	catch ( Exception e ) {
        	  System.out.printf( "Exception, %s, in RetrievePayrollForReview()\n", e );    			
      	}
    }
    
    @MessageMapping( "/SubmitPayrollApproval" )
    public void SubmitPayrollApproval( SubmitPayrollApprovalMsg message ) throws Exception {
    	try {
      	  HRuser.Singleton().Payroll().SubmitPayrollApproval( message.getDepartment() );
      	}
      	catch ( Exception e ) {
        	  System.out.printf( "Exception, %s, in SubmitPayrollApproval()\n", e );    			
      	}
    }

    @MessageMapping( "/SubmitToFinance" )
    public void SubmitToFinance( SubmitToFinanceMsg message ) throws Exception {
    	try {
      	  HRuser.Singleton().Payroll().SubmitToFinance( message.getDepartment() );
      	}
      	catch ( Exception e ) {
        	  System.out.printf( "Exception, %s, in SubmitToFinance()\n", e );    			
      	}
    }

    @MessageMapping( "/UpdatesSent" )
    public void UpdatesSent( UpdatesSentMsg message ) throws Exception {
    	try {
      	  HRuser.Singleton().Payroll().UpdatesSent( message.getDepartment(),
      	                                            Integer.parseInt( message.getCount() ) );
      	}
      	catch ( Exception e ) {
        	  System.out.printf( "Exception, %s, in UpdatesSent()\n", e );    			
      	}
    }
     
    @MessageMapping( "/SubmitItemHold" )
    public void SubmitItemHold( SubmitItemHoldMsg message ) throws Exception {
    	try {
      	  HRuser.Singleton().Payroll().SubmitItemHold( message.getDepartment(),
      			                                       Integer.parseInt( message.getEmployeeId() ),
      			                                       message.getPaymentLabel(),
      			                                       Boolean.parseBoolean( message.getHoldStatus() ) );
      	}
      	catch ( Exception e ) {
        	  System.out.printf( "Exception, %s, in SubmitItemHold()\n", e );    			
      	}
    }

    @MessageMapping( "/SubmitItemApproval" )
    public void SubmitItemApproval( SubmitItemApprovalMsg message ) throws Exception {
    	try {
      	  HRuser.Singleton().Payroll().SubmitItemApproval( message.getDepartment(),
      			                                           Integer.parseInt( message.getEmployeeId() ),
      			                                           message.getPaymentLabel() );
      	}
      	catch ( Exception e ) {
        	  System.out.printf( "Exception, %s, in SubmitItemApproval()\n", e );    			
      	}
    }

    // End of outgoing messages.
    
    // Incoming (to this component) messages.
    // The following methods forward incoming messages to the (JavaScript) client which
    // subscribes to a specific message-broker topic.  

    public void SendDataSentMsg( String Ident, Integer Count ) throws Exception {
    	DataSentMsg msg = new DataSentMsg( "DataSent", Ident, String.valueOf( Count ) );
        String topic = "/topic/HRuser/";
        this.template.convertAndSend( topic, msg );
    }
    
    public void SendNotificationMsg( String Ident, String Content ) throws Exception {
    	NotificationMsg msg = new NotificationMsg( "Notification", Ident, Content );
        String topic = "/topic/HRuser/";
        this.template.convertAndSend( topic, msg );
    }

    public void SendPayrollAvailableMsg( String Department ) throws Exception {
    	PayrollAvailableMsg msg = new PayrollAvailableMsg( "PayrollAvailable", Department );
        String topic = "/topic/HRuser/";
        this.template.convertAndSend( topic, msg );
    }

    public void SendPayeeDataMsg( String Department, 
    		                      Integer EmployeeId, 
    		                      String EmployeeFirstName,
    		                      String EmployeeLastName ) throws Exception {
    	PayeeDataMsg msg = new PayeeDataMsg( "PayeeData",
    			                              Department,
    			                              String.valueOf( EmployeeId ),
    			                              EmployeeFirstName,
    			                              EmployeeLastName );
        String topic = "/topic/HRuser/";
        this.template.convertAndSend( topic, msg );
    }
    
    public void SendPayrollDataMsg( Integer EmployeeId,
                                    String PaymentLabel,
    		                        Double PaymentAmount,
    		                        Boolean HoldStatus,
    		                        Boolean UnapprovalStatus ) throws Exception {
    	PayrollDataMsg msg = new PayrollDataMsg( "PayrollData",
    	                                          String.valueOf( EmployeeId ),
    			                                  PaymentLabel,
    			                                  String.valueOf( PaymentAmount ),
    			                                  String.valueOf( HoldStatus ),
    			                                  String.valueOf( UnapprovalStatus ) );
        String topic = "/topic/HRuser/";
        this.template.convertAndSend( topic, msg );
    }
    
     // End of incoming messages.
}
