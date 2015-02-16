package jp.pmw.sit_and_go.after;

import java.util.List;
import jp.pmw.mysql.Connect;
import com.jenkov.db.PersistenceManager;
import com.jenkov.db.itf.IDaos;
import com.jenkov.db.itf.PersistenceException;

public class CompTmpData<T>{
	/**
	 * createdate : 2015年2月14日
	 * getTmpMstメソッド
	 * DBのTMP_CLSSS_SCHEDULE_MSTからデータを取得するためのORM
	 **/
	public List<T> getTmpMst(Class<T> clazz,String tmpMstTableName) throws PersistenceException{
		PersistenceManager manager = new PersistenceManager();
		IDaos daos = manager.createDaos(Connect.getInstance().getConnection());
		List<T> tempMst = daos.getObjectDao().readList(clazz, "SELECT * FROM `"+tmpMstTableName+"` WHERE `COMPLETE_FLAG` = ? ORDER BY `RECORED_INSERT_DATE_TIME` ASC", 1);
		return tempMst;
	}
	/**
	 * createdate : 2015年2月15日
	 * getAllTmpMstメソッド
	 * DBのTMP_CLSSS_SCHEDULE_MSTからデータを取得するためのORM
	 **/
	public List<T> getAllTmpMst(Class<T> clazz,String tmpMstTableName) throws PersistenceException{
		PersistenceManager manager = new PersistenceManager();
		IDaos daos = manager.createDaos(Connect.getInstance().getConnection());
		List<T> tempMst = daos.getObjectDao().readList(clazz, "SELECT * FROM `"+tmpMstTableName+"` ORDER BY `RECORED_INSERT_DATE_TIME` ASC");
		return tempMst;
	}
}
