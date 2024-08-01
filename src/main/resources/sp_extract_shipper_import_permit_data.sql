DELIMITER //
CREATE PROCEDURE sp_extract_shipper_import_permit_data(IN first_app_date DATE)
BEGIN
CREATE TEMPORARY TABLE tmp_shipper_data_import
SELECT
  @rownum := @rownum + 1 AS 'row_num',
  di.permit_number AS Permit_Number,
  it.loading_port AS Loading_Port,
  '' AS Country,
  d.forwarder_name AS Forwarder,
   di.first_approval_date AS first_approval_date,
  MONTHNAME(first_approval_date) AS MonthofDay,
  CASE WHEN d.inward_trans_mode = 1 THEN 'SEA' WHEN d.inward_trans_mode = 2 THEN 'RAIL' WHEN d.inward_trans_mode = 3 THEN 'ROAD' WHEN d.inward_trans_mode = 4 THEN 'AIR' WHEN d.inward_trans_mode = 5 THEN 'MAIL' ELSE '' END AS Transport_Mode,
  i.hs_code AS HS_Code,
  ds.tot_gross_wight AS Gross_Weight,
  ds.tot_gross_wight_unit AS Gross_Weight_Unit,
  inv.term_type AS Term_Type,
  CASE WHEN d.packing_type = 5 THEN 'LCL' WHEN d.packing_type = 9 THEN 'FCL' ELSE '' END AS Packaging_Type,
  'IMPORT' AS Permit_Type,
  
  0 AS firstrowflag
FROM
  declaration d,
  declaration_info di,
  declaration_request dr,
  inward_transport it,
  items i,
  invoice inv,
  declaration_summary ds
  JOIN (
    SELECT
      @rownum := 0
  ) r
WHERE
  d.dec_req_id = dr.id
  and dr.request_status = 'APR'
  and dr.dec_id = di.id
  and di.major_type IN ('INP', 'IPT')
  and di.permit_number is not null
  and di.baseline_request = dr.id
  and ds.dec_req_id = dr.id
  and inv.dec_req_id = dr.id
  and i.dec_req_id = dr.id
  AND d.importer_uen = '197701252H'
  and it.dec_req_id = dr.id
 AND  DATE(di.first_approval_date) =  first_app_date;
CREATE TEMPORARY TABLE tmp_first_row_import
SELECT
  MIN(row_num) AS row_num,
  permit_number
FROM
  tmp_shipper_data
GROUP BY
  permit_number;
UPDATE
  tmp_shipper_data_import
  INNER JOIN tmp_first_row_import ON tmp_shipper_data_import.row_num = tmp_first_row_import.row_num
SET
  tmp_shipper_data_import.firstrowflag = 1;
UPDATE
  tmp_shipper_data_import
set
  Gross_Weight = '0'
WHERE
  firstrowflag = '0';
SELECT
  *
FROM
  tmp_shipper_data_import;
DROP
  TABLE tmp_shipper_data_import;
DROP
  TABLE tmp_first_row_import;
END //
DELIMITER ;