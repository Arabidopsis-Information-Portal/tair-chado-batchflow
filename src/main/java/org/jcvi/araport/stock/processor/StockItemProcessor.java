package org.jcvi.araport.stock.processor;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.jcvi.araport.stock.dao.DbDao;
import org.jcvi.araport.stock.dao.DbXRefDao;
import org.jcvi.araport.stock.dao.OrganismDao;
import org.jcvi.araport.stock.dao.StockDao;
import org.jcvi.araport.stock.dao.impl.DbDaoImpl;
import org.jcvi.araport.stock.dao.impl.DbXrefDaoImpl;
import org.jcvi.araport.stock.dao.impl.OrganismDaoImpl;
import org.jcvi.araport.stock.dao.impl.StockDaoImpl;
import org.jcvi.araport.stock.domain.Db;
import org.jcvi.araport.stock.domain.DbXref;
import org.jcvi.araport.stock.domain.Organism;
import org.jcvi.araport.stock.domain.SourceStockDrivingQuery;
import org.jcvi.araport.stock.writer.DbXrefItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.jcvi.araport.stock.domain.Stock;


@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class, DbXrefDaoImpl.class, OrganismDaoImpl.class, StockDaoImpl.class })
public class StockItemProcessor implements ItemProcessor <SourceStockDrivingQuery, Stock>{

	@Autowired
	DataSource targetDataSource;
	
	@Autowired
	private DbDao dbDao;
	
	@Autowired
	private DbXRefDao dbXrefDao;
	
	@Autowired
	private OrganismDao organismDao;

	private static final Logger log = Logger
			.getLogger(StockItemProcessor.class);

	
	@Override
	public Stock process(SourceStockDrivingQuery sourceRecord) throws Exception {
		
		return createStock(sourceRecord.getStockId());
		
	}
	
	public Stock createStock(Long sourceStockId){
		
		Db tairStockDb = dbDao.findDbByName("TAIR Stock");
		int dbId = tairStockDb.getDbId();
		
		String accession = String.valueOf(sourceStockId);
		DbXref dbXref = dbXrefDao.findDbXrefByAccessionAndDb(dbId, accession);
			
		if (dbXref == null) { // create DbXRef accession
			log.info("DBXref is null. Creating DbXref Accession = " + accession);
			dbXrefDao.merge(dbXref);
		} else {
			log.info("DBXref exists!");
		}
		
		
		Organism organism = organismDao.findByName("mouse-ear cress");
		
		if (organism == null) { // create organism
			log.info("Organism is null. Creating Organism = " + "mouse-ear cress");
			//dbXrefDao.merge(dbXref);
		} else {
			log.info("Organism exists!");
		}
		
		
		Stock stock = new Stock();
		
		stock.setStockId(2);
		stock.setDbxrefId(163629);
		stock.setStockTypeId(44354);
		stock.setName("CD3-179");
		stock.setOrganismId(6);
		
		
		int dbXRefId = dbXref.getDbXrefId();
			
		
		return null;
		
	}

}