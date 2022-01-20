package com.mopp.prescription.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mopp.DBUtil.DBConnection;
import com.mopp.prescription.model.Medicine;
import com.mopp.prescription.model.Prescription;

public class PrescriptionHistoryDao {

	public static Connection conn = DBConnection.getConnection();
	PrescriptionDao dao = new PrescriptionDao();

	public Prescription getPrescription(String preID) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		Prescription pre = null;

		try {
			sql.append("SELECT rx_id, rx_mem_no, rx_date, rx_symptoms, rx_is_taking ");
			sql.append("FROM PRESCRIPTION WHERE RX_ID=? ");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, preID);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				pre = setDTO(rs);
				if(dao.rxMed(preID).size() > 1) {
					pre.setMedicine(getMedicineInfo(preID).getMedName() + " 외");
				} else {
					pre.setMedicine(getMedicineInfo(preID).getMedName());
				}
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionHistoryDAO Method is getPrescription - " + e.getMessage());
		}
		return pre;
	}

	public List<Prescription> getPreMemList(int memNo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		List<Prescription> list = new ArrayList<>();
		try {
			sql.append("SELECT rx_id, rx_mem_no, rx_date, rx_symptoms, rx_is_taking ");
			sql.append("FROM PRESCRIPTION WHERE RX_MEM_NO = ? ORDER BY RX_ID DESC ");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memNo);
			rs = pstmt.executeQuery();

			int i = 1;
			while (rs.next()) {
				Prescription pre = new Prescription();
				pre = setDTO(rs);
				pre.setNo(i);
				if(dao.rxMed(pre.getRxID()).size() > 1) {
					pre.setMedicine(getMedicineInfo(pre.getRxID()).getMedName() + " 외");
				} else {
					pre.setMedicine(getMedicineInfo(pre.getRxID()).getMedName());
				}
				list.add(pre);
				i++;
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionHistoryDAO Method is getPreMemList - " + e.getMessage());
		}
		return list;

	}

	private Medicine getMedicineInfo(String preID) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		Medicine med = new Medicine();
		try {
			sql.append("SELECT M.MED_NO, M.MED_NAME ");
			sql.append("FROM MEDICINE M, ");
			sql.append("(SELECT HAVERX_MED_NO FROM HAVERX WHERE HAVERX_RX_ID = ?) H ");
			sql.append("WHERE M.MED_NO = H.HAVERX_MED_NO AND ROWNUM = 1 ");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, preID);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				med.setMedNo(rs.getInt(1));
				med.setMedName(rs.getString(2));
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionHistoryDAO Method is getMedicineInfo - " + e.getMessage());
		}
		return med;

	}

	private Prescription setDTO(ResultSet rs) {
		Prescription dto = new Prescription();
		try {
			dto.setRxID(rs.getString("rx_id"));
			dto.setMemNo(rs.getInt("rx_mem_no"));
			String viewDate = rs.getString("rx_date").substring(2, 10);
			dto.setCreateDate(viewDate);
			dto.setSymptoms(rs.getString("rx_symptoms"));
			String isTaking = "복용중";
			if (rs.getString("rx_is_taking").equals("F")) {
				isTaking = "복용 중단";
			}
			dto.setIsTaking(isTaking);
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionHistoryDAO Method is setDTO - " + e.getMessage());
		}
		return dto;
	}

	public String getMemName(int memNo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		String userName = null;
		try {
			sql.append("SELECT MEM_NAME FROM MEMBER WHERE MEM_NO = ?");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, memNo);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				userName = rs.getString("mem_name");
			}
		} catch (Exception e) {
			System.err.println("[ERROR] PrescriptionHistoryDAO Method is getMemName - " + e.getMessage());
		}
		return userName;
	}
}
