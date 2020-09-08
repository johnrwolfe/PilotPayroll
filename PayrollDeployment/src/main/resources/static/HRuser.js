var stompClient = null;

var vm = new Vue({
	el: '#main-content',
	data: {
	    AvailableDisabled: true,
	    PayrollRequestDisabled: true,
	    CancelDisabled: true,
	    UpdateDisabled: true,
	    ApproveDisabled: true,
	    SubmitDisabled: true,
		department: "",
	    notification: ""
	}
})

function initialize() {
        vm.AvailableDisabled = true;
	    vm.PayrollRequestDisabled = true;
	    vm.CancelDisabled = true;
	    vm.UpdateDisabled = true;
	    vm.ApproveDisabled = true;
	    vm.SubmitDisabled = true;
		vm.department = "";
	    vm.notification = "";
}


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        $("#payrollentries").hide();
    }
    else {
        $("#conversation").hide();
        initialize();
    }
    $("#replies").html("");
    $("#entries").html("");
}

// When connecting, subscribe to a topic to receive
// messages sent from the server.
function connect() {
    var socket = new SockJS('/PilotPayroll-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/HRuser/', function (message) {
            showReply( message );
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

// Client-to-server messages.

function sendAvailablePayrolls() {
    stompClient.send( "/app/AvailablePayrolls", {} );
}
function sendRetrievePayrollForReview() {
    stompClient.send( "/app/RetrievePayrollForReview", {}, 
      JSON.stringify( {'department': $("#department").val()} ) );
}

function sendSubmitPayrollApproval() {
    stompClient.send( "/app/SubmitPayrollForApproval", {}, 
      JSON.stringify( {'department': $("#department").val()} ) );
      vm.ApproveDisabled = true;
      vm.SubmitDisabled = false;
      $("#payrollentries").hide();
}

function sendUpdates() {
    // send a SubmitItemHold message for any changed hold status
    // then indicate all updates sent
    stompClient.send("/app/UpdatesSent", {}, 
      JSON.stringify( {'department': $("#department").val()} ) );
}

function sendSubmitToFinance() {
    stompClient.send("/app/SubmitToFinance", {}, 
      JSON.stringify( {'department': $("#department").val()} ) );
      vm.SubmitDisabled = true;
}

// Support functions for incoming message handling

function availablePayroll ( message ) {
    dept = JSON.parse( message.body ).department;
    $("#payrolls").append("<tr><td>" + dept + "</td></tr>");
}

function payeeDataMsg( message ) {
    // accept a payee element data message 
    name = JSON.parse( message.body ).employeeFirstName + " " + JSON.parse( message.body ).employeeLastName;
    $("#entries").append("<tr><td>" + name + "</td></tr>");
}

function payrollDataMsg( message ) {
    // accept a payroll element data message - just display it, for now
    entry = JSON.parse( message.body ).paymentLabel + " " + JSON.parse( message.body ).paymentAmount;
    $("#entries").append("<tr><td>" + entry + "</td></tr>");
}

// Display a message received from the server.

var msgs = { 'imminent': "Payroll generation imminent for department ",
		     'generating': "Draft payroll being generated for department ",
		     'generated': "Draft payroll has been generated for department ",
		     'reviewed': "Payroll has been reviewed for department ",
             'overdue': "Payroll submission overdue for department " };

function showReply( message ) {
    $("#replies").append("<tr><td>" + message + "</td></tr>");
    var messageName = JSON.parse( message.body ).messageName;
    if ( messageName == "Notification" ) {
        var msgident = JSON.parse( message.body ).ident;
        var content = JSON.parse( message.body ).content;
        vm.notification = msgs[ msgident ] + content;
        if ( msgident == "generated" ) {
            vm.AvailableDisabled = false;
         }
    } else if ( messageName == "PayrollAvailable" ) {
           vm.PayrollDisabled = false;
           payrollAvailable( message );
    } else if ( messageName == "PayeeData" ) {
           vm.PayrollDisabled = true;
           payeeDataMsg( message );
    } else if ( messageName == "PayrollData" ) {
           vm.PayrollDisabled = true;
           payrollDataMsg( message );
    } else if ( messageName == "DataSent" ) {
           vm.UpdateDisabled = false;
           vm.ApproveDisabled = false;
        $("#payrollentries").show();
    }
}

// Map buttons to functions.
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#available" ).click(function() { sendAvailablePayrolls(); });
    $( "#payroll" ).click(function() { sendRetrievePayrollForReview(); });
    $( "#cancel" ).click(function() { cancel(); });
    $( "#update" ).click(function() { sendUpdates(); });
    $( "#approve" ).click(function() { sendSubmitPayrollApproval(); });
    $( "#submit" ).click(function() { sendSubmitToFinance(); });
});