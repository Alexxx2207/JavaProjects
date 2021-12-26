package com.company;

import com.company.Models.Person;
import com.company.Models.Phone;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.company.GlobalConstants.*;

public class ReaderEngine implements Role{

	@Override
	public ArrayList<Person> selectPeople(Connection con, HashMap<String, String> searchTerms) throws SQLException {


		StringBuilder query = new StringBuilder("SELECT first_name, mid_name, last_name, tel  FROM person pe LEFT JOIN phone p ON pe.id = p.id_person WHERE 1=1");

		for (Map.Entry<String, String> pair:
			 searchTerms.entrySet()) {

			switch(pair.getKey())
			{
				case firstNameKey ->
				{
					query.append(" AND first_name LIKE '%").append(pair.getValue()).append("%'");
				}
				case middleNameKey ->
				{
					query.append(" AND mid_name LIKE '%").append(pair.getValue()).append("%'");
				}
				case lastNameKey ->
				{
					query.append(" AND last_name LIKE '%").append(pair.getValue()).append("%'");
				}
				case phoneKey ->
				{
					String toAdd = " AND tel LIKE '%" + pair.getValue() + "%'";
					query.append(toAdd);
				}
			}
		}

		query.append(';');

		PreparedStatement ps = con.prepareStatement(query.toString());

		ResultSet rs = ps.executeQuery();

		ArrayList<Person> result = new ArrayList<>();

		while(rs.next())
		{
			Person p = new Person(rs.getString("first_name"), rs.getString("mid_name"), rs.getString("last_name"), new Phone(rs.getString("tel")));

			result.add(p);
		}

		return result;
	}
}
