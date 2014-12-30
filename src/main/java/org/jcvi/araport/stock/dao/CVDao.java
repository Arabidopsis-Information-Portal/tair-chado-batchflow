package org.jcvi.araport.stock.dao;

import javax.sql.DataSource;

import org.jcvi.araport.stock.domain.CV;
import org.jcvi.araport.stock.domain.Db;
import org.jcvi.araport.stock.domain.DbXref;

public interface CVDao {

	public boolean create (CV cv);
	public CV findDbByName(String name);
	public boolean save(CV cv);
	
	public void merge(CV cv);
	
	public CV mergeAndReturn(CV cv);
	
	public void setDataSource(DataSource datasource);
	
	
}