package payrolldeployment;

//Spring requires a POJ class for each message.
public class AvailablePayrollsMsg {
	private String payload;
	public AvailablePayrollsMsg() {
    }
	public AvailablePayrollsMsg( String payload ) {
		this.payload = payload;
    }
	public void setPayload( String payload ) {
		this.payload = payload;
	}
	public String getPayload() {
		return payload;
	}
}