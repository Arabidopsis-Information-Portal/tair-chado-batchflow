package org.araport.stock.tasklet.business;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.jcvi.stock.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.common.ApplicationConstants;
import org.araport.stock.common.MetadataExecutionContext;
import org.araport.stock.dao.CVDao;
import org.araport.stock.dao.CVTermDao;
import org.araport.stock.dao.DbDao;
import org.araport.stock.dao.DbXRefDao;
import org.araport.stock.dao.OrganismDao;
import org.araport.stock.dao.StockDao;
import org.araport.stock.dao.impl.CVDaoImpl;
import org.araport.stock.dao.impl.CVTermDaoImpl;
import org.araport.stock.dao.impl.DbDaoImpl;
import org.araport.stock.dao.impl.DbXrefDaoImpl;
import org.araport.stock.dao.impl.OrganismDaoImpl;
import org.araport.stock.dao.impl.StockDaoImpl;
import org.araport.stock.domain.CV;
import org.araport.stock.domain.CVTerm;
import org.araport.stock.domain.Db;
import org.araport.stock.domain.DbXref;
import org.araport.stock.exception.ExceptionLogger;
import org.araport.stock.exception.StockLoaderException;
import org.araport.stock.utils.FileUtils;
import org.omg.CORBA.portable.InputStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class,
		DbXrefDaoImpl.class, StockDaoImpl.class, CVDaoImpl.class, CVTermDaoImpl.class })
@PropertySources(value = { @PropertySource("classpath:/sql/bootstrap/db_init_cvterm_stockproperties.sql") })
public class StockPropertiesCVTermLookupTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(StockPropertiesCVTermLookupTasklet.class);

	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DataSource targetDataSource;

	@Autowired
	private DbDao dbDao;

	private DbXRefDao dbXrefDao;

	private StockDao stockDao;

	private CVDao cvDao;
	
	private CVTermDao cvTermDao;

	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context)
			throws Exception {

		String sql = FileUtils
				.getSqlFileContents("/sql/bootstrap/db_init_cvterm_stockproperties.sql");
		log.info("Injected SQL:" + sql);
		dbDao.executeSQL(sql);

		Db stockTermDb = dbDao.findDbByName("stock_term");

		if (stockTermDb == null) {
			throw new StockLoaderException(
					"Database with name stock_term not ! Cannot load stock properties cvterms data.");
		}

		log.info("Stock Term Database:" + stockTermDb);

		populateLookups(stockTermDb.getDbId());

		return RepeatStatus.FINISHED;
	}

	@PostConstruct
	public void setDao() {
		this.dbXrefDao = new DbXrefDaoImpl();
		this.dbXrefDao.setDataSource(targetDataSource);
		this.stockDao = new StockDaoImpl();
		this.stockDao.setDataSource(targetDataSource);
		this.cvDao = new CVDaoImpl();
		this.cvDao.setDataSource(targetDataSource);
		this.cvTermDao = new CVTermDaoImpl();
		this.cvTermDao.setDataSource(targetDataSource);
		

	}

	private void populateLookups(int dbId) {

		CV cv = new CV();
		cv = populateCVLookup();
		populateDBCVTermStockProperties(dbId, cv);

	}

	private CV populateCVLookup() {

		CV cv = new CV();

		cv.setName(ApplicationConstants.CV_STOCK_PROPERTY_NAME);
		cv.setDefintion(ApplicationConstants.CV_STOCK_PROPERTY_DEFINITION);

		cvDao.merge(cv);

		cv = cvDao.mergeAndReturn(cv);

		log.info("CV:" + cv);
		
		return cv;
	}

	private void addCVTerm(final CV cv, final DbXref dbXref, final String name) {

		CVTerm cvTerm = new CVTerm();
		cvTerm.setCvId(cv.getCvId());
		cvTerm.setDbXrefId(dbXref.getDbXrefId());
		cvTerm.setName(name);
		cvTerm.setIs_obsolete(0);
		cvTerm.setIs_relationshiptype(0);
		
		cvTermDao.merge(cvTerm);

	}

	private void populateDBCVTermStockProperties(int dbId, final CV cv) {

		List<String> cvTerms = MetadataExecutionContext.getInstance()
				.getAllCvTermStockProperties();

		for (String item : cvTerms) {
			log.info("Stock CV Term: " + item);

			log.info("Loading CV Term to database...");

			DbXref dbXref = new DbXref();
			dbXref.setDbId(dbId);
			dbXref.setPrimaryAccession(item);
			dbXref.setVersion("");
			dbXref.setDescription("");

			dbXref = dbXrefDao.mergeAndReturn(dbXref);

			log.info("Loaded DbXref: " + dbXref);
			
			addCVTerm(cv, dbXref, item);

		}

	}

}
