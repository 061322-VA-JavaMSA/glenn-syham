package com.revature.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.revature.models.Payment;
import com.revature.models.PaymentConn;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.PaymentConService;
import com.revature.services.UserService;

public class ManagerController {
	private Scanner scan;
	private UserService uservice;
	private PaymentConService pcs;
	private PaymentController paycontrol;	
	
	public ManagerController() {
		uservice = new UserService();
		pcs = new PaymentConService();
		paycontrol = new PaymentController();
	}

	public void managerStartMenu() {
		scan = new Scanner(System.in);
		PaymentController payc = new PaymentController();
		int choice = 0;
		int done = 0;

		while (done == 0) {
			System.out.println("Main Menu:");
			System.out.println("1 = Employee");
			System.out.println("2 = Sales history of all offers");
			System.out.println("3 = exit");

			choice = scan.nextInt();
			System.out.print("\033[H\033[2J");
			System.out.flush();
			switch (choice) {
			case 1:
				managerEmployeeMenu();
				break;
			case 2:
				paycontrol.allByEmplyee();
				break;
			case 4:
				done = 1;
				System.out.println("Bye");
				System.exit(0);
				break;
			default:
				System.err.println("Please try again");
				break;
			}

		}
	}

	public void managerEmployeeMenu() {
		scan = new Scanner(System.in);
		PaymentController payc = new PaymentController();
		int choice = 0;
		int done = 0;

		while (done == 0) {
			System.out.println("Employee Menu:");
			System.out.println("1 = List");
			System.out.println("2 = Fire Employee");
			System.out.println("3 = exit");

			choice = scan.nextInt();
			System.out.print("\033[H\033[2J");
			System.out.flush();
			switch (choice) {
			case 1:
				ListEmployee();
				break;
			case 2:
				FireEmployee();
				break;
			case 3:
				done = 1;
				managerStartMenu();
				break;
			default:
				System.err.println("Please try again");
				break;
			}

		}
	}

	public void ListEmployee() {

		List<User> employees = new ArrayList<>();
		employees = uservice.retrieveUsersByRoleid(1);

		for (User employee : employees) {
			list(employee);
		}
	}

	public void FireEmployee() {
		System.out.println("Fire employee");
		System.out.println("Ener employee id:");
		int id = scan.nextInt();
		User employee = uservice.retrieveUserById(id);
		if (employee == null || employee.getRole_id() != 1) {
			System.out.println("Cannot find Employee");
		} else {
			list(employee);
			System.out.println("Do you want to fire this employee Y/N");
			String choice = scan.next();
			switch (choice) {
			case "y":
			case "Y":
				uservice.updateUserStatus(id, 2);
				System.out.println("employee has been fired");

				break;
			case "n":
			case "N":
			default:
				System.out.println("employee has been not fired");
				break;

			}
		}
	}

	public void list(User employee) {
		String status = (employee.getStatus() == 1) ? "Employed" : "Fired";

		System.out.println("ID: " + employee.getId() + " | Name: " + employee.getUsername() + " | Status: " + status);
	}
	
 
}
