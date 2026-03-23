## MO-IT101 - Group 3
- Flores, Ronald John
- Lacuña, Blanche

# MotorPH Payroll Calculator

A Java-based console application designed to simulate a payroll system for MotorPH. The program processes employee data, calculates attendance-based salaries, and applies payroll rules using CSV data sources.

## Overview

The MotorPH Payroll Calculator is a simple yet structured payroll system that supports two types of users: employees and payroll staff. It demonstrates core programming concepts such as file handling, modular design, and algorithm implementation using Java.

---

## System Flow (Basic Overview)

1. The program starts and prompts the user to log in.  
2. The system validates whether the user is an **employee** or **payroll staff**.  
3. If logged in as an employee:  
   - The user enters their employee ID.  
   - The system retrieves and displays employee details.  
4. If logged in as payroll staff:  
   - The user selects whether to process payroll for one or all employees.  
   - The system reads employee and attendance data from CSV files.  
   - It calculates total hours worked (with grace period and lunch deduction).  
   - Gross pay is computed based on hours and hourly rate.  
   - Government deductions are applied.  
   - Net pay and payroll breakdown are displayed per cutoff period.  

---

## Full System Process Flow

- **System Flow Document (Google Docs / Sheets):**
- https://docs.google.com/document/d/1ys4mnnXGKF0DI-XacAXwJHHFw8MKmL4r2cn8JWtsK80/edit?usp=sharing

---

## Methods Overview

Below are the grouped methods used in the program, following the structure of the source code, with a brief description of each.

### Main Control
1. `main()` – Starts the program and routes the user based on login role.

---

### Menus
2. `employeeMenu()` – Displays options for employees to view their information.  
3. `payrollStaffMenu()` – Displays payroll staff options for processing payroll.  
4. `processPayrollMenu()` – Lets the user choose between processing one or all employees.  

---

### Core Processing
5. `processEmployees()` – Reads employee data and processes payroll for one or multiple employees.  

---

### Display
6. `displayEmployeeInfo()` – Prints basic employee details to the console.  
7. `processMonthlyPayroll()` – Computes and displays payroll details for a specific employee and month.  

---

### Login
8. `userLogin()` – Validates user credentials and determines access level.  

---

### File Handling
9. `readEmployeeDetails()` – Retrieves a specific employee’s details from the CSV file.  
10. `parseCSVLine()` – Splits a CSV line into structured data fields.  

---

### Attendance Processing
11. `readEmployeeAttendance()` – Calculates total hours worked per cutoff period from attendance records.  
12. `calculateHoursWorked()` – Computes daily worked hours using grace period and lunch break rules.  

---

### Payroll Computation
13. `calculateGrossPay()` – Computes gross pay based on total hours worked and hourly rate.  
14. `computeGrossPay()` – Alternative/combined method used to calculate gross salary before deductions.  
15. `computeNetPay()` – Calculates net salary after applying deductions.  

---

### Deduction Methods
16. `calculateSSS()` – Computes SSS contribution based on salary brackets.  
17. `calculatePHCont()` – Calculates PhilHealth contribution based on salary percentage.  
18. `calculatePagIbig()` – Computes Pag-IBIG contribution with a fixed or capped value.  
19. `calculateWithholdingTax()` – Determines withholding tax based on taxable income.  

---

### Utility
20. `getMonthName()` – Converts a numeric month into its corresponding name.  

---

## Key Features

- User authentication for employees and payroll staff  
- Payroll processing for one or all employees  
- Attendance-based salary computation  
- Grace period and lunch break implementation  
- Government deduction calculations (SSS, PhilHealth, Pag-IBIG, Tax)  
- Monthly payroll breakdown with cutoff periods  

---

## System Access (Login Credentials)

Use the following credentials to access the system:

- **Employee Access**
  - Username: `employee`
  - Password: `12345`

- **Payroll Staff Access**
  - Username: `payroll_staff`
  - Password: `12345`

---

## Project Resources

- **Project Plan (Google Sheets):**
- https://docs.google.com/spreadsheets/d/1rYYgi3egJsz5okDKd1Njb-ouiSZNyCBf_GsTfoAN15Q/edit?usp=sharing

- **QA Testing Documentation (Google Sheets):**
- https://docs.google.com/spreadsheets/d/1gFZLAr1cdCxXAOCklngtLBZsJEdRI41IZl2byToMyi8/edit?usp=sharing

---

## How to Run

1. Download source code as ZIP file by clicking the green `<code>` button  
2. Extract ZIP file into a folder, then launch your chosen IDE  
3. Select "Open Project" in your IDE and choose the extracted source code folder  
4. Run the Java program in your chosen IDE once opened  

---
