package com.ecom.app.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.h2.tools.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class H2DBUtil {
	
	@Autowired
	private DataSource dataSource;

	public void exportDB(File file) throws SQLException {
		Script.process(dataSource.getConnection(), file.getPath(), "DROP", "");
	}

	public void importDB(File file) throws FileNotFoundException, SQLException {
		RunScript.execute(dataSource.getConnection(), new FileReader(file));
	}

}
