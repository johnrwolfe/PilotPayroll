package payrolldeployment;

//Spring requires a POJ class for each message.
public class GetTimeMsg {
	private String payload;
	public GetTimeMsg( String payload) {
		this.payload = payload;
	}
    public void setPayload( String payload ) {
	    this.payload = payload;
    }
    public String getPayload() {
	    return payload;
    }
}