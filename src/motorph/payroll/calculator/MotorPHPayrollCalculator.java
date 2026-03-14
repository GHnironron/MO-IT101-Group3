package motorph.payroll.calculator;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.Duration;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *MotorPH Payroll Calculator
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


// == MENUS & LOGIN SCREEN == //

    
// -Employee Menu- //    
    public static void employeeMenu(Scanner empMenu) {
    
        // Displays employee menu and allows employees to prompt an option
        System.out.println("\nWelcome, MotorPH Employee!");
        System.out.println("\n1. Enter employee ID");
        System.out.println("2. Exit");
        
        // Reads the user prompt with scanner class
        System.out.print("Pick Option: ");
        int option = empMenu.nextInt();
        empMenu.nextLine();
        
        // Conditional statement that prompts the user to enter their employee ID
        if (option == 1) {
            System.out.print("Enter employee number(10001-10034): ");
            String empNum = empMenu.nextLine();
        
        // Calls the readEmployeeDetails method to display specific employee ID's basic info
            String [] emp = readEmployeeDetails(empNum);
            
            if (emp == null) {
                System.out.println("Invalid employee number.");
                return;
            }
        
        // Displays employee basic info by reading the CSV file's respective index    
            System.out.println("\n==============================");
            System.out.println("Employee Number: " + emp[0]);
            System.out.println("Employee Name: " + emp[1] + " " + emp [2]);
            System.out.println("Birthday: " + emp[3]);
            System.out.println("==============================");
        } else {
            System.out.println("Program Terminated.");
        }
    }

// -Payroll Staff Menu- //   
    public static void payrollStaffMenu(Scanner staffMenu) {
        
        // Displays payroll staff menu and allows staff to prompt an option
        System.out.println("\nWelcome, MotorPH Payroll Staff!");
        System.out.println("\n1. Process Payroll");
        System.out.println("2. Exit");
        
        // Reads the user prompt with scanner class
        System.out.print("Pick Option: ");
        int option = staffMenu.nextInt();
        staffMenu.nextLine();
        
        // Calls the processPayrollMenu method if option 1 is picked, otherwise it terminates the program.
        if (option == 1) {
            processPayrollMenu(staffMenu);
        } else {
            System.out.println("Program Terminated.");
        }
    }

// -Login Screen- //    
    public static String userLogin(Scanner login) {
        
        // Displays the login screen, prompting the user to login as employee or payroll staff.
        System.out.println("Welcome to MotorPH Portal!");
        System.out.print("Enter username (employee/payroll_staff): ");
        String username = login.nextLine();
        
        // Prompts and scans the user to enter a password ("12345" in this case).
        System.out.print("Enter password: ");
        String password = login.nextLine();
        
        // Conditional statements that classifies the user logged in as an employee or staff (and also to determine which menu to display).
        if ((username.equals("employee") || username.equals("payroll_staff"))
                && password.equals("12345")) {
            return username;
        }    
        
        // Prints invalid if user enters incorrect login credentials.
        System.out.println("Invalid username and/or password.");
        return null;
    }

// -Process Payroll Sub-menu (From Payroll Staff Menu)- //    
    public static void processPayrollMenu (Scanner processPayroll) {
        
        // Displays options when processing payroll (One/All employees)
        System.out.println("\n1. One employee");
        System.out.println("2. All employees");
        System.out.println("3. Exit");
        
        // Use of scanner class to indicate user prompt
        System.out.print("Pick Option: ");
        int option = processPayroll.nextInt();
        processPayroll.nextLine();
        
        // Conditional statements that determine which payroll process method should be called as per user input
        if (option == 1) {
            processOneEmployee(processPayroll);
        } else if (option == 2) {
            processAllEmployees(processPayroll);
        } else {
            System.out.println("Program Terminated.");
        }

    }


