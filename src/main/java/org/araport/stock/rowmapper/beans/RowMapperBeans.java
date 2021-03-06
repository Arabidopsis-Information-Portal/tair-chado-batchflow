package org.araport.stock.rowmapper.beans;

import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockPropertySource;
import org.araport.stock.processor.DbXrefBatchProcessor;
import org.araport.stock.processor.StockBatchItemProcessor;
import org.araport.stock.processor.StockCVTermItemProcessor;
import org.araport.stock.processor.StockItemProcessor;
import org.araport.stock.processor.StockPropertyItemProcessor;
import org.araport.stock.reader.StockPropertiesItemReader;
import org.araport.stock.rowmapper.DbXrefRowMapper;
import org.araport.stock.rowmapper.DbXrefSourceRowMapper;
import org.araport.stock.rowmapper.SourceStockDrivingQueryRowMapper;
import org.araport.stock.rowmapper.StockCVTermSourceRowMapper;
import org.araport.stock.rowmapper.StockPropertiesSourceRowMapper;
import org.araport.stock.rowmapper.StockPropertyRowMapper;
import org.araport.stock.rowmapper.StockRowMapper;
import org.araport.stock.rowmapper.StockSourceRowMapper;
import org.araport.stock.writer.StockCVTermJdbcBatchWriter;
import org.araport.stock.writer.StockItemWriter;
import org.araport.stock.writer.StockJdbcBatchWriter;
import org.araport.stock.writer.StockPropertyItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class RowMapperBeans {

	@Bean
	public SourceStockDrivingQueryRowMapper sourceStockDrivingQueryMapper(){
		return new SourceStockDrivingQueryRowMapper(); 
	}
	
	@Bean
	public StockRowMapper stockRowMapper(){
		return new StockRowMapper(); 
	}
	
	@Bean
	public StockItemProcessor stockItemProcessor(){
		return new StockItemProcessor(); 
	}
	
	@Bean
	public StockPropertyItemProcessor stockPropertyItemProcessor(){
		return new StockPropertyItemProcessor();
	}
	
	@Bean
	public StockBatchItemProcessor stockBatchItemProcessor(){
		return new StockBatchItemProcessor();
	}
	
	
	@Bean
	public StockItemWriter stockItemWriter(){
		return new StockItemWriter();
	}
	
	@Bean
	public StockPropertyItemWriter stockPropertyItemWriter(){
		return new StockPropertyItemWriter();
	}
	
	@Bean 
	public StockJdbcBatchWriter stockJdbcBatchWriter(){
		return new StockJdbcBatchWriter();
	}
	
	@Bean
	public StockPropertiesSourceRowMapper stockPropertiesSourceMapper(){
		return new StockPropertiesSourceRowMapper();
	}
	
	@Bean
	public StockPropertyRowMapper stockPropertyRowMapper(){
		return new StockPropertyRowMapper();
	}
	
	@Bean 
	public DbXrefRowMapper dbXrefRowMapper(){
		return new DbXrefRowMapper();
	}
	
	@Bean 
	public DbXrefSourceRowMapper dbXrefSourceRowMapper(){
		return new DbXrefSourceRowMapper();
	}
	
	@Bean
	public DbXrefBatchProcessor dbXrefBatchProcessor(){
		return new DbXrefBatchProcessor();
	}
	
	@Bean 
	public 	StockSourceRowMapper stockSourceRowMapper(){
		return new StockSourceRowMapper();
	}
		
	@Bean
	public StockCVTermItemProcessor stockCVTermItemProcessor(){
		return new StockCVTermItemProcessor();
	}
	
	@Bean
	public      StockCVTermSourceRowMapper stockCVTermSourceRowMapper(){
		return new StockCVTermSourceRowMapper();
	}
	

}
