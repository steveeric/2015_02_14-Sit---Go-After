package jp.pmw.sit_and_go.after;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.jenkov.db.itf.PersistenceException;

import jp.pmw.log.MyLog;
import jp.pmw.mysql.Connect;
import jp.pmw.sitandgo.config.MyConfig;
import jp.pmw.sitandgo.tmp.TMP_CLASS_SCHEDULE_MST;
import jp.pmw.sitandgo.tmp.TMP_FACULTY_MST;
import jp.pmw.sitandgo.tmp.TMP_ROOM_MST;
import jp.pmw.sitandgo.tmp.TMP_STAFF_MST;
import jp.pmw.sitandgo.tmp.TMP_STUDENT_MST;
import jp.pmw.util.error.UtilError;

public class AfterLog {

	//ログファイル
	private File aterLogFile;
	private  BufferedWriter bw;
	public AfterLog(){
		process();
	}

	private void process(){
		try {
			boolean create = createFile();
			if(create == false){
				return ;
			}
			creteBuffer();
			getTmpConpItem();
			closeWritting();
		} catch (IOException e) {
			MyLog.getInstance().error(e.getMessage());
		} catch (PersistenceException e) {
			MyLog.getInstance().error(e.getMessage());
			UtilError.showError(e.getCause());
		} catch (SQLException e) {
			MyLog.getInstance().error(e.getMessage());
		}
	}

	private boolean createFile() throws IOException{
		aterLogFile = new File(MyConfig.PATH_AFTER + "\\" + System.currentTimeMillis()+MyConfig.LOG_EXTENTION);
		boolean createFlag = aterLogFile.createNewFile();
		if(createFlag == false){
			MyLog.getInstance().error("TMPログファイルを作成できませんでした.");
		}
		return createFlag;
	}

	private void creteBuffer() throws IOException{
		if(this.aterLogFile == null){
			MyLog.getInstance().error("TMPDBのログ処理を行うことが出来ませんでした.");
			return;
		}else{
			bw = new BufferedWriter(new FileWriter(aterLogFile));
		}
	}

	private void closeWritting() throws IOException{
		this.bw.close();
	}

	private void getTmpConpItem() throws PersistenceException, IOException, SQLException{
		String[] oderCompTmpTableName = MyConfig.ODER_TMP_TABLE_COMP;
		//String[] oderCompTmpTableName = {"TMP_ROOM_MST"};
		for(int i=0;i<oderCompTmpTableName.length;i++){
			String tmpMstTableName = oderCompTmpTableName[i];
			if(MyConfig.DB_TABLE_TMP_ROOM_MST.equals(tmpMstTableName)){
				CompTmpData<TMP_ROOM_MST> tmp = new CompTmpData<TMP_ROOM_MST>();
				List<TMP_ROOM_MST> list = tmp.getAllTmpMst(TMP_ROOM_MST.class, tmpMstTableName);
				createTmpRoomMst(list);
				deleteTmpData(tmpMstTableName);
			}else if(MyConfig.DB_TABLE_TMP_STUDENT_MST.equals(tmpMstTableName)){
				CompTmpData<TMP_STUDENT_MST> tmp = new CompTmpData<TMP_STUDENT_MST>();
				List<TMP_STUDENT_MST> list = tmp.getAllTmpMst(TMP_STUDENT_MST.class, tmpMstTableName);
				createTmpStudentMst(list);
				deleteTmpData(tmpMstTableName);
			}else if(MyConfig.DB_TABLE_TMP_FACULTY_MST.equals(tmpMstTableName)){
				CompTmpData<TMP_FACULTY_MST> tmp = new CompTmpData<TMP_FACULTY_MST>();
				List<TMP_FACULTY_MST> list = tmp.getAllTmpMst(TMP_FACULTY_MST.class, tmpMstTableName);
				createTmpFacultyMst(list);
				deleteTmpData(tmpMstTableName);
			}else if(MyConfig.DB_TABLE_TMP_STAFF_MST.equals(tmpMstTableName)){
				CompTmpData<TMP_STAFF_MST> tmp = new CompTmpData<TMP_STAFF_MST>();
				List<TMP_STAFF_MST> list = tmp.getAllTmpMst(TMP_STAFF_MST.class, tmpMstTableName);
				createTmpStaffMst(list);
				deleteTmpData(tmpMstTableName);
			}else if(MyConfig.DB_TABLE_TMP_CLASS_SCHEDULE_MST.equals(tmpMstTableName)){
				CompTmpData<TMP_CLASS_SCHEDULE_MST> tmp = new CompTmpData<TMP_CLASS_SCHEDULE_MST>();
				List<TMP_CLASS_SCHEDULE_MST> list = tmp.getAllTmpMst(TMP_CLASS_SCHEDULE_MST.class, tmpMstTableName);
				createTmpClassScheduleMst(list);
				deleteTmpData(tmpMstTableName);
			}
		}

	}

	private void createTmpRoomMst(List<TMP_ROOM_MST> list) throws IOException{
		for(int i=0;i<list.size();i++){
			writeTmpLog(list.get(i).getTmpLog());
		}
	}
	private void createTmpStudentMst(List<TMP_STUDENT_MST> list) throws IOException{
		for(int i=0;i<list.size();i++){
			writeTmpLog(list.get(i).getTmpLog());
		}
	}
	private void createTmpFacultyMst(List<TMP_FACULTY_MST> list) throws IOException{
		for(int i=0;i<list.size();i++){
			writeTmpLog(list.get(i).getTmpLog());
		}
	}
	private void createTmpStaffMst(List<TMP_STAFF_MST> list) throws IOException{
		for(int i=0;i<list.size();i++){
			writeTmpLog(list.get(i).getTmpLog());
		}
	}
	private void createTmpClassScheduleMst(List<TMP_CLASS_SCHEDULE_MST> list) throws IOException{
		for(int i=0;i<list.size();i++){
			writeTmpLog(list.get(i).getTmpLog());
		}
	}
	private void writeTmpLog(String tmpLog) throws IOException{
		//書き込み
		bw.write(tmpLog);
		bw.newLine();
	}
	private void deleteTmpData(String tmpTableName) throws SQLException{
		//String sql = "DELETE FROM `"+tmpTableName+"` WHERE `COMPLETE_FLAG` = 1";
		String sql = "DELETE FROM `"+tmpTableName+"`";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			Connect.getInstance().getConnection().setAutoCommit(false);
			ps = Connect.getInstance().getConnection().prepareStatement(sql);
			int result = ps.executeUpdate();
			if(result == 0){
				MyLog.getInstance().error("TMPテーブル名:"+tmpTableName+"の使用済みデータを削除出来ませんでした.");
			}else{
				MyLog.getInstance().error("TMPテーブル名:"+tmpTableName+"の使用済みデータを削除しました.");
			}
		} finally {
			if(ps != null){
				ps.close();
			}
			if(rs != null){
				rs.close();
			}if(Connect.getInstance().getConnection() != null){
				Connect.getInstance().getConnection().commit();
			}
		}
	}

}
