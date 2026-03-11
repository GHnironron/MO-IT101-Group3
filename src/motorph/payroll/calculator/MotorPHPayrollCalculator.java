package motorph.payroll.calculator;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.Duration;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author RJflores
 */

public class MotorPHPayrollCalculator {
    public static void main(String[] args) {
    
    Scanner sc = new Scanner(System.in);
    
    String user = userLogin(sc);
    if (user == null) return;
    
    if (user.equals("employee")) {
        employeeMenu(sc);
    } else if (user.equals("payroll_staff")) {
        payrollStaffMenu(sc);
    }
}
    
    public static void employeeMenu(Scanner empMenu) {
        
        System.out.println("\nWelcome, MotorPH Employee!");
        System.out.println("\n1. Enter employee ID");
        System.out.println("2. Exit");
        
        System.out.print("Pick Option: ");
        int option = empMenu.nextInt();
        empMenu.nextLine();
        
        if (option == 1) {
            System.out.print("Enter employee number: ");
            String empNum = empMenu.nextLine();
            
            String [] emp = findEmployeeID(empNum);
            
            if (emp == null) {
                System.out.println("Invalid employee number.");
                return;
            }
            
            System.out.println("\n Employee Number: " + emp[0]);
            System.out.println("Employee Name: " + emp[1] + " " + emp [2]);
            System.out.println("Birthday: " + emp[3]);
        } else {
            System.out.println("Program Terminated.");
        }
    }
   
    public static void payrollStaffMenu(Scanner staffMenu) {
        
        System.out.println("\nWelcome, MotorPH Payroll Staff!");
        System.out.println("\n1. Process Payroll");
        System.out.println("2. Exit");
        
        System.out.print("Pick Option: ");
        int option = staffMenu.nextInt();
        staffMenu.nextLine();
        
        if (option == 1) {
            processPayrollMenu(staffMenu);
        } else {
            System.out.println("Program Terminated.");
        }
    }
    
    public static String userLogin(Scanner login) {
        
        System.out.print("Enter username: ");
        String username = login.nextLine();
        
        System.out.print("Enter password: ");
        String password = login.nextLine();
        
        if ((username.equals("employee") || username.equals("payroll_staff"))
                && password.equals("12345")) {
            return username;
        }    
        
        System.out.println("Invalid username and/or password.");
        return null;
    }
    
    public static void processPayrollMenu (Scanner processPayroll) {
        
        System.out.println("\n1. One employee");
        System.out.println("2. All employees");
        System.out.println("3. Exit");
        
        System.out.print("Pick Option: ");
        int option = processPayroll.nextInt();
        processPayroll.nextLine();
        
        if (option == 1) {
            processOneEmployee(processPayroll);
        } else if (option == 2) {
            processAllEmployees(processPayroll);
        } else {
            System.out.println("Program Terminated.");
        }

    }
    
    public static void processOneEmployee(Scanner oneEmpProcess) {
        
    }
    
    public static void processAllEmployees(Scanner allEmpProcess) {
        
    }
    
    public static String[] readEmployeeDetails(String inputEmpID) {
        
        String empDetails = "src/MotorPHEmployeeData/MotorPH_EmployeeData - Employee Details.csv";
        String[] empData = new String[6];
        
        try (BufferedReader br = new BufferedReader(new FileReader(empDetails))) {   
            br.readLine();  // Skips the header
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(",");
                
                if (data[0].equals(inputEmpID)) {
                    empData[0] = data[0];   // ID
                    empData[1] = data[2];   // First Name
                    empData[2] = data[1];   // Last Name
                    empData[3] = data[3];   // Birthday
                    empData[4] = data[17];  // Gross Semi-Monthly
                    empData[5] = data[18];  // Hourly Rate
                    return empData;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employee details file.");
        }
        return null;
    }
    
    public static double[] readEmployeeAttendance(String empID, int month) {
        
        String empAttendance = "src/MotorPHEmployeeData/MotorPH_EmployeeData - Attendance Record.csv";
        double firstCutOff = 0.00;
        double secondCutOff = 0.00;
        
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
        
        try (BufferedReader br = new BufferedReader(new FileReader(empAttendance))) {
            br.readLine(); // Skips the header
            String line;
                   
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] data = line.split(",");
                if (!data[0].equals(empID)) continue;
                
                String[] dateParts = data [3].split("/");
                int recordMonth = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                
                if (year != 2024 || recordMonth != month) continue;
                
                LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
                
                double hours = calculateHoursWorked(login, logout);
                
                if (day <= 15) firstCutOff += hours;
                else secondCutOff += hours;
            }    
        }   catch (IOException e) {
            System.out.println("Error reading attendance file: " + month);
        }
    }
    
    public static String[] findEmployeeID() {
        
    }
    
    
    

