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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AllClientOverallReportArc {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/* // ==========================================DC1 shipper_overall_report SchedulerStart==========================================//
  	@Scheduled(cron = "* * * * * *")

public void shipperOverallReportDc1() {
	logger.info("DC1 shipper_overall_report scheduler starting");
	// ----------------------------------ConnectFirstDatabase----------------------------------//
	// Connect first RDS DC1 database
	//String url = "jdbc:mysql://aws2-0-production.c6yaoxud7osg.ap-southeast-1.rds.amazonaws.com:3306/TNETS41_DC1";
	//String username = "admin";
	//String password = "Bi$$Pr$d2023";
	
	// Connect to first local database
	 String url = "jdbc:mysql://localhost:3306/tnets41"; String username = "root"; String password = "root";
	
	

	logger.info("DC1 DB1 connected");

	// ----------------------------------ConnectSecondDatabase----------------------------------//

	// Connect second linux database with tnets_shipper_data DB
	// String url2 = "jdbc:mysql://localhost:3306/tnets_shipper_data"; String username2 = "root"; String password2 = "B!$$@D@t@2022";

	// Connect second linux database with test2 DB
	//String url2 = "jdbc:mysql://localhost:3306/test2";String username2 = "root";String password2 = "B!$$@D@t@2022";

	// Connect second local database
	 String url2 = "jdbc:mysql://localhost:3306/test2"; String username2 = "root"; String password2 = "root";

	
	logger.info("DataLake DB2 connected");

	try {
		Connection connection = DriverManager.getConnection(url, username, password);
		logger.info("DC1 Connection 1 DB successfull");

		Connection connection2 = DriverManager.getConnection(url2, username2, password2);
		logger.info("DC1 Connection 2 DB successfull");

		// Call the stored procedure
		CallableStatement cs = connection.prepareCall("{call a_sp_extract_dc1_shipper_overall_permit_report()}");
		// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
		cs.execute();
		logger.info("DC1 - call The StoreProcedure a_sp_extract_dc1_shipper_overall_permit_report ");

		// Retrieve the results
		ResultSet result = cs.getResultSet();
		List<String[]> dataList = new ArrayList<>();
		logger.info("DC1 data - list The data in the SP");

		int count = 0;
		while (result.next()) {
			String[] data = new String[64];
			data[0] = result.getString("job_no");
			data[1] = result.getString("permit_approval_date");
			data[2] = result.getString("user_id");
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
			data[59] = result.getString("division");
			data[60] = result.getString("initials");
			data[61] = result.getString("cancel_count");
			data[62] = result.getString("baseline_request");
			data[63] = result.getString("client_code");
			dataList.add(data);
			count++;
			
            logger.info("DC1 shipper_overall_report fetched count: " + count + " data from tnets41 DB");
		}
		System.out.println("DC1 shipper_overall_report retrive"+"_"+ count);

		// Insert the results into the local database
		
		String sql2 = "INSERT INTO shipper_overall_report (job_no,permit_approval_date,user_id,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,first_approval_date,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,origin_con,reference_id,items_id,charge_code,division,initials,cancel_count,baseline_request,client_code) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		logger.info("DC1 shipper_overall_report insertQuery Started");

		PreparedStatement statement2 = connection2.prepareStatement(sql2);
		logger.info("DC1 shipper_overall_report_report insertQuery prepareStatement");

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
			statement2.executeUpdate();
			
			logger.info("DC1 inserted job_no: {},  permit_number: {}, first_approval_date: {}, client_code: {}", data[0], data[15], data[16], data[63]);
		}
		System.out.println("DC1 shipper_overall_report inserted count: " + dataList.size() + "data from tnets41 DB data to linux DB executed successfully");
        	
		connection.close();
		// connection2.close();

		logger.info("DC1 shipper_overall_report end ");

	} catch (SQLException e) {
		System.out.println("DC1 shipper_overall_report Oops, error!");
		e.printStackTrace();

	}

}

//==========================================DC1Shipper_overall_reportEnd==========================================//*/
	
	/*// ==========================================Tnets DC1 ARC allClient_overall_report Scheduler Start==========================================

		@Scheduled(cron = "0 19 20 02 8 *")
		public void allClient_overall_reportTnetsDC1_ARC() {
			logger.info("DC1 ARC allClient_overall_report scheduler starting");
			// ----------------------------------ConnectFirstDatabase----------------------------------//

			// Connect first RDS DC1 ARC database
			String url = "jdbc:mysql://aws2-0-production.c6yaoxud7osg.ap-southeast-1.rds.amazonaws.com:3306/TNETS41ARC_DC1"; String username = "admin"; String password = "Bi$$Pr$d2023";
			logger.info("DC1 ARC DB1 connected");

			//----------------------------------ConnectSecondDatabase----------------------------------//

			// Connect second linux database with tnets_shipper_data DB
			String url2 = "jdbc:mysql://localhost:3306/tnets_shipper_data"; String username2 = "root"; String password2 = "B!$$@D@t@2022";

			// Connect second linux database with test2 DB
			logger.info("DataLake DB2 connected");

			try {
				Connection connection = DriverManager.getConnection(url, username, password);
				logger.info("DC1 ARC Connection 1 DB successfull");

				Connection connection2 = DriverManager.getConnection(url2, username2, password2);
				logger.info("DataLake Server Connection 2 DB successfull");

				// Call the stored procedure
				CallableStatement cs = connection.prepareCall("{call a_sp_extract_tnets_allClientArc_overall_report_permit_data()}");
				// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
				cs.execute();
				logger.info("DC1 ARC - callThe a_sp_extract_tnets_allClientArc_overall_report_permit_data SP");

				// Retrieve the results
				ResultSet result = cs.getResultSet();
				List<String[]> dataList = new ArrayList<>();
				logger.info("DC1 ARC data - list The data in the SP");

				int fetchCount = 0;
				while (result.next()) {
					String[] data = new String[64];
					data[0] = result.getString("job_no");
					data[1] = result.getString("permit_approval_date");
					data[2] = result.getString("first_approval_date");
					data[3] = result.getString("declarant_name");
					data[4] = result.getString("dec_type");
					data[5] = result.getString("importer_exporter_uen");
					data[6] = result.getString("importer_exporter_name");
					data[7] = result.getString("mawb_obl");
					data[8] = result.getString("hawb");
					data[9] = result.getString("vessel");
					data[10] = result.getString("aircraft_name");
					data[11] = result.getString("voyage");
					data[12] = result.getString("final_dest_country");
					data[13] = result.getString("country_code");
					data[14] = result.getString("loading_discharge_portName");
					data[15] = result.getString("loading_discharge_Port");
					data[16] = result.getString("permit_number");
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
					data[55] = result.getString("client_code");
					data[56] = result.getString("items_id");
					data[57] = result.getString("cancel_count");
					data[58] = result.getString("origin_con");
					data[59] = result.getString("reference_id");
					data[60] = result.getString("charge_code");
					data[61] = result.getString("incoterm");
					data[62] = result.getString("division");
					data[63] = result.getString("initials");
					dataList.add(data);
					fetchCount++;
					
					//logger.info("allClient_overall_report_report fetched rows from the stored procedure: {}", fetchCount );
		            //logger.info("Fetched permit_number: {}", data[0]);
					logger.info("DC1 ARC allClient_overall_report fetched count: " + fetchCount + " data from tnets41 DB");
				}
				System.out.println("DC1 ARC allClient_overall_report retrive"+"_"+ fetchCount);
				//logger.info("DC1 ARC allClient_overall_report OverAllfetch count: " + fetchCount + " data from tnets41 DB");
				//logger.info("DC1 ARC allClient_overall_report retrive");

				// Insert the results into the local database
				String sql2 = "INSERT INTO allClient_report_overall (job_no,permit_approval_date,first_approval_date,declarant_name,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,client_code,items_id,cancel_count,origin_con,reference_id,charge_code,incoterm,division,initials)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				logger.info("DC1 ARC allClient_overall_report insertQuery Started");

				PreparedStatement statement2 = connection2.prepareStatement(sql2);
				logger.info("DC1 ARC allClient_overall_report insertQuery prepareStatement");

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
					statement2.executeUpdate();				
					logger.info("inserted job_no: {}, permit_number: {}, first_approval_date: {}, client_code: {}", data[0], data[16], data[2], data[55]);
				}
				System.out.println("DC1 ARC allClient_overall_report inserted count:" + dataList.size() + " data to linux DB executed successfully");

				connection.close();
				// connection2.close();

				logger.info("DC1 ARC allClient_overall_report end ");

				System.out.println("DC1 ARC allClient_overall_report scheduled completed");
			} catch (SQLException e) {
				System.out.println("DC1 ARC Oops, error!");
				e.printStackTrace();

			}

		}

	// ==========================================DC1 ARC allClient Scheduler End==========================================//*/
		/*// ==========================================Tnets DC2 allClient_overall_report Scheduler Start==========================================

		@Scheduled(cron = "0 50 11 05 7 *")
		public void allClient_overall_reportTnetsDC2() {
			logger.info("DC2 allClient_overall_report scheduler starting");
			// ----------------------------------ConnectFirstDatabase----------------------------------//

			// Connect first RDS DC2 database
			String url = "jdbc:mysql://aws2-0-production.c6yaoxud7osg.ap-southeast-1.rds.amazonaws.com:3306/TNETS41_DC2"; String username = "admin"; String password = "Bi$$Pr$d2023";
			logger.info("DC2 DB1 connected");

			//----------------------------------ConnectSecondDatabase----------------------------------//

			// Connect second linux database with tnets_shipper_data DB
			String url2 = "jdbc:mysql://localhost:3306/tnets_shipper_data"; String username2 = "root"; String password2 = "B!$$@D@t@2022";

			// Connect second linux database with test2 DB
			logger.info("DataLake DB2 connected");

			try {
				Connection connection = DriverManager.getConnection(url, username, password);
				logger.info("DC2 Connection 1 DB successfull");

				Connection connection2 = DriverManager.getConnection(url2, username2, password2);
				logger.info("DataLake Server Connection 2 DB successfull");

				// Call the stored procedure
				CallableStatement cs = connection.prepareCall("{call a_sp_extract_tnets_allClient_overall_report_permit_data()}");
				// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
				cs.execute();
				logger.info("DC2 - callThe a_sp_extract_tnets_allClient_overall_report_permit_data SP");

				// Retrieve the results
				ResultSet result = cs.getResultSet();
				List<String[]> dataList = new ArrayList<>();
				logger.info("DC2 data - list The data in the SP");

				int fetchCount = 0;
				while (result.next()) {
					String[] data = new String[66];
					data[0] = result.getString("job_no");
					data[1] = result.getString("permit_approval_date");
					data[2] = result.getString("first_approval_date");
					data[3] = result.getString("declarant_name");
					data[4] = result.getString("dec_type");
					data[5] = result.getString("importer_exporter_uen");
					data[6] = result.getString("importer_exporter_name");
					data[7] = result.getString("mawb_obl");
					data[8] = result.getString("hawb");
					data[9] = result.getString("vessel");
					data[10] = result.getString("aircraft_name");
					data[11] = result.getString("voyage");
					data[12] = result.getString("final_dest_country");
					data[13] = result.getString("country_code");
					data[14] = result.getString("loading_discharge_portName");
					data[15] = result.getString("loading_discharge_Port");
					data[16] = result.getString("permit_number");
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
					data[55] = result.getString("client_code");
					data[56] = result.getString("items_id");
					data[57] = result.getString("cancel_count");
					data[58] = result.getString("origin_con");
					data[59] = result.getString("reference_id");
					data[60] = result.getString("remarks1");
					data[61] = result.getString("remarks2");
					data[62] = result.getString("remarks_charge");
					data[63] = result.getString("remarks_div");
					data[64] = result.getString("remarks_term");
					dataList.add(data);
					fetchCount++;
					
					//logger.info("allClient_overall_report_report fetched rows from the stored procedure: {}", fetchCount );
		            //logger.info("Fetched permit_number: {}", data[0]);
					logger.info("DC2 allClient_overall_report fetched count: " + fetchCount + " data from tnets41 DB");
				}
				System.out.println("DC2 allClient_overall_report retrive"+"_"+ fetchCount);
				//logger.info("DC2 allClient_overall_report OverAllfetch count: " + fetchCount + " data from tnets41 DB");
				//logger.info("DC2 allClient_overall_report retrive");

				// Insert the results into the local database
				String sql2 = "INSERT INTO allClient_report_overall (job_no,permit_approval_date,first_approval_date,declarant_name,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,client_code,items_id,cancel_count,origin_con,reference_id,remarks1,remarks2,remarks_charge,remarks_div,remarks_term)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				logger.info("DC2 allClient_overall_report insertQuery Started");

				PreparedStatement statement2 = connection2.prepareStatement(sql2);
				logger.info("DC2 allClient_overall_report insertQuery prepareStatement");

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
					statement2.executeUpdate();				
					logger.info("inserted job_no: {}, permit_number: {}, first_approval_date: {}, client_code: {}", data[0], data[16], data[2], data[55]);
				}
				System.out.println("DC2 allClient_overall_report inserted count:" + dataList.size() + " data to linux DB executed successfully");

				connection.close();
				// connection2.close();

				logger.info("DC2 allClient_overall_report end ");

				System.out.println("DC2 allClient_overall_report scheduled completed");
			} catch (SQLException e) {
				System.out.println("DC2 Oops, error!");
				e.printStackTrace();

			}

		}

	// ==========================================DC2 allClient Scheduler End==========================================//*/
		
		/*// ==========================================Tnets DC2 ARC allClient_overall_report Scheduler Start==========================================

		@Scheduled(cron = "0 36 19 02 8 *")
		public void allClient_overall_reportTnetsDC2_ARC() {
			logger.info("DC2 ARC allClient_overall_report scheduler starting");
			// ----------------------------------ConnectFirstDatabase----------------------------------//

			// Connect first RDS DC2 ARC database
			String url = "jdbc:mysql://aws2-0-production.c6yaoxud7osg.ap-southeast-1.rds.amazonaws.com:3306/TNETS41ARC_DC2"; String username = "admin"; String password = "Bi$$Pr$d2023";
			logger.info("DC2 ARC DB1 connected");

			//----------------------------------ConnectSecondDatabase----------------------------------//

			// Connect second linux database with tnets_shipper_data DB
			String url2 = "jdbc:mysql://localhost:3306/tnets_shipper_data"; String username2 = "root"; String password2 = "B!$$@D@t@2022";

			// Connect second linux database with test2 DB
			logger.info("DataLake DB2 connected");

			try {
				Connection connection = DriverManager.getConnection(url, username, password);
				logger.info("DC2 ARC Connection 1 DB successfull");

				Connection connection2 = DriverManager.getConnection(url2, username2, password2);
				logger.info("DataLake Server Connection 2 DB successfull");

				// Call the stored procedure
				CallableStatement cs = connection.prepareCall("{call a_sp_extract_tnets_allClientArc_overall_report_permit_data()}");
				// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
				cs.execute();
				logger.info("DC2 ARC - callThe a_sp_extract_tnets_allClientArc_overall_report_permit_data SP");

				// Retrieve the results
				ResultSet result = cs.getResultSet();
				List<String[]> dataList = new ArrayList<>();
				logger.info("DC2 ARC data - list The data in the SP");

				int fetchCount = 0;
				while (result.next()) {
					String[] data = new String[64];
					data[0] = result.getString("job_no");
					data[1] = result.getString("permit_approval_date");
					data[2] = result.getString("first_approval_date");
					data[3] = result.getString("declarant_name");
					data[4] = result.getString("dec_type");
					data[5] = result.getString("importer_exporter_uen");
					data[6] = result.getString("importer_exporter_name");
					data[7] = result.getString("mawb_obl");
					data[8] = result.getString("hawb");
					data[9] = result.getString("vessel");
					data[10] = result.getString("aircraft_name");
					data[11] = result.getString("voyage");
					data[12] = result.getString("final_dest_country");
					data[13] = result.getString("country_code");
					data[14] = result.getString("loading_discharge_portName");
					data[15] = result.getString("loading_discharge_Port");
					data[16] = result.getString("permit_number");
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
					data[55] = result.getString("client_code");
					data[56] = result.getString("items_id");
					data[57] = result.getString("cancel_count");
					data[58] = result.getString("origin_con");
					data[59] = result.getString("reference_id");
					data[60] = result.getString("charge_code");
					data[61] = result.getString("incoterm");
					data[62] = result.getString("division");
					data[63] = result.getString("initials");
					dataList.add(data);
					fetchCount++;
					
					//logger.info("allClient_overall_report_report fetched rows from the stored procedure: {}", fetchCount );
		            //logger.info("Fetched permit_number: {}", data[0]);
					logger.info("DC2 ARC allClient_overall_report fetched count: " + fetchCount + " data from tnets41 DB");
				}
				System.out.println("DC2 ARC allClient_overall_report retrive"+"_"+ fetchCount);
				//logger.info("DC2 ARC allClient_overall_report OverAllfetch count: " + fetchCount + " data from tnets41 DB");
				//logger.info("DC2 ARC allClient_overall_report retrive");

				// Insert the results into the local database
				String sql2 = "INSERT INTO allClient_report_overall (job_no,permit_approval_date,first_approval_date,declarant_name,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,client_code,items_id,cancel_count,origin_con,reference_id,charge_code,incoterm,division,initials)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				logger.info("DC2 ARC allClient_overall_report insertQuery Started");

				PreparedStatement statement2 = connection2.prepareStatement(sql2);
				logger.info("DC2 ARC allClient_overall_report insertQuery prepareStatement");

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
					statement2.executeUpdate();				
					logger.info("inserted job_no: {}, permit_number: {}, first_approval_date: {}, client_code: {}", data[0], data[16], data[2], data[55]);
				}
				System.out.println("DC2 ARC allClient_overall_report inserted count:" + dataList.size() + " data to linux DB executed successfully");

				connection.close();
				// connection2.close();

				logger.info("DC2 ARC allClient_overall_report end ");

				System.out.println("DC2 ARC allClient_overall_report scheduled completed");
			} catch (SQLException e) {
				System.out.println("DC2 ARC Oops, error!");
				e.printStackTrace();

			}

		}

	// ==========================================DC2 ARC allClient Scheduler End==========================================//*/
		

/*// ==========================================Tnets HQ allClient_overall Scheduler Start==========================================

		@Scheduled(cron = "0 33 22 04 7 *")
		public void allClientOverallTnetsHQ() {
			logger.info("HQ allClient_overall scheduler starting");
			// ----------------------------------ConnectFirstDatabase----------------------------------//
			// Connect to first local database
			// String url = "jdbc:mysql://localhost:3306/tnets41"; String username = "root"; String password = "root";
			
			// Connect first RDS DC1 database
			String url = "jdbc:mysql://aws2-0-production.c6yaoxud7osg.ap-southeast-1.rds.amazonaws.com:3306/TNETS41"; String username = "admin"; String password = "Bi$$Pr$d2023";
			logger.info("HQ DB1 connected");

			//----------------------------------ConnectSecondDatabase----------------------------------//

			// Connect second local database
			// String url2 = "jdbc:mysql://localhost:3306/test2"; String username2 = "root"; String password2 = "root";

			// Connect second linux database with tnets_shipper_data DB
			String url2 = "jdbc:mysql://localhost:3306/tnets_shipper_data"; String username2 = "root"; String password2 = "B!$$@D@t@2022";

			// Connect second linux database with test2 DB
			//String url2 = "jdbc:mysql://localhost:3306/test2"; String username2 = "root"; String password2 = "B!$$@D@t@2022";
			logger.info("DataLake DB2 connected");

			try {
				Connection connection = DriverManager.getConnection(url, username, password);
				logger.info("HQ Connection 1 DB successfull");

				Connection connection2 = DriverManager.getConnection(url2, username2, password2);
				logger.info("DataLake Server Connection 2 DB successfull");

				// Call the stored procedure
				CallableStatement cs = connection.prepareCall("{call a_sp_extract_tnets_allClient_overall_permit_data()}");
				// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
				cs.execute();
				logger.info("HQ - callThe a_sp_extract_tnets_allClient_overall_permit_data SP");

				// Retrieve the results
				ResultSet result = cs.getResultSet();
				List<String[]> dataList = new ArrayList<>();
				logger.info("HQ data - list The data in the SP");

				int fetchCount = 0;
			while (result.next()) {
				String[] data = new String[65];
				data[0] = result.getString("job_no");
				data[1] = result.getString("permit_approval_date");
				data[2] = result.getString("first_approval_date");
				data[3] = result.getString("declarant_name");
				data[4] = result.getString("dec_type");
				data[5] = result.getString("importer_exporter_uen");
				data[6] = result.getString("importer_exporter_name");
				data[7] = result.getString("mawb_obl");
				data[8] = result.getString("hawb");
				data[9] = result.getString("vessel");
				data[10] = result.getString("aircraft_name");
				data[11] = result.getString("voyage");
				data[12] = result.getString("final_dest_country");
				data[13] = result.getString("country_code");
				data[14] = result.getString("loading_discharge_portName");
				data[15] = result.getString("loading_discharge_Port");
				data[16] = result.getString("permit_number");
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
				data[55] = result.getString("client_code");
				data[56] = result.getString("items_id");
				data[57] = result.getString("cancel_count");
				data[58] = result.getString("origin_con");
				data[59] = result.getString("reference_id");
				data[60] = result.getString("remarks1");
				data[61] = result.getString("remarks2");
				data[62] = result.getString("remarks_charge");
				data[63] = result.getString("remarks_div");
				data[64] = result.getString("remarks_term");
				dataList.add(data);
				fetchCount++;
				
				//logger.info("allClient_overall_report_report fetched rows from the stored procedure: {}", fetchCount );
	            //logger.info("Fetched permit_number: {}", data[0]);
				logger.info("DC2 allClient_overall_report fetched count: " + fetchCount + " data from tnets41 DB");
			}
			System.out.println("DC2 allClient_overall_report retrive"+"_"+ fetchCount);
			//logger.info("DC2 allClient_overall_report OverAllfetch count: " + fetchCount + " data from tnets41 DB");
			//logger.info("DC2 allClient_overall_report retrive");

			// Insert the results into the local database
			String sql2 = "INSERT INTO allClient_report_overall (job_no,permit_approval_date,first_approval_date,declarant_name,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,client_code,items_id,cancel_count,origin_con,reference_id,remarks1,remarks2,remarks_charge,remarks_div,remarks_term)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			logger.info("DC2 allClient_overall_report insertQuery Started");

			PreparedStatement statement2 = connection2.prepareStatement(sql2);
			logger.info("DC2 allClient_overall_report insertQuery prepareStatement");

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
				statement2.executeUpdate();				
				logger.info("inserted job_no: {}, permit_number: {}, first_approval_date: {}, client_code: {}", data[0], data[16], data[2], data[55]);
				}
				System.out.println("HQ allClient_overall_report Total inserted count: " + dataList.size() + " data to linux DB executed successfully");

				connection.close();
				// connection2.close();

				logger.info("HQ allClient_overall_report end ");

				System.out.println("HQ allClient_overall_report scheduled completed");
			} catch (SQLException e) {
				System.out.println("HQ Oops, error!");
				e.printStackTrace();

			}

		}

	// ==========================================HQ allClient_overall Scheduler End==========================================//*/	

		/*// ==========================================Tnets HQ allClientArc_overall Scheduler Start==========================================

		@Scheduled(cron = "0 25 14 02 8 *")
		public void allClientOverallTnetsHqArc() {
			logger.info("HQ ARC allClientArc_overall scheduler starting");
			// ----------------------------------ConnectFirstDatabase----------------------------------//
			
			// Connect first RDS DC1 database
			String url = "jdbc:mysql://aws2-0-production.c6yaoxud7osg.ap-southeast-1.rds.amazonaws.com:3306/TNETS41ARC"; String username = "admin"; String password = "Bi$$Pr$d2023";
			logger.info("HQ ARC DB1 connected");

			//----------------------------------ConnectSecondDatabase----------------------------------//

			// Connect second linux database with tnets_shipper_data DB
			String url2 = "jdbc:mysql://localhost:3306/tnets_shipper_data"; String username2 = "root"; String password2 = "B!$$@D@t@2022";

			logger.info("DataLake DB2 connected");

			try {
				Connection connection = DriverManager.getConnection(url, username, password);
				logger.info("HQ ARC Connection 1 DB successfull");

				Connection connection2 = DriverManager.getConnection(url2, username2, password2);
				logger.info("DataLake Server Connection 2 DB successfull");

				// Call the stored procedure
				CallableStatement cs = connection.prepareCall("{call a_sp_extract_tnets_allClientArc_overall_report_permit_data()}");
				// cs.setDate(1, java.sql.Date.valueOf("2022-01-01"));
				cs.execute();
				logger.info("HQ ARC - callThe a_sp_extract_tnets_allClientArc_overall_report_permit_data SP");
				// Retrieve the results
				ResultSet result = cs.getResultSet();
				List<String[]> dataList = new ArrayList<>();
				logger.info("HQ ARC data - list The data in the SP");

				int fetchCount = 0;
			while (result.next()) {
				String[] data = new String[64];
				data[0] = result.getString("job_no");
				data[1] = result.getString("permit_approval_date");
				data[2] = result.getString("first_approval_date");
				data[3] = result.getString("declarant_name");
				data[4] = result.getString("dec_type");
				data[5] = result.getString("importer_exporter_uen");
				data[6] = result.getString("importer_exporter_name");
				data[7] = result.getString("mawb_obl");
				data[8] = result.getString("hawb");
				data[9] = result.getString("vessel");
				data[10] = result.getString("aircraft_name");
				data[11] = result.getString("voyage");
				data[12] = result.getString("final_dest_country");
				data[13] = result.getString("country_code");
				data[14] = result.getString("loading_discharge_portName");
				data[15] = result.getString("loading_discharge_Port");
				data[16] = result.getString("permit_number");
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
				data[55] = result.getString("client_code");
				data[56] = result.getString("items_id");
				data[57] = result.getString("cancel_count");
				data[58] = result.getString("origin_con");
				data[59] = result.getString("reference_id");
				data[60] = result.getString("charge_code");
				data[61] = result.getString("incoterm");
				data[62] = result.getString("division");
				data[63] = result.getString("initials");
				dataList.add(data);
				fetchCount++;
				
				//logger.info("allClientArc_overall_report_report fetched rows from the stored procedure: {}", fetchCount );
	            //logger.info("Fetched permit_number: {}", data[0]);
				logger.info("HQ allClientArc_overall_report fetched count: " + fetchCount + " data from tnets41 DB");
			}
			System.out.println("HQ allClientArc_overall_report retrive"+"_"+ fetchCount);
			//logger.info("HQ allClientArc_overall_report OverAllfetch count: " + fetchCount + " data from tnets41 DB");
			//logger.info("HQ allClientArc_overall_report retrive");

			// Insert the results into the local database
			String sql2 = "INSERT INTO allClient_report_overall (job_no,permit_approval_date,first_approval_date,declarant_name,dec_type,importer_exporter_uen,importer_exporter_name,mawb_obl,hawb,vessel,aircraft_name,voyage,final_dest_country,country_code,loading_discharge_portName,loading_discharge_Port,permit_number,arrival_departure_date,dec_agent_name,dec_agent_uen,forwarder_name,forwarder_uen,aed_permit_condition,tot_items,amend_reason,aed_status,cif_fob_value,tot_gross_wight,tot_gross_wight_unit,tot_gst,tot_outer_pack,tot_outer_pack_unit,term_type,packaging_type,late_by,late_grouping,apr_month,transport_mode,flight_id,freight_category,shipment_category,permit_type,message_type,request_type,gst_report_category,gst_f5_return,equipment_id,seal_id,seq_num,size_type,weight,customs_duty_amount,hs_code,hs_description,items_gst_amount,client_code,items_id,cancel_count,origin_con,reference_id,charge_code,incoterm,division,initials)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			logger.info("HQ allClientArc_overall_report insertQuery Started");

			PreparedStatement statement2 = connection2.prepareStatement(sql2);
			logger.info("HQ allClientArc_overall_report insertQuery prepareStatement");

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
				statement2.executeUpdate();				
				logger.info("inserted job_no: {}, permit_number: {}, first_approval_date: {}, client_code: {}", data[0], data[16], data[2], data[55]);
				}
				System.out.println("HQ allClientArc_overall_report Total inserted count: " + dataList.size() + " data to linux DB executed successfully");

				connection.close();
				// connection2.close();

				logger.info("HQ allClientArc_overall_report end ");

				System.out.println("HQ allClientArc_overall_report scheduled completed");
			} catch (SQLException e) {
				System.out.println("HQ ARC Oops, error!");
				e.printStackTrace();

			}

		}

	// ==========================================HQ allClientArc_overall Scheduler End==========================================//*/
		

}
