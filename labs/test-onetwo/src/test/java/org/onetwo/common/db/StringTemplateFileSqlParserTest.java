package org.onetwo.common.db;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.hibernate.sql.HibernateNamedInfo;
import org.onetwo.common.hibernate.sql.HibernateNamedSqlFileManager;
import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.spring.sql.FileSqlParser;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.ParserContext;
import org.onetwo.common.spring.sql.ParserContextFunctionSet;
import org.onetwo.common.spring.sql.SqlFunctionFactory;
import org.onetwo.common.spring.sql.StringTemplateLoaderFileSqlParser;
import org.onetwo.common.spring.sql.TemplateInNamedQueryParser;
import org.onetwo.common.utils.LangUtils;

public class StringTemplateFileSqlParserTest {

	private HibernateNamedSqlFileManager fileManager;
	private FileSqlParser parser;
	ParserContext parserContext;
	
	@Before
	public void before(){
		StringTemplateLoaderFileSqlParser<HibernateNamedInfo> p = new StringTemplateLoaderFileSqlParser<HibernateNamedInfo>();
		this.parser = p;
		this.fileManager = new HibernateNamedSqlFileManager(DataBase.Oracle, false, HibernateNamedInfo.class, p);
		fileManager.build();
//		this.parser.initParser();
	}
	@Test
	
	public void testPaser(){
		HibernateNamedInfo info = this.fileManager.getNamedQueryInfo("testParserQuery");
		parserContext = ParserContext.create();
		this.parserContext.put(SqlFunctionFactory.CONTEXT_KEY, SqlFunctionFactory.getSqlFunctionDialet(info.getDataBaseType()));
		this.parserContext.put(ParserContextFunctionSet.CONTEXT_KEY, ParserContextFunctionSet.getInstance());
		TemplateInNamedQueryParser attrParser = new TemplateInNamedQueryParser(parser, parserContext, info);
		this.parserContext.put(JFishNamedFileQueryInfo.TEMPLATE_KEY, attrParser);
		this.parserContext.put("userName", "way");
		this.parserContext.put("datestr", "2014-04-19");
		String sql = this.parser.parse(info.getFullName(), parserContext);
		System.out.println("sql: " + sql);
		Assert.assertEquals("select id from ( select id from tableName where userName like '%way' ) where startDate >= convert(datetime, 201404)", sql.trim());
	}
	
	@Test
	public void testPaser2(){
		HibernateNamedInfo info = this.fileManager.getNamedQueryInfo("testParserQuery2");
		parserContext = ParserContext.create();
		this.parserContext.put(SqlFunctionFactory.CONTEXT_KEY, SqlFunctionFactory.getSqlFunctionDialet(info.getDataBaseType()));
		TemplateInNamedQueryParser attrParser = new TemplateInNamedQueryParser(parser, parserContext, info);
		this.parserContext.put(JFishNamedFileQueryInfo.TEMPLATE_KEY, attrParser);
		this.parserContext.put(ParserContextFunctionSet.CONTEXT_KEY, ParserContextFunctionSet.getInstance());
		this.parserContext.put("userName", "way");
		String sql = this.parser.parse(info.getFullName(), parserContext);
		System.out.println("sql: " + sql);
		Assert.assertEquals("select * from tableName2 t where t.id in ( select id from tableName where userName like '%way' )", sql);
	}
	

	
	@Test
	public void testPaser3(){
		HibernateNamedInfo info = this.fileManager.getNamedQueryInfo("testParserQuery3");
		parserContext = ParserContext.create();
		this.parserContext.put(SqlFunctionFactory.CONTEXT_KEY, SqlFunctionFactory.getSqlFunctionDialet(info.getDataBaseType()));
		TemplateInNamedQueryParser attrParser = new TemplateInNamedQueryParser(parser, parserContext, info);
		this.parserContext.put(JFishNamedFileQueryInfo.TEMPLATE_KEY, attrParser);
		this.parserContext.put(ParserContextFunctionSet.CONTEXT_KEY, ParserContextFunctionSet.getInstance());
		this.parserContext.put("userName", "way");
		String sql = this.parser.parse(info.getFullName(), parserContext);
		System.out.println("sql: " + sql);
		Assert.assertEquals("update sb set aa=bb from batch_user sb where sb.way=:userName;", sql);
	}

	
	@Test
	public void testNumberParams(){
		HibernateNamedInfo info = this.fileManager.getNamedQueryInfo("testNumberParams");
		parserContext = ParserContext.create();
		this.parserContext.put(SqlFunctionFactory.CONTEXT_KEY, SqlFunctionFactory.getSqlFunctionDialet(info.getDataBaseType()));
		TemplateInNamedQueryParser attrParser = new TemplateInNamedQueryParser(parser, parserContext, info);
		this.parserContext.put(JFishNamedFileQueryInfo.TEMPLATE_KEY, attrParser);
		this.parserContext.put(ParserContextFunctionSet.CONTEXT_KEY, ParserContextFunctionSet.getInstance());
		this.parserContext.put("id", 100405611);
		String sql = this.parser.parse(info.getFullName(), parserContext);
		System.out.println("sql: " + sql);
		Assert.assertEquals("select *  from tableName2 t where  t.id = '100405611'", sql);
	}
	

	@Test
	public void testForeach(){
		HibernateNamedInfo info = this.fileManager.getNamedQueryInfo("testForeach");
		parserContext = ParserContext.create();
		this.parserContext.put(SqlFunctionFactory.CONTEXT_KEY, SqlFunctionFactory.getSqlFunctionDialet(info.getDataBaseType()));
		TemplateInNamedQueryParser attrParser = new TemplateInNamedQueryParser(parser, parserContext, info);
		this.parserContext.put(JFishNamedFileQueryInfo.TEMPLATE_KEY, attrParser);
		this.parserContext.put(ParserContextFunctionSet.CONTEXT_KEY, ParserContextFunctionSet.getInstance());
		this.parserContext.put("cardNos", LangUtils.newArrayList("111", "2222"));
		String sql = this.parser.parse(info.getFullName(), parserContext);
		System.out.println("sql: " + sql);
		Assert.assertEquals("select uic.ic_lno from ( SELECT ui.ic_lno, count(ui.ic_lno) as amount from issue_user_info ui where ui.ic_lno in (111, 2222) group by ui.ic_lno ) uic where uic.amount>0", sql);
	}
	

	@Test
	public void testInParams(){
		HibernateNamedInfo info = this.fileManager.getNamedQueryInfo("testInParams");
		parserContext = ParserContext.create();
		this.parserContext.put(SqlFunctionFactory.CONTEXT_KEY, SqlFunctionFactory.getSqlFunctionDialet(info.getDataBaseType()));
		TemplateInNamedQueryParser attrParser = new TemplateInNamedQueryParser(parser, parserContext, info);
		this.parserContext.put(JFishNamedFileQueryInfo.TEMPLATE_KEY, attrParser);
		this.parserContext.put(ParserContextFunctionSet.CONTEXT_KEY, ParserContextFunctionSet.getInstance());
		this.parserContext.put("cardNos", LangUtils.newArrayList("111", "2222"));
		String sql = this.parser.parse(info.getFullName(), parserContext);
		System.out.println("sql: " + sql);
		Assert.assertEquals("select uic.ic_lno from ( SELECT ui.ic_lno, count(ui.ic_lno) as amount from issue_user_info ui where ui.ic_lno in ( :cardNo0, :cardNo1 ) group by ui.ic_lno ) uic where uic.amount>0", sql);
	}
	
}