// == PROCESS & DISPLAY ONE/ALL EMPLOYEE DETAILS == //

    
// -Individual- //  
    public static void processOneEmployee(Scanner oneEmpProcess) {
        
        // Asks for specific employee ID input
        System.out.print("Enter Employee #: ");
        String empNo = oneEmpProcess.nextLine();
        
        // Loads employee details from the CSV (returns null/invalid if not found)
        String[] emp = readEmployeeDetails(empNo);
        if (emp == null) {
            System.out.println("Invalid Employee #.");
            return;
        }
        
        // Cleans and extracts the hourly rate of specific employee
        double rate = Double.parseDouble(emp[5].replace(",", "").replace("\"", "").trim());
        
        // Displays employee information
        System.out.println("\n===================================");
        System.out.println("Employee #   : " + emp[0]);
        System.out.println("Employee Name: " + emp[2] + ", " + emp[1]);
        System.out.println("Birthday     : " + emp[3]);
        System.out.println("======================================");
        
        // Loops through each month (from June to December 2024) to calculate payroll.
        for(int month = 6; month <= 12; month++) {
            
            // Calculates gross pay for the first and second cutoffs.
            double firstGross = computeGrossPay(empNo, month, true, rate);
            double secondGross = computeGrossPay(empNo, month, false, rate);
            
            // Calls the computeNetPay method to apply deductions.
            double[] result = computeNetPay(firstGross, secondGross);
            
            // Result array to extract all deduction values.
            double netPay = result[0];
            double totalDeductions = result[1];
            double sss = result[2];
            double philHealth = result[3];
            double pagIbig = result[4];
            double withHoldingTax = result[5];
            
            // Converts month number to name (calls the getMonthName method)
            String monthName = getMonthName(month);
            int days = YearMonth.of(2024, month).lengthOfMonth();
        
            // Displays payroll information per cutoff period.
            System.out.println("Cutoff Date: " + monthName + " 1 to 15");
            System.out.println("Gross Salary: " + firstGross);

            System.out.println("\nCutoff Date: " + monthName + " 16 to " + days);
            System.out.println("Gross Salary: " + secondGross);
            System.out.println("Deductions:");
            System.out.println("  SSS       : " + sss);
            System.out.println("  PhilHealth: " + philHealth);
            System.out.println("  Pag-IBIG  : " + pagIbig);
            System.out.println("  Tax       : " + withHoldingTax);
            System.out.println("Total Deduction: " + totalDeductions);
            System.out.println("Net Salary     : " + netPay);
            
            System.out.println("------------------------------");
        }
    }

// -All- //    
    public static void processAllEmployees(Scanner allEmpProcess) {
        
        String empDetails = "src/MotorPHEmployeeData/MotorPH_EmployeeData - Employee Details.csv";        
        try (BufferedReader br = new BufferedReader(new FileReader(empDetails))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                // Uses the parseCSVLine method to handle quoted fields (e.g., "90,000" being split into two columns because of line.split(","))
                String[] data = parseCSVLine(line);
                
                // Validates that there are no more than 19 columns in the CSV file (0-18)
                if (data.length < 19) continue;                
                
                // Extracts employee info from respective CSV columns
                String empNo = data[0];
                String firstName = data[2];
                String lastName = data[1];
                String birthDay = data[3];
                
                // Parses the hourly rate into a double for pay calculations (also removes commas and slashes).
                double rate = Double.parseDouble(
                    data[18].replace(",", "").replace("\"", "").trim()
                );
            
            // Displays employee information
            System.out.println("===================================");
            System.out.println("Employee #   : " + empNo);
            System.out.println("Employee Name: " + lastName + ", " + firstName);
            System.out.println("Birthday     : " + birthDay);
            System.out.println("===================================");

            // Loops through each month (from June to December 2024) to calculate payroll.
            for (int month = 6; month <= 12; month++) {
                    
                // Calculates gross pay for the first and second cutoffs.
                double firstGross = computeGrossPay(empNo, month, true, rate);
                double secondGross = computeGrossPay(empNo, month, false, rate);

                // Calls the computeNetPay method to apply deductions.
                double[] result = computeNetPay(firstGross, secondGross);

                // Result array to extract all deduction values.
                double netPay = result[0];
                double totalDeductions = result[1];
                double sss = result[2];
                double philHealth = result[3];
                double pagIbig = result[4];
                double withHoldingTax = result[5];

                // Converts month number to name (calls the getMonthName method)
                String monthName = getMonthName(month);
                int days = YearMonth.of(2024, month).lengthOfMonth();

                // Displays payroll information per cutoff period.
                System.out.println("Cutoff Date: " + monthName + " 1 to 15");
                System.out.println("Gross Salary: " + firstGross);

                System.out.println("\nCutoff Date: " + monthName + " 16 to " + days);
                System.out.println("Gross Salary: " + secondGross);
                System.out.println("Deductions:");
                System.out.println("  SSS       : " + sss);
                System.out.println("  PhilHealth: " + philHealth);
                System.out.println("  Pag-IBIG  : " + pagIbig);
                System.out.println("  Tax       : " + withHoldingTax);
                System.out.println("Total Deduction: " + totalDeductions);
                System.out.println("Net Salary     : " + netPay);
                
                System.out.println("------------------------------");
            }
        }
    } catch (Exception e) {
        System.out.println("Error processing all employees.");
    }
} 
    

