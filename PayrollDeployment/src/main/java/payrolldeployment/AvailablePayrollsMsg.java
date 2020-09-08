package payrolldeployment;

//Spring requires a POJ class for each message.
public class AvailablePayrollsMsg {
	private String payload;
	public AvailablePayrollsMsg() {
    }
	public void setPayload( String payload ) {
		this.payload = payload;
	}
	public String getPayload() {
		return payload;
	}
}