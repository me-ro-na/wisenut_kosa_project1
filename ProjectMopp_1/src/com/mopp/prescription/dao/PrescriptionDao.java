package com.mopp.prescription.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mopp.DBUtil.DBConnection;
import com.mopp.prescription.model.Medicine;
import com.mopp.prescription.model.Prescription;

public class PrescriptionDao {
	public static Connection conn = DBConnection.getConnection();

	public List<Medicine> allMedicine() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		List<Medicine> list = new ArrayList<>();

		sql.append("SELECT * FROM MEDICINE ");
		sql.append("ORDER BY MED_NO ASC ");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Medicine med = new Medicine();
				med = setDTO(rs);
				list.add(med);
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is allMedicine - " + e.getMessage());
		}
		return list;
	}

	public List<Medicine> rxMed(String rxID) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		List<Medicine> list = new ArrayList<>();

		sql.append("SELECT * ");
		sql.append("FROM MEDICINE M, ");
		sql.append("(SELECT HAVERX_MED_NO, HAVERX_ID FROM HAVERX WHERE HAVERX_RX_ID = ?) H ");
		sql.append("WHERE M.MED_NO = H.HAVERX_MED_NO ");
		sql.append("ORDER BY H.HAVERX_ID DESC ");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rxID);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Medicine med = setDTO(rs);
				list.add(med);
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is rxMed - " + e.getMessage());
		}
		return list;
	}

	private int getPreCountAll() {
		StringBuffer sql = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;

		sql.append("SELECT COUNT(*) FROM PRESCRIPTION ");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is getPreCountAll - " + e.getMessage());
		}
		return result;
	}

	private int getMemPreCount(int memNo) {
		StringBuffer sql = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;

		sql.append("SELECT COUNT(*) FROM PRESCRIPTION WHERE RX_MEM_NO = ? ");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memNo);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is getMemPreCount - " + e.getMessage());
		}
		return result;
	}

	private int getHaveRXCountAll() {
		StringBuffer sql = new StringBuffer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;

		sql.append("SELECT COUNT(*) FROM HAVERX ");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is getHaveRXCountAll - " + e.getMessage());
		}
		return result;
	}

	public void createPre(Prescription pre, List<Integer> medList) {
		StringBuffer sql = new StringBuffer();
		CallableStatement cstmt = null;

//		RX_ID: NO_(MEM_NO)_DATE_(user's_prescription_no)
		sql.append("{CALL INSERT_RX(?, ?, ?)} ");

		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

		String rxID = String.format("%02d", getPreCountAll() + 1) + "_" + String.format("%02d", pre.getMemNo()) + "_"
				+ sdf.format(today) + "_" + String.format("%02d", getMemPreCount(pre.getMemNo()) + 1);
		pre.setRxID(rxID);

		try {
			cstmt = conn.prepareCall(sql.toString());
			cstmt.setString(1, pre.getRxID());
			cstmt.setInt(2, pre.getMemNo());
			cstmt.setString(3, pre.getSymptoms());

			cstmt.executeUpdate();
			createHaveRX(pre, medList);

		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is createPre - " + e.getMessage());
		}
	}

	private void createHaveRX(Prescription pre, List<Integer> medList) {
		StringBuffer sql = new StringBuffer();
		CallableStatement cstmt = null;
		int haverxCount = getHaveRXCountAll();

		sql.append("{CALL INSERT_HAVERX(?, ?, ?)} ");
		try {
//			HAVERX_ID: NO_(HAVERX_RX_ID)
			for (int medNo : medList) {
				String haveRXID = String.format("%02d", haverxCount + 1) + "_" + pre.getRxID();
				cstmt = conn.prepareCall(sql.toString());
				cstmt.setString(1, haveRXID);
				cstmt.setString(2, pre.getRxID());
				cstmt.setInt(3, medNo);

				haverxCount++;
				cstmt.executeUpdate();
				cstmt = null;
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is createHaveRX - " + e.getMessage());
		}
	}

	public void delRX(String rxID) {
		delHaveRx(rxID);
		StringBuffer sql = new StringBuffer();
		PreparedStatement pstmt = null;

		sql.append("DELETE FROM PRESCRIPTION WHERE RX_ID = ?");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rxID);
			pstmt.executeQuery();

		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is delHaveRx - " + e.getMessage());
		}
	}

	public void updatePre(Prescription pre, int[] medListNew, int[] medListOrg, List<Integer> haveDelList, List<Integer> haveNewList) {
		StringBuffer sql = new StringBuffer();
		PreparedStatement pstmt = null;

		sql.append("UPDATE PRESCRIPTION SET ");
		sql.append("RX_ID = ?, RX_SYMPTOMS = ?, RX_IS_TAKING = ? ");
		sql.append("WHERE RX_ID = ? ");

		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, pre.getRxID());
			pstmt.setString(2, pre.getSymptoms());
			pstmt.setString(3, pre.getIsTaking());
			pstmt.setString(4, pre.getRxID());

			pstmt.executeQuery();
			
			howHaveRX(pre.getRxID(), medListNew, medListOrg, haveDelList, haveNewList);

		} catch (Exception e) {
			e.printStackTrace();
//			System.err.println("[ERROR] PrescriptionDao Method is updatePre - " + e.getMessage());
		}
	}

	private void howHaveRX(String rxID, int[] medListNew, int[] medListOrg, List<Integer> haveDelList, List<Integer> haveCrList) {
		for (int i = 0; i < medListNew.length; i++) {
			if (medListOrg[i] > 0) {
				updateHaveRX(rxID, medListOrg[i], medListNew[i]);
			}
		}
		if (haveDelList.size() > 0) {
			for(int med : haveDelList) {
				delHaveRxMed(rxID, med);
			}
		}
		if(haveCrList.size() > 0) {
			for(int med : haveCrList) {
				crHaveRxMed(rxID, med);
			}
		}
	}
	
	private void crHaveRxMed(String rxID, int medNo) {
		StringBuffer sql = new StringBuffer();
		CallableStatement cstmt = null;
		int haverxCount = getHaveRXCountAll();

		sql.append("{CALL INSERT_HAVERX(?, ?, ?)} ");
		try {
//			HAVERX_ID: NO_(HAVERX_RX_ID)
				String haveRXID = String.format("%02d", haverxCount + 1) + "_" + rxID;
				cstmt = conn.prepareCall(sql.toString());
				cstmt.setString(1, haveRXID);
				cstmt.setString(2, rxID);
				cstmt.setInt(3, medNo);

				cstmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is createHaveRX - " + e.getMessage());
		}
	}
	
	private void delHaveRxMed(String rxID, int medNo) {
		StringBuffer sql = new StringBuffer();
		PreparedStatement pstmt = null;

		sql.append("DELETE FROM HAVERX WHERE HAVERX_RX_ID = ? AND HAVERX_MED_NO = ? ");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rxID);
			pstmt.setInt(2, medNo);
			pstmt.executeQuery();

		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is delHaveRxMed - " + e.getMessage());
		}
	}

	private void delHaveRx(String rxID) {
		StringBuffer sql = new StringBuffer();
		PreparedStatement pstmt = null;

		sql.append("DELETE FROM HAVERX WHERE HAVERX_RX_ID = ? ");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, rxID);
			pstmt.executeQuery();

		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is delHaveRx - " + e.getMessage());
		}
	}

	private void updateHaveRX(String rxID, int medNoOrg, int medNoNew) {
		StringBuffer sql = new StringBuffer();
		PreparedStatement pstmt = null;
		sql.append("UPDATE HAVERX SET HAVERX_MED_NO = ? WHERE HAVERX_RX_ID = ? AND HAVERX_MED_NO = ? ");
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, medNoNew);
			pstmt.setString(2, rxID);
			pstmt.setInt(3, medNoOrg);
			pstmt.executeQuery();

		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is updateHaveRX - " + e.getMessage());
		}

	}

	private Medicine setDTO(ResultSet rs) {
		Medicine dto = new Medicine();
		try {
			dto.setMedNo(rs.getInt("med_no"));
			dto.setMedName(rs.getString("med_name"));
			dto.setMedCode(rs.getString("med_code"));
			dto.setMedMade(rs.getString("med_made"));
			dto.setMedWarn(rs.getString("med_warn"));
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionDao Method is setDTO - " + e.getMessage());
		}
		return dto;
	}

}