// -Misc: Converts month number to name- //
    public static String getMonthName(int monthName) {
        
        // Switch statements that formats month numbers into names (June-December).
        return switch (monthName) {
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "Month " + monthName;
        };
    }
    
    
// == READ CSV FILES == //

    
// -Reads Employee Details- //    
    public static String[] readEmployeeDetails(String inputEmpID) {
        
        // Initiates the employee details csv as a variable for more readable code.
        String empDetails = "src/MotorPHEmployeeData/MotorPH_EmployeeData - Employee Details.csv";
        String[] empData = new String[6];
        
        // Reads employee details CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(empDetails))) {   
            br.readLine();  // Skips the header
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
       
                // Calls the parceCSVLine method to prevent column splits from commas
                String[] data = parseCSVLine(line);
                if (data.length < 19) continue;
                
            /** DEBUG: Print first employee found to see actual columns
            if (data[0].equals("10001")) {
                System.out.println("DEBUG - Employee 10001 raw data:");
                for (int i = 0; i < data.length; i++) {
                    System.out.println("  [" + i + "] = " + data[i]);
                }
            }
            **/    
                if (data[0].equals(inputEmpID)) {
                    empData[0] = data[0];   // Employee ID
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

// -Reads Employee Attendance- //
    public static double[] readEmployeeAttendance(String empID, int month) {
        
        // Initiates employee attendance csv as variable for more readable code.
        String empAttendance = "src/MotorPHEmployeeData/MotorPH_EmployeeData - Attendance Record.csv";
        double firstCutOff = 0.00;
        double secondCutOff = 0.00;
        
        // Formats the attendance as hours:minutes for hours worked calculation purposes.
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
        
        // Reads the attendance CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(empAttendance))) {
            br.readLine(); // Skips the header
            String line;

        while ((line = br.readLine()) != null) {
            // Skip empty lines
            if (line.trim().isEmpty()) continue;

            String[] data = line.split(",");

            // Validates the CSV columns
            if (data.length < 6) continue;

            // Trim the employee ID and compare
            if (!data[0].trim().equals(empID.trim())) continue;

            try {
                // Parse the date from column 4 (M/D/YYYY format)
                String[] dateParts = data[3].trim().split("/");
                
                if (dateParts.length != 3) continue;
                
                int recordMonth = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                // Only process records for the specified month and year
                if (year != 2024 || recordMonth != month) continue;

                // Parse login and logout times
                LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);

                // Calls the hours worked calculation method
                double hours = calculateHoursWorked(login, logout);

                // Add to the appropriate cutoff period
                if (day <= 15) {
                    firstCutOff += hours;
                } else {
                    secondCutOff += hours;
                }
                
            } catch (Exception e) {
                // Skip records with parsing errors silently
                continue;
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading attendance file.");
    }
    
    return new double[] {firstCutOff, secondCutOff};
    }

// -Parse CSV Line with Quoted Fields- //
    public static String[] parseCSVLine(String line) {
    
        
        List<String> result = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    // Check for escaped quote (two consecutive quotes)
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        field.append('"');
                        i++; // Skip next quote
                    } else {
                        // End of quoted field
                        inQuotes = false;
                    }
                } else {
                    field.append(c);
                }
            } else {
                if (c == ',') {
                    // Found delimiter - add field to result
                    result.add(field.toString().trim());
                    field = new StringBuilder();
                } else if (c == '"') {
                    // Start of quoted field
                    inQuotes = true;
                } else if (c != ' ' || field.length() > 0) {
                    // Add character (skip leading spaces)
                    field.append(c);
                }
            }
        }
    // Add last field
    result.add(field.toString().trim());
    
    return result.toArray(new String[0]);
}
    
    
// == HOURS, GROSS, & NET PAY CALCULATION == //
        
    
// -Hours Worked Calculation- //
    public static double calculateHoursWorked(LocalTime timeIn, LocalTime timeOut){    
    // Create LocalTime variables for employee login and logout (applies 8AM-5PM rule)
        LocalTime login = LocalTime.of(8, 0);
        LocalTime logout = LocalTime.of(17, 0);
    
    // If logged in before 8 AM, login is 8 AM by default
        if (timeIn.isBefore(login)) timeIn = login;
    
    // If logged in after 5 PM, no hours credited.  
        if (timeIn.isAfter(logout)) return 0.0;
    
    // If logged out after 5 PM, logout is 5 PM by default    
        if (timeOut.isAfter(logout)) timeOut = logout;
    
    // If logged out before 8 AM, no hours credited.   
        if (timeOut.isBefore(login)) return 0.0;
        
    // Calculates the total duration between login and logout   
        Duration totalHours = Duration.between(timeIn, timeOut);
    
    // Converts the duration into a readable hour and minute format
        long hours = totalHours.toHours();              // full hours worked
        long minutes = totalHours.toMinutes() % 60;     // remaining minutes after hours
        return hours + (minutes / 60.0);
    }

