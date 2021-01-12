# PilotPayroll

An in-development pilot of a simple payroll application.
## Configurations
The following configurations exist:
1. Automated Testing.  Employs a modeled test bench, enabling:
 - Self-checking testing with Verifier
 - Ciera-generated code using `pom-testbench.xml`.
2. Browser-based Clients.  Leverages browser-based clients repesenting a user with payroll review authority.
 - Use `pom-clients.xml` for this configuration.
## Importing for Automated Testing - Ciera

1. Import the built-in external entities from the Epoch test [model](https://github.com/xtuml/models/tree/7a9fd1c3fe351b495f348a061fd10bc053991ea0/test/EpochTest).
2. Import the TestFramework project from https://github.com/amullarney/TestFramework
3. Import the following projects from this repository:
- Payroll
- AutomatedTestbench
4. Build with pom-testbench.xml.
5. Execute (bash) run-testbench.sh; test cases should run automatically.
## Importing for Browser-based Clients - Ciera
1. Import the built-in external entities from the Epoch test [model](https://github.com/xtuml/models/tree/7a9fd1c3fe351b495f348a061fd10bc053991ea0/test/EpochTest).
2. Import the following projects from this repository:
 - Payroll
 - HRuser
 - TestControl
3. Build with pom-clients.xml.
4. Execute (bash) runm-clients-servelt.sh.
5. In a browser, open two consoles: localhost:8080/TestControl.html & localhost:8080/HRuser.html; hit 'connect' in both.
6. In TestControl, Set Time to day 14, hour 11 of month 2 in 2020. In HumanResources, payroll generation will be imminent.
7. Advance time by 24 hours. Payroll will be generated. Proceed to Request available, Submit updates etc.
## Run for Automated Testing (Verifier)
1. Create a debug configuration of type "xtUML eXecute Application" and name it Payroll_test
2. Enable "Log model execution activity"
3. Disable "Run deterministically"
4. Disable "Enable simulated time"
5. Select the components in SimulationConfiguration within the AutomatedTesting project.
6. Run Verifier using this debug configuration
7. Execute function RunNominalCases in TestFunctions in AutomatedTesting - all test cases should pass.
