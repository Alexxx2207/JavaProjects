package com.company;

import com.company.Models.Person;
import com.company.Models.Phone;

import java.lang.reflect.Type;
import java.sql.*;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Scanner;

import static com.company.GlobalConstants.*;


public class Engine implements IEngine {


	private final String url;
	private String user;
	private String password;
	private Connection connection;



	private int actionChoice;
	
	Scanner scanner;
	
	public Engine() {
		
		url = "jdbc:mysql://localhost:3306/prsn";
		user = "";
		password = "";
		connection = null;

		actionChoice = 0;
		
		scanner = new Scanner(System.in);


	}
	
	@Override
	public void Run() throws SQLException {
		System.out.println("Welcome!!!");
		
		while(true)
		{
			showLoggingMenu();

			System.out.println("Logged In!");
			while(true)
			{
				showMainMenu();

				if((user.equals("admin") && actionChoice == 5) || (user.equals("reader")) && actionChoice == 2)
				{
					System.out.println("\nLogged out!\n");
					break;
				}

				if(user.equals("admin"))
				{
					handleAdminChoice();
				}
				else
				{
					handleReaderChoice();
				}
			}
		}
		
	}


	@Override
	public void showLoggingMenu() {
		
		do
		{
			System.out.print("\nChoose a role - Admin or Reader - (Case insensitive): ");
			user = scanner.nextLine().toLowerCase();

		}while(!user.equals("admin") && !user.equals("reader"));
		

		while(true)
		{
			System.out.print("Password: ");
			password = scanner.nextLine();

			if(validateCredentials())
			{
				break;
			}
			System.out.println("Invalid Credentials!");
		}
	}

	@Override
	public void showMainMenu() {

		String choice = "";

		while(true)
		{
			System.out.println("Choose action: ");

			if (user.equals("admin"))
			{
				System.out.println("1) Get data about people");
				System.out.println("2) Insert data about new person");
				System.out.println("3) Update data about a person");
				System.out.println("4) Delete data about a person");
				System.out.println("5) Exit");
			}
			else if (user.equals("reader"))
			{
				System.out.println("1) Get data about people");
				System.out.println("2) Exit");
			}

			System.out.print("Choice(insert the number in front of your choice): ");

			choice = scanner.nextLine();

			if(!tryParseInt(choice))
			{
				System.out.println("\nInvalid input!\n");
				continue;
			}

			actionChoice = Integer.parseInt(choice);

			if(actionChoice >= 1 && actionChoice <= 5 && user.equals("admin") ||
				actionChoice >= 1 && actionChoice <= 2 && user.equals("reader"))
			{
				break;
			}
			System.out.println("\nInvalid choice!\n");
		}
	}

	@Override
	public boolean validateCredentials(){
		try {
			connection = DriverManager.getConnection(url, user, password);

		} catch (SQLException e) {
			return false;
		}
		
		return true;
	}