// -Gross Pay Calculation- //
    public static double calculateGrossPay(double hours, double rate) {
    
    // Gross Pay according to hours worked
        return hours * rate;        
    } 
    public static double computeGrossPay(String empNo, int month, boolean firstCutOff, double rate) {
    
    // Reads attendance hours for selected cutoffs
        double [] cutOffs = readEmployeeAttendance(empNo, month);
        
        double hours;
        if (firstCutOff) {
            hours = cutOffs[0];     // Total Hours for 1-15 cutoff
        } else {
            hours = cutOffs[1];     // Total Hours for 16-end of cutoff
        }
        
        if (hours > 0) {
            return calculateGrossPay(hours, rate);
        }
        
        return 0.0;
    }

// -Net Pay Calculation- //        
    public static double[] computeNetPay(double firstGross, double secondGross) {
        double combinedGross = firstGross + secondGross;
        
        // Ensures minimum gross pay
        if (combinedGross <= 0) {
            return new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        }
        
        // Applies the first three deductions to monthly basic salary (methods for each respective calculations are called)
        double sss = calculateSSS(combinedGross);
        double philHealth = calculatePHCont(combinedGross);
        double pagIbig = calculatePagIbig(combinedGross);

        // Adds up deductions before tax to calculate withholding tax
        double totalDeductionsBeforeTax = sss + philHealth + pagIbig;
        double taxableIncomeComputed = combinedGross - totalDeductionsBeforeTax;

        // Calls the calculateWithholdingTax method to return the withholding tax value
        double withholdingTax = calculateWithholdingTax(taxableIncomeComputed);
       
        // Sums up all deductions and subtracts it on gross pay (2nd cutoff total)
        double totalDeductions = sss + philHealth + pagIbig + withholdingTax;
        double netPay = combinedGross - totalDeductions;
        
        if (netPay < 0) {
            System.out.println("Invalid Net Pay Calculation Detected.");
        }
        
        // Returns the values of deductions along with net pay for display.
        return new double[] {netPay, totalDeductions, sss, philHealth, pagIbig, withholdingTax};
    }    

    
// == GOVERNMENT DEDUCTIONS == //   
    
    
// -SSS Deduction- //  
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
    
// -PhilHealth Contribution- //
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
    
// -PagIbig Deduction- //
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
    
// -Withholding Tax Deduction- //
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



    