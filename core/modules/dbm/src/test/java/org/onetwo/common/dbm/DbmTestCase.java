package org.onetwo.common.dbm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	DbmDaoTest.class,
	DbmEntityManagerTest.class,
//	OneBatchInsertTest.class,
	BatchInsertTest.class
})
public class DbmTestCase {

}
