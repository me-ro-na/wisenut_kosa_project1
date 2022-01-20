package com.mopp.login.controllers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

import com.mopp.prescription.model.Prescription;
import com.mopp.profile.model.Member;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseJob {
	public static Connection conn;

	// 로그인 시 ID와 비밀번호를 가지고 정보 조회
	public boolean getLoginAuthority(String inputId, String inputPassword) {

		boolean isMember = false; // Member가 맞는지 확인하는 변수
		PreparedStatement pstmt = null; // Query 실행을 위한 변수

		try {
			// ID와 PW 조건으로 mem_no(PK)가 있는 지 확인
			pstmt = conn.prepareStatement("select mem_no from member where mem_user_id=? and mem_pwd=?");
			pstmt.setString(1, inputId);
			pstmt.setString(2, inputPassword);

			// next() : Query가 실행했을때 값이 있으면 true, 없으면 false값
			ResultSet rs = pstmt.executeQuery();
			isMember = rs.next();
			MainController.memNo = rs.getInt(1);
		} catch (SQLException e) {
			System.err.println("[ERROR] DatabaseJob Method is getLoginAuthority - " + e.getMessage());
		}

		return isMember;
	}

	// static 사용 목적 : 프로그램 사용중에는 Query실행이 수차례 발생하는데, 그 때마다 새로 connection 객체를 시도하고
	// close()하는데 시간이 많이 걸림
	// -> 프로그램 시작부터 종료하기까지 하나의 connection을 유지하고, 호출하면서 처리시간을 단축
	// -> 프로그램 실행시 로딩하는데 시간이 많이 걸리는 이유
	public static Connection getConnection() {
		// 환경설정 파일을 읽어오기 위한 객체 생성
		Properties properties = new Properties();
		Reader reader;

		try {
			reader = new FileReader("lib/oracle.properties"); // 읽어올 파일 지정
			properties.load(reader); // 설정 파일 로딩하기

			String driverName = properties.getProperty("driver"); // driver name
			String url = properties.getProperty("url");
			String user = properties.getProperty("user");
			String pwd = properties.getProperty("password");

			// SQL을 사용하지 위한 JDBC Driver 불러오기
			Class.forName(driverName);

			// DB 연결 시도
			conn = DriverManager.getConnection(url, user, pwd);
			conn.setAutoCommit(true);
		} catch (FileNotFoundException e) {
			System.err.println("[ERROR] DatabaseJob Method is getConnection: 지정한 파일을 찾을수없습니다 - " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("[ERROR] DatabaseJob Method is getConnection: 드라이버로드 실패 - " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("[ERROR] DatabaseJob Method is getConnection: connection fail - " + e.getMessage());
			e.printStackTrace();
		}

		// DB connection을 static으로 저장
		return conn;
	}

	public boolean createMember(Map<Integer, String> memberInfo) {
		boolean isMember = false; // Member가 맞는지 확인하는 변수
		PreparedStatement pstmt = null; // Query 실행을 위한 변수

		try {
			// ID와 PW 조건으로 mem_no(PK)가 있는 지 확인
			pstmt = conn.prepareStatement("select count(mem_no) from member");

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int memberCount = rs.getInt(1);

				pstmt = conn.prepareStatement("insert into member values(?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, memberCount + 1);
				pstmt.setString(2, memberInfo.get(2));
				pstmt.setString(3, memberInfo.get(3));
				pstmt.setString(4, memberInfo.get(4));
				pstmt.setString(5, memberInfo.get(5));
				pstmt.setString(6, memberInfo.get(6));
				pstmt.setString(7, memberInfo.get(7));
				pstmt.setString(8, memberInfo.get(8));
				pstmt.setString(9, memberInfo.get(9));
				pstmt.setString(10, memberInfo.get(10));

				// next() : Query가 실행했을때 값이 있으면 true, 없으면 false값
				isMember = pstmt.executeQuery().next();
				pstmt.close();
			}
		} catch (SQLException e) {
			System.err.println("[ERROR] DatabaseJob Method is createMember - " + e.getMessage());
		}

		return isMember;
	}

	public ObservableList<Prescription> getPrescriptions() {
		PreparedStatement pstmt = null; // Query 실행을 위한 변수

		ObservableList<Prescription> list = FXCollections.observableArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			// ID와 PW 조건으로 mem_no(PK)가 있는 지 확인
			String sql = "select rx_date, rx_symptoms from prescription where rx_mem_no=? order by rx_date desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, MainController.memNo);

			ResultSet rs = pstmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				Prescription pre = new Prescription();
				pre.setNo(i++);
				pre.setCreateDate(sdf.format(rs.getDate("rx_date")));
				pre.setSymptoms(rs.getString("rx_symptoms"));
				list.add(pre);
			}
		} catch (SQLException e) {
			System.err.println("[ERROR] DatabaseJob Method is getPrescriptions - " + e.getMessage());
			e.printStackTrace();
		}

		return list;
	}

	public String searchID(String name, String email) {
		String yourId = "";
		PreparedStatement pstmt = null; // Query 실행을 위한 변수

		try {
			String sql = "select mem_user_id from member where mem_name=? and mem_email=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				yourId = rs.getString(1);

		} catch (SQLException e) {
			System.err.println("[ERROR] DatabaseJob Method is searchID - " + e.getMessage());
			e.printStackTrace();
		}

		return yourId;
	}

	public String searchPW(String name, String email) {
		String yourId = "";
		PreparedStatement pstmt = null; // Query 실행을 위한 변수

		try {
			String sql = "select mem_user_id from member where mem_name=? and mem_email=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				yourId = rs.getString(1);
			}
		} catch (SQLException e) {
			System.err.println("[ERROR] DatabaseJob Method is searchPW - " + e.getMessage());
			e.printStackTrace();
		}

		return yourId;
	}

	public int checkIdandName(String id, String email) {
		int memNo = 0;
		PreparedStatement pstmt = null; // Query 실행을 위한 변수

		try {
			String sql = "select mem_no from member where mem_user_id=? and mem_name=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, email);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				memNo = rs.getInt(1);

		} catch (SQLException e) {
			System.err.println("[ERROR] DatabaseJob Method is checkIdandName - " + e.getMessage());
		}

		return memNo;

	}

	public boolean passwordReset(int memNo, String password) {

		boolean isReset = false;
		PreparedStatement pstmt = null; // Query 실행을 위한 변수

		try {
			String sql = "update member set mem_pwd=? where mem_no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setString(2, String.valueOf(memNo));

			ResultSet rs = pstmt.executeQuery();
			isReset = rs.next();

		} catch (SQLException e) {
			System.err.println("[ERROR] DatabaseJob Method is passwordReset - " + e.getMessage());
		}

		return isReset;
	}

	public Member getUserInfo(int memNo) throws Exception {
		String sql = "select * from member where mem_no=?";
		PreparedStatement pstmt = null;
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, MainController.memNo);
		Member userInfo = new Member();

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			userInfo.setName(rs.getString("mem_name"));
			userInfo.setMemNo(rs.getInt("mem_no")); // ID
			userInfo.setMemId(rs.getString("mem_user_id"));
			userInfo.setPwd(rs.getString("mem_pwd"));
			userInfo.setTel(rs.getString("mem_tel"));
			userInfo.setGender(rs.getString("mem_gender"));
			String[] emailValue = rs.getString("mem_email").split("@");
			userInfo.setEmail1(emailValue[0]);
			userInfo.setEmail2(emailValue[1]);
			userInfo.setBirthday(rs.getDate("mem_birthday"));
			userInfo.setRegDate(rs.getDate("mem_reg_date").toString());
			userInfo.setIsAdmin(rs.getString("mem_is_admin"));
		}
		return userInfo;
	}
}
