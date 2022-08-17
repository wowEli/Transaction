package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.JDBCUtil;
import vo.Bank1;

public class Bank1DAO {
	Connection conn;
	PreparedStatement pstmt;
	
	final String sql_transfer1="UPDATE BANK1 SET BALANCE=BALANCE-? WHERE BID=101";
	final String sql_transfer2="UPDATE BANK2 SET BALANCE=BALANCE+? WHERE BID=222";
	public boolean transfer(int balance) {
		// DAO의 메서드는 일반적으로 vo
		// 강제적으로 자료형,인자의 개수를 고정할수도있음!
		conn=JDBCUtil.connect();
		try {
			conn.setAutoCommit(false);
			// 트랜잭션의 시작을 설정하는 메서드
			// 자동commit을 해제할수있음(MySQL)
			
			// +++하나의 작업단위+++
			pstmt=conn.prepareStatement(sql_transfer1);
			pstmt.setInt(1, balance);
			pstmt.executeUpdate();
			
			pstmt=conn.prepareStatement(sql_transfer2);
			pstmt.setInt(1, balance);
			pstmt.executeUpdate();
			// +++하나의 작업단위+++
			
			System.out.println("얍!");
			pstmt=conn.prepareStatement(sql_selectOne);
			ResultSet rs=pstmt.executeQuery();
			rs.next();
			System.out.println(rs.getString("BNAME")+" | "+rs.getInt("BALANCE"));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rs.close();
			
			pstmt=conn.prepareStatement(sql_selectOne);
			rs=pstmt.executeQuery();
			rs.next();
			if(rs.getInt("BALANCE")<0) { // 가진금액보다 더많이 계좌이체를 하려고할때
				conn.rollback();
				return false;
			}
			else {
				conn.commit();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return true;
	}
	
	final String sql_selectOne="SELECT * FROM BANK1 WHERE BID=101";
	public Bank1 selectOne(Bank1 vo) {
		Bank1 data=null;
		conn=JDBCUtil.connect();
		try {
			pstmt=conn.prepareStatement(sql_selectOne);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				data=new Bank1();
				data.setBalance(rs.getInt("BALANCE"));
				data.setBid(rs.getInt("BID"));
				data.setBname(rs.getString("BNAME"));
				System.out.println("Bank1DAO: selectOne(): "+data);
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.disconnect(pstmt, conn);
		}
		return data;
	}
}
