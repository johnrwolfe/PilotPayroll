package automatedtestbench;


import io.ciera.runtime.summit.application.ApplicationExecutor;
import io.ciera.runtime.summit.application.IApplication;
import io.ciera.runtime.summit.application.ILogger;
import io.ciera.runtime.summit.application.tasks.GenericExecutionTask;
import io.ciera.runtime.summit.application.tasks.HaltExecutionTask;
import io.ciera.runtime.summit.components.IComponent;
import io.ciera.runtime.summit.exceptions.XtumlException;

import java.util.Arrays;

import simulationconfiguration.PayrollMgmt;
import simulationconfiguration.PayrollTestbench;
import simulationconfiguration.TestSequencer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutomatedTestbenchApplication implements IApplication {

    private IComponent<?>[] components;
    private ApplicationExecutor[] executors;
    private static AutomatedTestbenchApplication singleton;

    public AutomatedTestbenchApplication() {
    	singleton = this;
        components = new IComponent<?>[3];
        executors = new ApplicationExecutor[1];
        setup( null, null );
        initialize();
    }

    @Override
    public void run() {
        for (ApplicationExecutor executor : executors) {
            executor.run();
        }
    }

    @Override
    public void setup( String[] args, ILogger logger ) {
        for ( int i = 0; i < executors.length; i++ ) {
            if ( null != logger ) {
                executors[i] = new ApplicationExecutor( "AutomatedTestbenchApplicationExecutor" + i, args, logger );
            }
            else {
                executors[i] = new ApplicationExecutor( "AutomatedTestbenchApplicationExecutor" + i, args );
            }
        }
        components[2] = new TestSequencer(this, executors[0], 2);
        components[0] = new PayrollMgmt(this, executors[0], 0);
        components[1] = new PayrollTestbench(this, executors[0], 1);
        ((PayrollTestbench)components[1]).APPTESTSUPPORT().satisfy(((PayrollMgmt)components[0]).TEST());
        ((PayrollMgmt)components[0]).TEST().satisfy(((PayrollTestbench)components[1]).APPTESTSUPPORT());
        ((PayrollTestbench)components[1]).TESTAPP().satisfy(((PayrollMgmt)components[0]).USER());
        ((PayrollMgmt)components[0]).USER().satisfy(((PayrollTestbench)components[1]).TESTAPP());
        ((PayrollTestbench)components[1]).TESTSEQ().satisfy(((TestSequencer)components[2]).TESTBENCH());
        ((TestSequencer)components[2]).TESTBENCH().satisfy(((PayrollTestbench)components[1]).TESTSEQ());
    }

    public TestSequencer TestSequencer() {
        return (TestSequencer)components[2];
    }
    public PayrollMgmt PayrollMgmt() {
        return (PayrollMgmt)components[0];
    }
    public PayrollTestbench PayrollTestbench() {
        return (PayrollTestbench)components[1];
    }

    @Override
    public void initialize() {
        for ( IComponent<?> component : components ) {
            component.getRunContext().execute( new GenericExecutionTask() {
                @Override
                public void run() throws XtumlException {
                    component.initialize();
                }
            });
        }
    }

    @Override
    public void start() {
        if (1 == executors.length) {
            executors[0].run();
        }
        else {
            for ( ApplicationExecutor executor : executors ) {
                executor.start();
            }
        }
    }

    @Override
    public void printVersions() {
        io.ciera.runtime.Version.printVersion();
        for ( IComponent<?> c : components ) {
            System.out.printf("%s: %s (%s)", c.getClass().getName(), c.getVersion(), c.getVersionDate());
            System.out.println();
        }
    }

    @Override
    public void stop() {
        for ( ApplicationExecutor executor : executors ) {
            executor.execute(new HaltExecutionTask());
        }
    }

    public static void main( String[] args ) {
        SpringApplication.run( AutomatedTestbenchApplication.class, args );
        singleton.start();
    }

}
