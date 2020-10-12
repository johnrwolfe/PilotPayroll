# PilotPayroll

An in-development pilot of a simple payroll application.
## Configurations
The following configurations exist:
1. Automated Testing.  Employs a modeled test bench, enabling:
 - Self-checking testing with Verifier
 - Ciera-generated code using `pom-testbench.xml`.
2. Browser-based Clients.  Leverages browser-based clients repesenting a user with payroll review authority and a  Use `pom-clients.xml` for this configuration.
## Importing for Automated Testing 

1. Import the built-in external entities from the Epoch test [model](https://github.com/xtuml/models/tree/7a9fd1c3fe351b495f348a061fd10bc053991ea0/test/EpochTest).
2. Import the TestFramework project from https://github.com/amullarney/TestFramework
3. Import the following projects from this repository:
- Payroll
- AutomatedTestbench
## Importing for Browser-based Clients
1. Import the built-in external entities from the Epoch test [model](https://github.com/xtuml/models/tree/7a9fd1c3fe351b495f348a061fd10bc053991ea0/test/EpochTest).
2. Import the following projects from this repository:
 - Payroll
 - HRuser
 - TestContrrol
3. Import the Ciera runtime project (ref. https://github.com/xtuml/ciera/wiki/Jump_Start)
  < work in progress >
## Run for Automated Testing (Verifier)
1. Create a debug configuration of type "xtUML eXecute Application" and name it Payroll_test
2. Enable "Log model execution activity"
3. Disable "Run deterministically"
4. Disable "Enable simulated time"
5. Select the SimulationConfiguration within the AutomatedTesting project
6. Run Verifier using this debug configuration
7. Execute InitializeAndRun function in TestFunctions in AutomatedTesting