	private void handleAdminChoice() throws SQLException {
		AdminEngine adminEngine = new AdminEngine();

		switch (actionChoice) {
			case 1 -> {
				HashMap<String, String> searchTerms = search();

				ArrayList<Person> peopleFound = adminEngine.selectPeople(connection, searchTerms);

				System.out.println("\nResults:");

				for (Person p:
						peopleFound) {
					System.out.println("--------------------------------------------------");
					System.out.println("First Name: " + p.getFirstName());
					System.out.println("Middle Name: " + (p.getMiddleName().equals("") || p.getMiddleName() == null ? "[Undefined]" : p.getMiddleName()));
					System.out.println("Last Name: " + p.getLastName());
					System.out.println("Phone: " + (p.getPhone().getTel() == null || p.getPhone().getTel().equals("") ? "No phone number" : p.getPhone().getTel()));
					System.out.println("--------------------------------------------------");
				}
			}
			case 2 -> {

				String choice = "";

				while(true) {
					System.out.println("Want to insert: ");
					System.out.println("1) Person");
					System.out.println("2) Phone");
					System.out.print("Choice: ");

					choice = scanner.nextLine();

					if (!(choice.compareTo("0") > 0 && choice.compareTo("3") < 0))
					{
						System.out.println("Invalid input");
						continue;
					}
					break;
				}

				if(choice.equals("1"))
				{
					String firstName = "", midName = "", lastName = "";
					System.out.print("First Name(Required):");
					firstName = scanner.nextLine();
					System.out.print("Middle Name(Optional, just insert '-' and click enter to skip):");
					midName = scanner.nextLine();
					System.out.print("Last Name(Required):");
					lastName = scanner.nextLine();
					adminEngine.insertPersonSQL(connection, new Person(firstName, midName, lastName));
				}
				else
				{
					String tel = "";
					String personIdInput = "";
					int personId = -1;

					System.out.print("Telephone number: ");
					tel = scanner.nextLine();

					System.out.println();

					ArrayList<Person> people = listPeopleOf();

					for (Person p:
							people) {
						System.out.println("--------------------------------------------------");
						System.out.println("Id: " + p.getId());
						System.out.println("First Name: " + p.getFirstName());
						System.out.println("Middle Name: " + (p.getMiddleName().equals("") || p.getMiddleName() == null ? "[Undefined]" : p.getMiddleName()));
						System.out.println("Last Name: " + p.getLastName());
						System.out.println("--------------------------------------------------");
					}

					while(true)
					{
						System.out.print("Choose Person by id: ");

						personIdInput = scanner.nextLine();

						if(!tryParseInt(personIdInput))
						{
							System.out.println("Invalid id!");
							continue;
						}

						personId = Integer.parseInt(personIdInput);

						boolean found = false;
						for (Person p:
								people) {
							if(p.getId() == personId)
							{
								found = true;
								break;
							}
						}
						if(!found)
						{
							System.out.println("Person with this ID wasn't found!");
							continue;
						}

						break;
					}

					adminEngine.insertPersonSQL(connection, new Phone(tel, personId));
				}
				System.out.println("Successfully Added!");
			}
			case 3 -> {
				StringBuilder newValue = new StringBuilder();
				int[] result = updateMenu(newValue);
				if(result[1] == 4)
					adminEngine.updatePersonSQL(connection, newValue.toString(), result[2]);
				else
					adminEngine.updatePersonSQL(connection, result[0], result[1], newValue.toString());
				System.out.println("Successfully updated person!");
			}
			case 4 -> {
				System.out.println("Who to delete");
				String choice = "";
				int personId = -1;
				
				ArrayList<Person> people = listPeopleOf();

				for (Person p:
						people) {
					System.out.println("--------------------------------------------------");
					System.out.println("Id: " + p.getId());
					System.out.println("First Name: " + p.getFirstName());
					System.out.println("Middle Name: " + (p.getMiddleName().equals("") || p.getMiddleName() == null ? "[Undefined]" : p.getMiddleName()));
					System.out.println("Last Name: " + p.getLastName());
					System.out.println("--------------------------------------------------");
				}
				
				while (true) {
					System.out.print("Person Id: ");
					choice = scanner.nextLine();

					if (!tryParseInt(choice)) {
						System.out.println("\nInvalid input!\n");
						continue;
					}

					personId = Integer.parseInt(choice);

					if (!checkPersonId(personId)) {
						System.out.println("Person Not Found!");
						continue;
					}

					System.out.println("Are you sure? Y/N (Case insensitive)");
					boolean isSure = scanner.nextLine().toLowerCase().equals("y");

					if (isSure) {
						break;
					}
				}
				adminEngine.deletePersonSQL(connection, personId);
				System.out.println("Successfully deleted person!");
			}
		}
	}

	private void handleReaderChoice() throws SQLException {
		ReaderEngine readerEngine = new ReaderEngine();

		if (actionChoice == 1) {

			HashMap<String, String> searchTerms = search();

			ArrayList<Person> peopleFound = readerEngine.selectPeople(connection, searchTerms);

			System.out.println("\nResults:");

			for (Person p:
				 peopleFound) {
				System.out.println("--------------------------------------------------");
				System.out.println("First Name: " + p.getFirstName());
				System.out.println("Middle Name: " + (p.getMiddleName().equals("") || p.getMiddleName() == null ? "[Undefined]" : p.getMiddleName()));
				System.out.println("Last Name: " + p.getLastName());
				System.out.println("Phone: " + (p.getPhone().getTel() == null || p.getPhone().getTel().equals("") ? "No phone number" : p.getPhone().getTel()));
				System.out.println("--------------------------------------------------");
			}
		}
	}

	private HashMap<String, String> search() {
		HashMap<String, String> searchTerms = new HashMap<String, String>();

		while (true)
		{
			String choice = "";

			while(true)
			{
				System.out.println("Add search term for (if you already insert a certain search term, \nthe new input will replace the old one!): ");
				System.out.println("1) First Name");
				System.out.println("2) Middle Name");
				System.out.println("3) Last Name");
				System.out.println("4) Phone");
				System.out.println("5) Search!(to stop adding search terms)");
				System.out.print("\nChoice: ");
				choice = scanner.nextLine();

				if(choice.compareTo("0") > 0 && choice.compareTo("6") < 0)
				{
					break;
				}

				System.out.println("\nInvalid choice!\n");
			}

			if(choice.equals("5"))
				break;

			switch (choice) {
				case "1" -> {
					System.out.print("First Name: ");
					searchTerms.put(firstNameKey, scanner.nextLine());
				}
				case "2" -> {
					System.out.print("Middle Name: ");
					searchTerms.put(middleNameKey, scanner.nextLine());
				}
				case "3" -> {
					System.out.print("Last Name: ");
					searchTerms.put(lastNameKey, scanner.nextLine());
				}
				case "4" -> {
					System.out.print("Phone: ");
					searchTerms.put(phoneKey, scanner.nextLine());
				}
			}
		}

		return searchTerms;
	}