// -Employee Info Presentation- //
    
    // Stores basic employee name and birthday (mm/dd/yyyy)
        String firstName = "Manuel III";
        String lastName = "Garcia";
        String birthDay = "10/11/1983";
    
    // Unique employee identifier
        String employeeID = "10001";
        
    // Combines first and last name for display
        String fullName = firstName + ", " + lastName;


        
// -Hours Worked Calculation- //
    public static double calculateHoursWorked(LocalTime timeIn, LocalTime timeOut){    
    // Create LocalTime variables for employee login and logout
        LocalTime login = LocalTime.of(8, 0);
        LocalTime logout = LocalTime.of(17, 0);
    
        if (timeIn.isBefore(login)) timeIn = login;
        if (timeIn.isAfter(logout)) return 0.0;
        
        if (timeOut.isAfter(logout)) timeOut = logout;
        if (timeOut.isBefore(login)) return 0.0;
        
    // Calculates the total duration between login and logout   
        Duration totalHours = Duration.between(timeIn, timeOut);
    
    // Converts the duration into a readable hour and minute format
        long hours = totalHours.toHours();              // full hours worked
        long minutes = totalHours.toMinutes() % 60;     // remaining minutes after hours
        return hours + (minutes / 60.0);
    }

// -Gross Pay Calculation- //

    // Hourly rate given in CSV file
        double hourlyRate = 535.71;
    
    // Converts total hours and minutes worked into decimal hours
        double totalHoursWorked = hours + (minutes/60);
    
    // Conditional statement that validates hours and rate input
        if (totalHoursWorked <= 0 || hourlyRate <= 0) {
            System.out.println ("Invalid hours worked/hourly rate input.");
        } else {
            double grossPay = totalHoursWorked * hourlyRate;    // Computes gross pay if input is valid
        
    // Displays results
        System.out.println("\n--Gross Pay Calculation--");
        System.out.println("Total Hours Worked: " + totalHoursWorked);
        System.out.println("Hourly Rate: PHP" + hourlyRate);
        System.out.println("Total Gross Pay (Cut-off): PHP" + grossPay);
        }

            
