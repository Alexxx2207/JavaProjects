package com.company;

import com.company.Models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminEngine extends ReaderEngine implements Role {

	public void insertPersonSQL(Connection con, Person person) throws SQLException {
		StringBuilder query = new StringBuilder("INSERT INTO person(first_name, last_name");

		if(person.getMiddleName().equals("-"))
		{
			String toAdd = "('"+ person.getFirstName() + "', '" + person.getLastName() + "');";
			query.append(") VALUES ").append(toAdd);
		}
		else
		{
			String toAdd = "('" + person.getFirstName() + "', '" + person.getLastName() + "', '" + person.getMiddleName() + "');";

			query.append(", mid_name) VALUES ").append(toAdd);
		}

		PreparedStatement ps = con.prepareStatement(query.toString());

		ps.executeUpdate();
	}

	public void insertPersonSQL(Connection con, Phone phone) throws SQLException {
		StringBuilder query = new StringBuilder("INSERT INTO phone(tel, id_person) VALUES ");

		String toAdd = "('" + phone.getTel() + "', " + phone.getPersonId() + ");";

		query.append(toAdd);

		PreparedStatement ps = con.prepareStatement(query.toString());

		ps.executeUpdate();
	}

	public void updatePersonSQL(Connection con, int personId, int choiceToUpdate, String newValue) throws SQLException {
		StringBuilder query = new StringBuilder("UPDATE person SET ");

		switch(choiceToUpdate)
		{
			case 1 ->
			{
				query.append("first_name = '").append(newValue).append("' ");
			}
			case 2 ->
			{
				query.append("mid_name = '").append(newValue).append("' ");
			}
			case 3 ->
			{
				query.append("last_name = '").append(newValue).append("' ");
			}
		}

		query.append("WHERE id = ").append(personId).append(";");

		PreparedStatement ps = con.prepareStatement(query.toString());

		ps.executeUpdate();
	}

	public void updatePersonSQL(Connection con, String newValue, int phoneId) throws SQLException {

		String query = "UPDATE phone SET " + "tel = " + newValue + " " +
				"WHERE id = " + phoneId + ";";
		PreparedStatement ps = con.prepareStatement(query);

		ps.executeUpdate();
	}

	public void deletePersonSQL(Connection con, int personId) throws SQLException {

		String query = "DELETE FROM person WHERE id = ?;";

		PreparedStatement ps = con.prepareStatement(query);

		ps.setInt(1, personId);

		ps.executeUpdate();
	}
}
