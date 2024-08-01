package com.customs360.bizz.schedulers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.customs360.bizz.services.EmailService;

@Component
public class AllClientScheduler {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("firstDataSource")
	private DataSource firstDataSource;

	@Autowired
	@Qualifier("secondDataSource")
	private DataSource secondDataSource;

	@Autowired
	@Qualifier("thirdDataSource")
	private DataSource thirdDataSource;

	@Autowired
	@Qualifier("fourthDataSource")
	private DataSource fourthDataSource;

	@Autowired
	private EmailService emailService;
	
	//@Scheduled(cron = "0 30 19 26 * *")
	@Scheduled(cron = "0 10 14 * * *")
	public void allClientTnetsDC1() {
		logger.info("DC1 allClient scheduler starting");

		try {
			Connection connection = firstDataSource.getConnection();
			// Connection connection = DriverManager.getConnection(url, username, password);
			logger.info("DC1 Connection 1 DB successfull");

			Connection connection2 = fourthDataSource.getConnection();
			// Connection connection2 = DriverManager.getConnection(url2, username2,
			// password2);
			logger.info("DataLake Server Connection 4 DB successfull");

			// Call the stored procedure
			CallableStatement cs = connection.prepareCall("{call a_sp_extract_tnets_allClient_permit_data()}");
			// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
			cs.execute();
			logger.info("DC1 - callThe a_sp_extract_tnets_allClient_permit_data SP");

			// Retrieve the results
			ResultSet result = cs.getResultSet();
			List<String[]> dataList = new ArrayList<>();
			logger.info("DC1 data - list The data in the SP");

			int fetchCount = 0;
			while (result.next()) {
				String[] data = new String[70];
				data[0] = result.getString("job_no");
				data[1] = result.getString("permit_approval_date");
				data[2] = result.getString("declarant_name");
				data[3] = result.getString("dec_type");
				data[4] = result.getString("importer_exporter_uen");
				data[5] = result.getString("importer_exporter_name");
				data[6] = result.getString("mawb_obl");
				data[7] = result.getString("hawb");
				data[8] = result.getString("vessel");
				data[9] = result.getString("aircraft_name");
				data[10] = result.getString("voyage");
				data[11] = result.getString("final_dest_country");
				data[12] = result.getString("country_code");
				data[13] = result.getString("loading_discharge_portName");
				data[14] = result.getString("loading_discharge_Port");
				data[15] = result.getString("permit_number");
				data[16] = result.getString("first_approval_date");
				data[17] = result.getString("arrival_departure_date");
				data[18] = result.getString("dec_agent_name");
				data[19] = result.getString("dec_agent_uen");
				data[20] = result.getString("forwarder_name");
				data[21] = result.getString("forwarder_uen");
				data[22] = result.getString("aed_permit_condition");
				data[23] = result.getString("tot_items");
				data[24] = result.getString("amend_reason");
				data[25] = result.getString("aed_status");
				data[26] = result.getString("cif_fob_value");
				data[27] = result.getString("tot_gross_wight");
				data[28] = result.getString("tot_gross_wight_unit");
				data[29] = result.getString("tot_gst");
				data[30] = result.getString("tot_outer_pack");
				data[31] = result.getString("tot_outer_pack_unit");
				data[32] = result.getString("term_type");
				data[33] = result.getString("packaging_type");
				data[34] = result.getString("late_by");
				data[35] = result.getString("late_grouping");
				data[36] = result.getString("apr_month");
				data[37] = result.getString("transport_mode");
				data[38] = result.getString("flight_id");
				data[39] = result.getString("freight_category");
				data[40] = result.getString("shipment_category");
				data[41] = result.getString("permit_type");
				data[42] = result.getString("message_type");
				data[43] = result.getString("request_type");
				data[44] = result.getString("gst_report_category");
				data[45] = result.getString("gst_f5_return");
				data[46] = result.getString("equipment_id");
				data[47] = result.getString("seal_id");
				data[48] = result.getString("seq_num");
				data[49] = result.getString("size_type");
				data[50] = result.getString("weight");
				data[51] = result.getString("customs_duty_amount");
				data[52] = result.getString("hs_code");
				data[53] = result.getString("hs_description");
				data[54] = result.getString("items_gst_amount");
				data[55] = result.getString("origin_con");
				data[56] = result.getString("reference_id");
				data[57] = result.getString("items_id");
				data[58] = result.getString("charge_code");
				data[59] = result.getString("incoterm");
				data[60] = result.getString("division");
				data[61] = result.getString("initials");
				data[62] = result.getString("cancel_count");
				data[63] = result.getString("baseline_request");
				data[64] = result.getString("client_code");
				data[65] = result.getString("user_name");
                data[66] = result.getString("mailbox_id");
                data[67] = result.getString("mailbox_name");
                data[68] = result.getString("supplier_name");
                data[69] = result.getString("supplier_uen");
				dataList.add(data);
				fetchCount++;
				// logger.info("DC1 allClient_report fetched count: " + fetchCount + " data from tnets41 DB");
			}
			System.out.println("DC1 allClient_report retrive" + "_" + fetchCount);

			// Insert the results into the local database
			String sql2 = "INSERT INTO allClient_report (job_no,permit_approval_date,declarant_name,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,first_approval_date,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,origin_con,reference_id,items_id,charge_code,incoterm,division,initials,cancel_count,baseline_request,client_code,user_name, mailbox_id, mailbox_name,supplier_name,supplier_uen) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			logger.info("DC1 allClient_report insertQuery Started");

			PreparedStatement statement2 = connection2.prepareStatement(sql2);
			logger.info("DC1 allClient_report insertQuery prepareStatement");

			int insertCount = 0;
			for (String[] data : dataList) {
				statement2.setString(1, data[0]);
				statement2.setString(2, data[1]);
				statement2.setString(3, data[2]);
				statement2.setString(4, data[3]);
				statement2.setString(5, data[4]);
				statement2.setString(6, data[5]);
				statement2.setString(7, data[6]);
				statement2.setString(8, data[7]);
				statement2.setString(9, data[8]);
				statement2.setString(10, data[9]);
				statement2.setString(11, data[10]);
				statement2.setString(12, data[11]);
				statement2.setString(13, data[12]);
				statement2.setString(14, data[13]);
				statement2.setString(15, data[14]);
				statement2.setString(16, data[15]);
				statement2.setString(17, data[16]);
				statement2.setString(18, data[17]);
				statement2.setString(19, data[18]);
				statement2.setString(20, data[19]);
				statement2.setString(21, data[20]);
				statement2.setString(22, data[21]);
				statement2.setString(23, data[22]);
				statement2.setString(24, data[23]);
				statement2.setString(25, data[24]);
				statement2.setString(26, data[25]);
				statement2.setString(27, data[26]);
				statement2.setString(28, data[27]);
				statement2.setString(29, data[28]);
				statement2.setString(30, data[29]);
				statement2.setString(31, data[30]);
				statement2.setString(32, data[31]);
				statement2.setString(33, data[32]);
				statement2.setString(34, data[33]);
				statement2.setString(35, data[34]);
				statement2.setString(36, data[35]);
				statement2.setString(37, data[36]);
				statement2.setString(38, data[37]);
				statement2.setString(39, data[38]);
				statement2.setString(40, data[39]);
				statement2.setString(41, data[40]);
				statement2.setString(42, data[41]);
				statement2.setString(43, data[42]);
				statement2.setString(44, data[43]);
				statement2.setString(45, data[44]);
				statement2.setString(46, data[45]);
				statement2.setString(47, data[46]);
				statement2.setString(48, data[47]);
				statement2.setString(49, data[48]);
				statement2.setString(50, data[49]);
				statement2.setString(51, data[50]);
				statement2.setString(52, data[51]);
				statement2.setString(53, data[52]);
				statement2.setString(54, data[53]);
				statement2.setString(55, data[54]);
				statement2.setString(56, data[55]);
				statement2.setString(57, data[56]);
				statement2.setString(58, data[57]);
				statement2.setString(59, data[58]);
				statement2.setString(60, data[59]);
				statement2.setString(61, data[60]);
				statement2.setString(62, data[61]);
				statement2.setString(63, data[62]);
				statement2.setString(64, data[63]);
				statement2.setString(65, data[64]);
				statement2.setString(66, data[65]);
				statement2.setString(67, data[66]);
				statement2.setString(68, data[67]);
				statement2.setString(69, data[68]);
				statement2.setString(70, data[69]);
				statement2.executeUpdate();

				// logger.info("inserted job_no: {}, permit_number: {}, first_approval_date:
				// {},permit_approval_date: {}, client_code: {}", data[0], data[15], data[16],
				// data[1], data[64]);
			}

			System.out.println("DC1 allClient_report inserted count:" + dataList.size()
					+ " data to linux DB executed successfully");

			// Send email notification upon successful completion
			emailService.sendEmailNotification(fetchCount, dataList.size(), "DC1_allClient");
			logger.info("sendEmailNotification");

			connection.close();
			connection2.close();

			logger.info("DC1 allClient_report end ");

		} catch (SQLException e) {
			System.out.println("DC1 Oops, error!");
			e.printStackTrace();

		}

	}