// -Net Pay Calculation- //        
        
        double monthlyBasicSalary = 25000.00;   // Sample gross pay (for now)
        double taxableIncome = 23400.00;        // Sample taxable income (for now)
        
        // Applies the first three deductions to monthly basic salary (methods for each respective calculations are called)
        double sssDeduction = calculateSSS(monthlyBasicSalary);
        double philHealthDeduction = calculatePHCont(monthlyBasicSalary);
        double pagIbigDeduction = calculatePagIbig(monthlyBasicSalary);

        // Adds up deductions before tax to calculate withholding tax
        double totalDeductionsBeforeTax = sssDeduction + philHealthDeduction + pagIbigDeduction;
        double taxableIncomeComputed = monthlyBasicSalary - totalDeductionsBeforeTax;

        // Calls the calculateWithholdingTax method to return the withholding tax value
        double withholdingTax = calculateWithholdingTax(taxableIncomeComputed);
       
        // Sums up all deductions and subtracts it on gross pay (or basic salary for now)
        double totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + withholdingTax;
        double netPay = monthlyBasicSalary - totalDeductions;
    
    // Method that calculates SSS deductions    
    public static double calculateSSS(double monthlyBasicSalary) {

    // List all SSS compensation ranges (upper limits)
        double[] compRange = {
        3250, 3750, 4250, 4750, 5250, 5750, 6250, 6750,
        7250, 7750, 8250, 8750, 9250, 9750, 10250, 10750,
        11250, 11750, 12250, 12750, 13250, 13750, 14250, 14750,
        15250, 15750, 16250, 16750, 17250, 17750, 18250, 18750,
        19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750,
        23250, 23750, 24250, 24750          
        };
        
    // List all corresponding SSS contributions for the ranges
        double[] contribSSS = {
        135.00, 157.50, 180.00, 202.50, 225.00, 247.50, 270.00, 292.50,
        315.00, 337.50, 360.00, 382.50, 405.00, 427.50, 450.00, 472.50,
        495.00, 517.50, 540.00, 562.50, 585.00, 607.50, 630.00, 652.50,
        675.00, 697.50, 720.00, 742.50, 765.00, 787.50, 810.00, 832.50,
        855.00, 877.50, 900.00, 922.50, 945.00, 967.50, 990.00, 1012.50,
        1035.00, 1057.50, 1080.00, 1102.50            
        };
    
    // Use a for loop statement to determine and return the correct SSS contribution
        for (int i = 0; i < compRange.length; i++) {
            if (monthlyBasicSalary < compRange[i]) {
                return contribSSS[i];
            }
        }

    // Returns 1125 if salary is above 24750 
        return 1125.00;
  }
    
    // Method that calculates PhilHealth contribution
    public static double calculatePHCont(double monthlyBasicSalary) {
       
    // Multiplies basic salary to premium rate (3%)
       double totalPremium = monthlyBasicSalary * 0.03;
       
    // Conditional statements according to salary requirements   
       if (monthlyBasicSalary <= 10000.00) {
           totalPremium = 300.00;
       } else if (monthlyBasicSalary >= 60000.00) {
           totalPremium = 1800.00;
       }
    
    // Deducts the original premium share to employee share (50%)   
       double empShare = totalPremium / 2.0;
    // Returns the value of the finished PhilHealth calculation   
       return empShare;
   }
    
    // Method that calculates Pag-IBIG deductions
    public static double calculatePagIbig(double monthlyBasicSalary) {
        
        // Add Pag-IBIG rate variable for calculation
        double pagIbigRate;
        
        // Conditional statements according to Pag-IBIG mandated deductions
        if (monthlyBasicSalary >= 1000.00 && monthlyBasicSalary <= 1500.00) {
            pagIbigRate = 0.01;
        } else if (monthlyBasicSalary > 1500.00) {
            pagIbigRate = 0.02;
        } else {
            pagIbigRate = 0.00;   // No salary, no contribution
        }
        
        double pagIbigCont = monthlyBasicSalary * pagIbigRate;
        
        // Maximum Pag-IBIG contribution amount as per reference is 100.
        if (pagIbigCont > 100.00) {
            pagIbigCont = 100.00;
        }
        // Returns the value of the calculated Pag-IBIG deduction
        return pagIbigCont;
    }
    
    // Method that calculates Withholding Tax
    public static double calculateWithholdingTax(double taxableIncome) {
        
        // Add withholding tax variable for calculation
        double withTax;
        
        // Conditional statements according to withholding tax rates
        if (taxableIncome <= 20832.00) {
            withTax = 0.0;
        } else if (taxableIncome < 33333.00) {
            withTax = (taxableIncome - 20833.00) * 0.20;
        } else if (taxableIncome < 66667.00) {
            withTax = 2500 + (taxableIncome - 33333.00) * 0.25;
        } else if (taxableIncome < 166667.00) {
            withTax = 10833 + (taxableIncome - 66667.00) * 0.30;
        } else if (taxableIncome < 666667.00) {
            withTax = 40833.33 + (taxableIncome - 166667.00) * 0.32;
        } else {
            withTax = 200833.33 + (taxableIncome - 666667.00) * 0.35;
        }
        
        // Returns the value of calculated withholding tax
        return withTax;
    }
}

    