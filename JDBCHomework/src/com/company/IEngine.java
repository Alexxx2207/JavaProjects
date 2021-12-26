package com.company;

import java.sql.SQLException;

public interface IEngine {
	void Run() throws SQLException;
	
	void showLoggingMenu();
	
	void showMainMenu();
	
	boolean validateCredentials() throws Throwable;
}