	//@Scheduled(cron = "0 50 19 26 * *")
	@Scheduled(cron = "0 30 14 * * *")
	public void allClientTnetsDC2() {
		logger.info("DC2 allClient scheduler starting");

		try {

			Connection connection = secondDataSource.getConnection();
			// Connection connection = DriverManager.getConnection(url, username, password);
			logger.info("DC2 Connection 1 DB successfull");

			Connection connection2 = fourthDataSource.getConnection();
			// Connection connection2 = DriverManager.getConnection(url2, username2,
			// password2);
			logger.info("DataLake Server Connection 2 DB successfull");

			// Call the stored procedure
			CallableStatement cs = connection.prepareCall("{call a_sp_extract_tnets_allClient_permit_data()}");
			// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
			cs.execute();
			logger.info("DC2 - callThe a_sp_extract_tnets_allClient_permit_data SP");

			// Retrieve the results
			ResultSet result = cs.getResultSet();
			List<String[]> dataList = new ArrayList<>();
			logger.info("DC2 data - list The data in the SP");

			int fetchCount = 0;
			while (result.next()) {
				String[] data = new String[70];
				data[0] = result.getString("job_no");
				data[1] = result.getString("permit_approval_date");
				data[2] = result.getString("declarant_name");
				data[3] = result.getString("dec_type");
				data[4] = result.getString("importer_exporter_uen");
				data[5] = result.getString("importer_exporter_name");
				data[6] = result.getString("mawb_obl");
				data[7] = result.getString("hawb");
				data[8] = result.getString("vessel");
				data[9] = result.getString("aircraft_name");
				data[10] = result.getString("voyage");
				data[11] = result.getString("final_dest_country");
				data[12] = result.getString("country_code");
				data[13] = result.getString("loading_discharge_portName");
				data[14] = result.getString("loading_discharge_Port");
				data[15] = result.getString("permit_number");
				data[16] = result.getString("first_approval_date");
				data[17] = result.getString("arrival_departure_date");
				data[18] = result.getString("dec_agent_name");
				data[19] = result.getString("dec_agent_uen");
				data[20] = result.getString("forwarder_name");
				data[21] = result.getString("forwarder_uen");
				data[22] = result.getString("aed_permit_condition");
				data[23] = result.getString("tot_items");
				data[24] = result.getString("amend_reason");
				data[25] = result.getString("aed_status");
				data[26] = result.getString("cif_fob_value");
				data[27] = result.getString("tot_gross_wight");
				data[28] = result.getString("tot_gross_wight_unit");
				data[29] = result.getString("tot_gst");
				data[30] = result.getString("tot_outer_pack");
				data[31] = result.getString("tot_outer_pack_unit");
				data[32] = result.getString("term_type");
				data[33] = result.getString("packaging_type");
				data[34] = result.getString("late_by");
				data[35] = result.getString("late_grouping");
				data[36] = result.getString("apr_month");
				data[37] = result.getString("transport_mode");
				data[38] = result.getString("flight_id");
				data[39] = result.getString("freight_category");
				data[40] = result.getString("shipment_category");
				data[41] = result.getString("permit_type");
				data[42] = result.getString("message_type");
				data[43] = result.getString("request_type");
				data[44] = result.getString("gst_report_category");
				data[45] = result.getString("gst_f5_return");
				data[46] = result.getString("equipment_id");
				data[47] = result.getString("seal_id");
				data[48] = result.getString("seq_num");
				data[49] = result.getString("size_type");
				data[50] = result.getString("weight");
				data[51] = result.getString("customs_duty_amount");
				data[52] = result.getString("hs_code");
				data[53] = result.getString("hs_description");
				data[54] = result.getString("items_gst_amount");
				data[55] = result.getString("origin_con");
				data[56] = result.getString("reference_id");
				data[57] = result.getString("items_id");
				data[58] = result.getString("charge_code");
				data[59] = result.getString("incoterm");
				data[60] = result.getString("division");
				data[61] = result.getString("initials");
				data[62] = result.getString("cancel_count");
				data[63] = result.getString("baseline_request");
				data[64] = result.getString("client_code");
				data[65] = result.getString("user_name");
                data[66] = result.getString("mailbox_id");
                data[67] = result.getString("mailbox_name");
                data[68] = result.getString("supplier_name");
                data[69] = result.getString("supplier_uen");
				dataList.add(data);
				fetchCount++;

				// logger.info("DC2 allClient_report fetched count: " + fetchCount + " data from
				// tnets41 DB");
			}
			System.out.println("DC2 allClient_report retrive" + "_" + fetchCount);

			// Insert the results into the local database
			String sql2 = "INSERT INTO allClient_report (job_no,permit_approval_date,declarant_name,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,first_approval_date,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,origin_con,reference_id,items_id,charge_code,incoterm,division,initials,cancel_count,baseline_request,client_code,user_name, mailbox_id, mailbox_name,supplier_name,supplier_uen) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			logger.info("DC2 allClient_report insertQuery Started");

			PreparedStatement statement2 = connection2.prepareStatement(sql2);
			logger.info("DC2 allClient_report insertQuery prepareStatement");

			int insertCount = 0;
			for (String[] data : dataList) {
				statement2.setString(1, data[0]);
				statement2.setString(2, data[1]);
				statement2.setString(3, data[2]);
				statement2.setString(4, data[3]);
				statement2.setString(5, data[4]);
				statement2.setString(6, data[5]);
				statement2.setString(7, data[6]);
				statement2.setString(8, data[7]);
				statement2.setString(9, data[8]);
				statement2.setString(10, data[9]);
				statement2.setString(11, data[10]);
				statement2.setString(12, data[11]);
				statement2.setString(13, data[12]);
				statement2.setString(14, data[13]);
				statement2.setString(15, data[14]);
				statement2.setString(16, data[15]);
				statement2.setString(17, data[16]);
				statement2.setString(18, data[17]);
				statement2.setString(19, data[18]);
				statement2.setString(20, data[19]);
				statement2.setString(21, data[20]);
				statement2.setString(22, data[21]);
				statement2.setString(23, data[22]);
				statement2.setString(24, data[23]);
				statement2.setString(25, data[24]);
				statement2.setString(26, data[25]);
				statement2.setString(27, data[26]);
				statement2.setString(28, data[27]);
				statement2.setString(29, data[28]);
				statement2.setString(30, data[29]);
				statement2.setString(31, data[30]);
				statement2.setString(32, data[31]);
				statement2.setString(33, data[32]);
				statement2.setString(34, data[33]);
				statement2.setString(35, data[34]);
				statement2.setString(36, data[35]);
				statement2.setString(37, data[36]);
				statement2.setString(38, data[37]);
				statement2.setString(39, data[38]);
				statement2.setString(40, data[39]);
				statement2.setString(41, data[40]);
				statement2.setString(42, data[41]);
				statement2.setString(43, data[42]);
				statement2.setString(44, data[43]);
				statement2.setString(45, data[44]);
				statement2.setString(46, data[45]);
				statement2.setString(47, data[46]);
				statement2.setString(48, data[47]);
				statement2.setString(49, data[48]);
				statement2.setString(50, data[49]);
				statement2.setString(51, data[50]);
				statement2.setString(52, data[51]);
				statement2.setString(53, data[52]);
				statement2.setString(54, data[53]);
				statement2.setString(55, data[54]);
				statement2.setString(56, data[55]);
				statement2.setString(57, data[56]);
				statement2.setString(58, data[57]);
				statement2.setString(59, data[58]);
				statement2.setString(60, data[59]);
				statement2.setString(61, data[60]);
				statement2.setString(62, data[61]);
				statement2.setString(63, data[62]);
				statement2.setString(64, data[63]);
				statement2.setString(65, data[64]);
				statement2.setString(66, data[65]);
				statement2.setString(67, data[66]);
				statement2.setString(68, data[67]);
				statement2.setString(69, data[68]);
				statement2.setString(70, data[69]);
				statement2.executeUpdate();
				// logger.info("inserted job_no: {}, permit_number: {}, first_approval_date:
				// {},permit_approval_date: {}, client_code: {}", data[0], data[15],
				// data[16],data[1], data[64]);
			}
			System.out.println("DC2 allClient_report inserted count:" + dataList.size()
					+ " data to linux DB executed successfully");

			// Send email notification upon successful completion
			emailService.sendEmailNotification(fetchCount, dataList.size(), "DC2_allClient");
			logger.info("sendEmailNotification");

			connection.close();
			connection2.close();

			logger.info("DC2 allClient_report end ");
			System.out.println("DC2 allClient_report scheduled completed");

		} catch (SQLException e) {
			System.out.println("DC2 Oops, error!");
			e.printStackTrace();

		}

	}

