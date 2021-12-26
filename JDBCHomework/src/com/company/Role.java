package com.company;

import com.company.Models.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Role
{
	ArrayList<Person> selectPeople(Connection con, HashMap<String, String> searchTerms) throws SQLException;
}