	//int[0] = personId
	//int[1] = what to manipulate
	//int[2] = phone to manipulate
	private int[] updateMenu(StringBuilder newValue) throws SQLException {
		int[] result = new int[3];
		String choice = "";

		ArrayList<Person> people = listPeopleOf();

		for (Person p:
			 people) {
			System.out.println("--------------------------------------------------");
			System.out.println("Id: " + p.getId());
			System.out.println("First Name: " + p.getFirstName());
			System.out.println("Middle Name: " + (p.getMiddleName().equals("") || p.getMiddleName() == null ? "[Undefined]" : p.getMiddleName()));
			System.out.println("Last Name: " + p.getLastName());
			System.out.println("--------------------------------------------------");
		}

		while(true)
		{
			System.out.print("Who to update(Insert Person Id): ");

			choice = scanner.nextLine();

			if (!tryParseInt(choice)) {
				System.out.println("\nInvalid input!\n");
				continue;
			}

			result[0] = Integer.parseInt(choice);

			if(checkPersonId(result[0]))
			{
				break;
			}
			System.out.println("Person Not Found!");
		}

		System.out.println("What To Update:");
		System.out.println("1) First Name");
		System.out.println("2) Middle Name");
		System.out.println("3) Last Name");
		System.out.println("4) Phone");
		System.out.print("Choice(insert the number in front of your choice): ");

		while(true) {

			choice = scanner.nextLine();

			if (!tryParseInt(choice)) {
				System.out.println("\nInvalid input!\n");
				continue;
			}

			result[1] = Integer.parseInt(choice);

			if(result[1] >= 1 && result[1] <= 4)
			{
				break;
			}

			System.out.println("Invalid choice!");
		}

		if(result[1] == 4)
		{
			ArrayList<Phone> phones = listPhonesOf(result[0]);

			System.out.println("[id]) [telephone number]");
			for (Phone p:
				 phones) {
				System.out.println(p.getId()+ ") " + p.getTel());
			}

			while (true)
			{
				System.out.print("Choose phone by id: ");

				choice = scanner.nextLine();

				if(!tryParseInt(choice))
				{
					System.out.println("Invalid input!");
					continue;
				}

				result[2] = Integer.parseInt(choice);

				boolean found = false;
				for (Phone p:
					 phones) {
					if(p.getId() == result[2])
					{
						found = true;
						break;
					}
				}
				if(!found)
				{
					System.out.println("Invalid choice!");
					continue;
				}
				break;
			}
		}

		System.out.print("New value: ");
		newValue.append(scanner.nextLine());

		return result;
	}

	private ArrayList<Phone> listPhonesOf(int personId) throws SQLException {
		System.out.println("Phones:");

		PreparedStatement ps = connection.prepareStatement("SELECT id, tel FROM phone WHERE id_person = " + personId + ";");

		ResultSet rs = ps.executeQuery();

		ArrayList<Phone> phones = new ArrayList<>();

		while(rs.next())
		{
			Phone p = new Phone(rs.getInt("id"), rs.getString("tel"));
			phones.add(p);
		}

		return phones;
	}

	private ArrayList<Person> listPeopleOf() throws SQLException {
		System.out.println("People:");

		PreparedStatement ps = connection.prepareStatement("SELECT id, first_name, mid_name, last_name FROM person;");

		ResultSet rs = ps.executeQuery();

		ArrayList<Person> people = new ArrayList<>();

		while(rs.next())
		{
			Person p = new Person(rs.getInt("id"), rs.getString("first_name"), rs.getString("mid_name"), rs.getString("last_name"));
			people.add(p);
		}

		return people;
	}

	private boolean tryParseInt(String item)
	{
		try {
			Integer.parseInt(item);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private boolean checkPersonId(int id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM person WHERE id = ?;");

		statement.setInt(1, id);

		ResultSet resultSet = statement.executeQuery();
		resultSet.next();

		return resultSet.getInt("COUNT(*)") == 1;
	}
}