	//@Scheduled(cron = "0 20 20 26 * *")
	@Scheduled(cron = "0 00 15 * * *")
	public void allClientTnetsHQ() {
		logger.info("HQ allClient scheduler starting");

		try {
			Connection connection = thirdDataSource.getConnection();
			// Connection connection = DriverManager.getConnection(url, username, password);
			logger.info("HQ Connection 1 DB successfull");

			Connection connection2 = fourthDataSource.getConnection();
			// Connection connection2 = DriverManager.getConnection(url2, username2,
			// password2);
			logger.info("DataLake Server Connection 2 DB successfull");

			// Call the stored procedure
			CallableStatement cs = connection.prepareCall("{call a_sp_extract_tnets_allClient_permit_data()}");
			// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
			cs.execute();
			logger.info("HQ - callThe a_sp_extract_tnets_allClient_permit_data SP");

			// Retrieve the results
			ResultSet result = cs.getResultSet();
			List<String[]> dataList = new ArrayList<>();
			logger.info("HQ data - list The data in the SP");

			int fetchCount = 0;
			while (result.next()) {
				String[] data = new String[70];
				data[0] = result.getString("job_no");
				data[1] = result.getString("permit_approval_date");
				data[2] = result.getString("declarant_name");
				data[3] = result.getString("dec_type");
				data[4] = result.getString("importer_exporter_uen");
				data[5] = result.getString("importer_exporter_name");
				data[6] = result.getString("mawb_obl");
				data[7] = result.getString("hawb");
				data[8] = result.getString("vessel");
				data[9] = result.getString("aircraft_name");
				data[10] = result.getString("voyage");
				data[11] = result.getString("final_dest_country");
				data[12] = result.getString("country_code");
				data[13] = result.getString("loading_discharge_portName");
				data[14] = result.getString("loading_discharge_Port");
				data[15] = result.getString("permit_number");
				data[16] = result.getString("first_approval_date");
				data[17] = result.getString("arrival_departure_date");
				data[18] = result.getString("dec_agent_name");
				data[19] = result.getString("dec_agent_uen");
				data[20] = result.getString("forwarder_name");
				data[21] = result.getString("forwarder_uen");
				data[22] = result.getString("aed_permit_condition");
				data[23] = result.getString("tot_items");
				data[24] = result.getString("amend_reason");
				data[25] = result.getString("aed_status");
				data[26] = result.getString("cif_fob_value");
				data[27] = result.getString("tot_gross_wight");
				data[28] = result.getString("tot_gross_wight_unit");
				data[29] = result.getString("tot_gst");
				data[30] = result.getString("tot_outer_pack");
				data[31] = result.getString("tot_outer_pack_unit");
				data[32] = result.getString("term_type");
				data[33] = result.getString("packaging_type");
				data[34] = result.getString("late_by");
				data[35] = result.getString("late_grouping");
				data[36] = result.getString("apr_month");
				data[37] = result.getString("transport_mode");
				data[38] = result.getString("flight_id");
				data[39] = result.getString("freight_category");
				data[40] = result.getString("shipment_category");
				data[41] = result.getString("permit_type");
				data[42] = result.getString("message_type");
				data[43] = result.getString("request_type");
				data[44] = result.getString("gst_report_category");
				data[45] = result.getString("gst_f5_return");
				data[46] = result.getString("equipment_id");
				data[47] = result.getString("seal_id");
				data[48] = result.getString("seq_num");
				data[49] = result.getString("size_type");
				data[50] = result.getString("weight");
				data[51] = result.getString("customs_duty_amount");
				data[52] = result.getString("hs_code");
				data[53] = result.getString("hs_description");
				data[54] = result.getString("items_gst_amount");
				data[55] = result.getString("origin_con");
				data[56] = result.getString("reference_id");
				data[57] = result.getString("items_id");
				data[58] = result.getString("charge_code");
				data[59] = result.getString("incoterm");
				data[60] = result.getString("division");
				data[61] = result.getString("initials");
				data[62] = result.getString("cancel_count");
				data[63] = result.getString("baseline_request");
				data[64] = result.getString("client_code");
				data[65] = result.getString("user_name");
                data[66] = result.getString("mailbox_id");
                data[67] = result.getString("mailbox_name");
                data[68] = result.getString("supplier_name");
                data[69] = result.getString("supplier_uen");
				dataList.add(data);
				fetchCount++;

				// logger.info("HQ allClient_report fetched count: " + fetchCount + " data from
				// tnets41 DB");
			}
			System.out.println("HQ allClient_report retrive" + "_" + fetchCount);

			// Insert the results into the local database
			String sql2 = "INSERT INTO allClient_report (job_no,permit_approval_date,declarant_name,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,first_approval_date,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,origin_con,reference_id,items_id,charge_code,incoterm,division,initials,cancel_count,baseline_request,client_code,user_name, mailbox_id, mailbox_name,supplier_name,supplier_uen) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			logger.info("HQ allClient_report insertQuery Started");

			PreparedStatement statement2 = connection2.prepareStatement(sql2);
			logger.info("HQ allClient_report insertQuery prepareStatement");

			int insertCount = 0;
			for (String[] data : dataList) {
				statement2.setString(1, data[0]);
				statement2.setString(2, data[1]);
				statement2.setString(3, data[2]);
				statement2.setString(4, data[3]);
				statement2.setString(5, data[4]);
				statement2.setString(6, data[5]);
				statement2.setString(7, data[6]);
				statement2.setString(8, data[7]);
				statement2.setString(9, data[8]);
				statement2.setString(10, data[9]);
				statement2.setString(11, data[10]);
				statement2.setString(12, data[11]);
				statement2.setString(13, data[12]);
				statement2.setString(14, data[13]);
				statement2.setString(15, data[14]);
				statement2.setString(16, data[15]);
				statement2.setString(17, data[16]);
				statement2.setString(18, data[17]);
				statement2.setString(19, data[18]);
				statement2.setString(20, data[19]);
				statement2.setString(21, data[20]);
				statement2.setString(22, data[21]);
				statement2.setString(23, data[22]);
				statement2.setString(24, data[23]);
				statement2.setString(25, data[24]);
				statement2.setString(26, data[25]);
				statement2.setString(27, data[26]);
				statement2.setString(28, data[27]);
				statement2.setString(29, data[28]);
				statement2.setString(30, data[29]);
				statement2.setString(31, data[30]);
				statement2.setString(32, data[31]);
				statement2.setString(33, data[32]);
				statement2.setString(34, data[33]);
				statement2.setString(35, data[34]);
				statement2.setString(36, data[35]);
				statement2.setString(37, data[36]);
				statement2.setString(38, data[37]);
				statement2.setString(39, data[38]);
				statement2.setString(40, data[39]);
				statement2.setString(41, data[40]);
				statement2.setString(42, data[41]);
				statement2.setString(43, data[42]);
				statement2.setString(44, data[43]);
				statement2.setString(45, data[44]);
				statement2.setString(46, data[45]);
				statement2.setString(47, data[46]);
				statement2.setString(48, data[47]);
				statement2.setString(49, data[48]);
				statement2.setString(50, data[49]);
				statement2.setString(51, data[50]);
				statement2.setString(52, data[51]);
				statement2.setString(53, data[52]);
				statement2.setString(54, data[53]);
				statement2.setString(55, data[54]);
				statement2.setString(56, data[55]);
				statement2.setString(57, data[56]);
				statement2.setString(58, data[57]);
				statement2.setString(59, data[58]);
				statement2.setString(60, data[59]);
				statement2.setString(61, data[60]);
				statement2.setString(62, data[61]);
				statement2.setString(63, data[62]);
				statement2.setString(64, data[63]);
				statement2.setString(65, data[64]);
				statement2.setString(66, data[65]);
				statement2.setString(67, data[66]);
				statement2.setString(68, data[67]);
				statement2.setString(69, data[68]);
				statement2.setString(70, data[69]);
				statement2.executeUpdate();
				// logger.info("HQ_inserted job_no: {}, permit_number: {}, first_approval_date:
				// {},permit_approval_date: {}, client_code: {}", data[0], data[15],
				// data[16],data[1], data[64]);
			}
			System.out.println("HQ allClient_report inserted count:" + dataList.size()
					+ " data to linux DB executed successfully");

			// Send email notification upon successful completion
			emailService.sendEmailNotification(fetchCount, dataList.size(), "HQ_allClient");
			logger.info("sendEmailNotification");

			connection.close();
			connection2.close();

			logger.info("HQ allClient_report end ");
			System.out.println("HQ allClient_report scheduled completed");

		} catch (SQLException e) {
			System.out.println("HQ Oops, error!");
			e.printStackTrace();
		}
	}

}
