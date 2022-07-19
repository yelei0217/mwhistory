/*      */ package com.kingdee.eas.custom.app;
/*      */ import com.kingdee.bos.BOSException;
/*      */ import com.kingdee.bos.Context;
/*      */ import com.kingdee.bos.dao.IObjectPK;
/*      */ import com.kingdee.bos.dao.IObjectValue;
/*      */ import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
/*      */ import com.kingdee.bos.metadata.entity.EntityViewInfo;
/*      */ import com.kingdee.bos.metadata.entity.FilterInfo;
/*      */ import com.kingdee.bos.metadata.entity.FilterItemInfo;
/*      */ import com.kingdee.bos.metadata.query.util.CompareType;
/*      */ import com.kingdee.bos.util.BOSUuid;
/*      */ import com.kingdee.eas.base.permission.UserFactory;
/*      */ import com.kingdee.eas.base.permission.UserInfo;
/*      */ import com.kingdee.eas.basedata.assistant.CurrencyFactory;
/*      */ import com.kingdee.eas.basedata.assistant.CurrencyInfo;
/*      */ import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
/*      */ import com.kingdee.eas.basedata.assistant.PaymentTypeFactory;
/*      */ import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
/*      */ import com.kingdee.eas.basedata.assistant.SettlementTypeFactory;
/*      */ import com.kingdee.eas.basedata.assistant.SettlementTypeInfo;
/*      */ import com.kingdee.eas.basedata.master.account.AccountViewFactory;
/*      */ import com.kingdee.eas.basedata.master.account.AccountViewInfo;
/*      */ import com.kingdee.eas.basedata.master.auxacct.AsstActTypeFactory;
/*      */ import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
/*      */ import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
/*      */ import com.kingdee.eas.basedata.master.material.IMaterial;
/*      */ import com.kingdee.eas.basedata.master.material.MaterialCollection;
/*      */ import com.kingdee.eas.basedata.master.material.MaterialFactory;
/*      */ import com.kingdee.eas.basedata.master.material.MaterialInfo;
/*      */ import com.kingdee.eas.basedata.org.AdminOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.AdminOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.CostCenterOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.CostCenterOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.PurchaseOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.person.PersonFactory;
/*      */ import com.kingdee.eas.basedata.person.PersonInfo;
/*      */ import com.kingdee.eas.basedata.scm.common.BillTypeInfo;
/*      */ import com.kingdee.eas.basedata.scm.common.BizTypeFactory;
/*      */ import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
/*      */ import com.kingdee.eas.basedata.scm.common.RowTypeFactory;
/*      */ import com.kingdee.eas.basedata.scm.common.RowTypeInfo;
/*      */ import com.kingdee.eas.basedata.scm.sm.pur.DemandTypeInfo;
/*      */ import com.kingdee.eas.common.EASBizException;
/*      */ import com.kingdee.eas.cp.bc.ExpenseTypeFactory;
/*      */ import com.kingdee.eas.cp.bc.ExpenseTypeInfo;
/*      */ import com.kingdee.eas.custom.EAISynTemplate;
/*      */ import com.kingdee.eas.custom.app.insertbill.VSPJDSupport;
/*      */ import com.kingdee.eas.custom.app.unit.AppUnit;
/*      */ import com.kingdee.eas.custom.app.unit.PayRequestBillUtil;
/*      */ import com.kingdee.eas.custom.app.unit.SupplyInfoLogUnit;
/*      */ import com.kingdee.eas.custom.util.DBUtil;
/*      */ import com.kingdee.eas.custom.util.VerifyUtil;
/*      */ import com.kingdee.eas.fi.ap.IPayRequestBill;
/*      */ import com.kingdee.eas.fi.ap.OtherBillFactory;
/*      */ import com.kingdee.eas.fi.ap.OtherBillInfo;
/*      */ import com.kingdee.eas.fi.ap.OtherBillPlanInfo;
/*      */ import com.kingdee.eas.fi.ap.OtherBillType;
/*      */ import com.kingdee.eas.fi.ap.OtherBillentryInfo;
/*      */ import com.kingdee.eas.fi.ap.PayRequestBillEntryCollection;
/*      */ import com.kingdee.eas.fi.ap.PayRequestBillEntryInfo;
/*      */ import com.kingdee.eas.fi.ap.PayRequestBillFactory;
/*      */ import com.kingdee.eas.fi.ap.PayRequestBillInfo;
/*      */ import com.kingdee.eas.fi.ap.VerificateBillTypeEnum;
/*      */ import com.kingdee.eas.fi.ar.ArApBillBaseInfo;
/*      */ import com.kingdee.eas.fi.ar.BillStatusEnum;
///*      */ import com.kingdee.eas.fi.cas.BillStatusEnum;
/*      */ import com.kingdee.eas.fi.cas.CasRecPayBillTypeEnum;
/*      */ import com.kingdee.eas.fi.cas.PaymentBillEntryInfo;
/*      */ import com.kingdee.eas.fi.cas.PaymentBillFactory;
/*      */ import com.kingdee.eas.fi.cas.PaymentBillInfo;
/*      */ import com.kingdee.eas.fi.cas.PaymentBillTypeFactory;
/*      */ import com.kingdee.eas.fi.cas.PaymentBillTypeInfo;
/*      */ import com.kingdee.eas.fi.cas.SourceTypeEnum;
/*      */ import com.kingdee.eas.framework.CoreBaseInfo;
/*      */ import com.kingdee.eas.framework.CoreBillBaseInfo;
/*      */ import com.kingdee.eas.scm.common.PurchaseTypeEnum;
/*      */ import com.kingdee.eas.scm.sm.pur.PurRequestEntryInfo;
/*      */ import com.kingdee.eas.scm.sm.pur.PurRequestFactory;
/*      */ import com.kingdee.eas.scm.sm.pur.PurRequestInfo;
/*      */ import com.kingdee.eas.util.app.DbUtil;
/*      */ import com.kingdee.jdbc.rowset.IRowSet;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import org.apache.log4j.Logger;
/*      */ 
/*      */ public class BusinessFormOAControllerBean extends AbstractBusinessFormOAControllerBean {
/*  105 */   private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.BusinessFormOAControllerBean");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String _ApOtherFormOA(Context ctx, String database) throws BOSException {
/*  116 */     String sql = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  125 */     updateNoPeople(ctx, database);
/*      */     
/*  127 */     updateNoExpenseType(ctx, database);
/*  128 */     sql = " select bx.id,bx.fnumber,bx.bizDate,bx.isLoan,bx.payType,bx.isrentalfee,bx.company,bx.Dept,bx.supplierid,bx.Yhzh,bx.Khh,bx.applyer,  bx.Applyerbank,bx.Applyerbanknum,bx.Agency,bx.Amount,bx.Jsfs,bx.purchType,bx.purchModel,bx.Paystate,bx.Paystatetime  from eas_lolkk_bx bx  where bx.eassign = 0 and bx.PURCHTYPE != '08' ";
/*      */ 
/*      */ 
/*      */     
/*  132 */     Calendar cal = Calendar.getInstance();
/*  133 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  134 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, sql.toString());
/*  135 */     System.out.println("--------------------------" + list.size());
/*  136 */     for (Map<String, Object> map : list) {
/*      */       
/*  138 */       if (map.get("PURCHTYPE") != null && !map.get("PURCHTYPE").toString().equals("03")) {
/*  139 */         apOtherNotShichang(ctx, database, map); continue;
/*  140 */       }  if (map.get("PURCHTYPE") != null && map.get("PURCHTYPE").toString().equals("03")) {
/*  141 */         apOtherIsShichang(ctx, database, map);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  146 */     return super._ApOtherFormOA(ctx, database);
/*      */   }
/*      */   
/*      */   private void updateNoExpenseType(Context ctx, String database) {
/*  150 */     String updateSql = "UPDATE  eas_lolkk_bx  set eassign = 2 , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='分录上某个费用类型禁用'  where ID in (select bx.id from eas_lolkk_bx bx left JOIN eas_lolkk_bx_sub sub  on sub.parentid = bx.id  left JOIN EAS_PAYTYPE_OA_MIDTABLE paytype  on paytype.fnumber = sub.paytypecode where bx.eassign = 0 and paytype.fstatus = 2 )";
/*      */ 
/*      */     
/*  153 */     System.out.print("--------------" + updateSql);
/*      */     try {
/*  155 */       EAISynTemplate.execute(ctx, database, updateSql);
/*  156 */     } catch (BOSException e) {
/*  157 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String _PayApplyToOA(Context ctx, String database, String billId) throws BOSException {
/*  170 */     String result = "";
/*  171 */     if (!isExistsByBillId(ctx, database, billId).booleanValue()) {
/*      */       try {
/*  173 */         PayRequestBillInfo payRequestBillInfo = 
/*  174 */           PayRequestBillFactory.getLocalInstance(ctx).getPayRequestBillInfo(
/*  175 */             (IObjectPK)new ObjectUuidPK(billId));
/*  176 */         PayRequestBillEntryCollection entryCollection = payRequestBillInfo
/*  177 */           .getEntrys();
/*  178 */         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  179 */         String id = payRequestBillInfo.getId().toString();
/*      */         
/*  181 */         String fnumber = payRequestBillInfo.getNumber();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  188 */         String bizDate = sdf.format(payRequestBillInfo.getBizDate());
/*      */         
/*  190 */         String Formtype = "6";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  197 */         String Suppliernum = "";
/*  198 */         String Suppliername = "";
/*  199 */         String Supplierbank = "";
/*  200 */         String Supplierbanknum = "";
/*  201 */         String prex = "";
/*  202 */         boolean isPrcosess = false;
/*  203 */         MaterialInfo materialInfo = null;
/*  204 */         if (entryCollection.size() > 0) {
/*  205 */           PayRequestBillEntryInfo entryOne = entryCollection.get(0);
/*  206 */           materialInfo = entryOne.getMaterialNo();
/*  207 */           if (materialInfo != null && materialInfo.getId() != null && !"".equals(materialInfo.getId().toString()))
/*      */           {
/*  209 */             isPrcosess = AppUnit.isProcessMaterialByID(ctx, materialInfo.getId().toString());
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  223 */           Suppliernum = entryOne.getAsstActNumber();
/*  224 */           Suppliername = entryOne.getAsstActName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  240 */           Supplierbank = entryOne.getAccountBank();
/*  241 */           Supplierbanknum = entryOne.getAccountBankNo();
/*      */         } 
/*      */         
/*  244 */         String Usedate = "";
/*      */         
/*  246 */         UserInfo uInfo = UserFactory.getLocalInstance(ctx).getUserInfo(
/*  247 */             (IObjectPK)new ObjectUuidPK(
/*  248 */               payRequestBillInfo.getCreator().getId()));
/*      */         
/*  250 */         CompanyOrgUnitInfo couInfo = 
/*  251 */           CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo(
/*  252 */             (IObjectPK)new ObjectUuidPK(
/*  253 */               payRequestBillInfo.getCompany().getId()));
/*  254 */         String Companynum = couInfo.getId().toString();
/*  255 */         String Company = couInfo.getName();
/*  256 */         String Gzamount = payRequestBillInfo.getRequestAmount()
/*  257 */           .toString();
/*  258 */         String requestAmount = payRequestBillInfo.getAuditAmount().toString();
/*  259 */         String invoiceNumber = (payRequestBillInfo.get("fapiaohao") != null) ? payRequestBillInfo.get("fapiaohao").toString() : "";
/*  260 */         String purpose = (payRequestBillInfo.getDescription() != null) ? payRequestBillInfo.getDescription() : "";
/*      */         
/*  262 */         SettlementTypeInfo stInfo = 
/*  263 */           SettlementTypeFactory.getLocalInstance(ctx)
/*  264 */           .getSettlementTypeInfo(
/*  265 */             (IObjectPK)new ObjectUuidPK(
/*  266 */               "e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
/*  267 */         String Jsfs = stInfo.getNumber();
/*  268 */         String Eassign = "0";
/*  269 */         String Eastime = sdf.format(new Date());
/*  270 */         String oasign = "0";
/*  271 */         String Oatime = sdf.format(new Date());
/*  272 */         String applyer = null;
/*  273 */         String personid = "";
/*  274 */         String requestReason = (payRequestBillInfo.getRequestReason() != null) ? payRequestBillInfo.getRequestReason() : "";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  280 */         PersonInfo person = null;
/*      */ 
/*      */         
/*  283 */         if (payRequestBillInfo.getString("oacaigoushenqingdandanhao") != null && !"".equals(payRequestBillInfo.getString("oacaigoushenqingdandanhao"))) {
/*  284 */           Map map1 = AppUnit.getPurchModelFromOANum(ctx, payRequestBillInfo.getString("oacaigoushenqingdandanhao"));
/*  285 */           if (map1.get("person") != null && !"".equals(map1.get("person"))) {
/*  286 */             personid = (String)map1.get("person");
/*  287 */             if (!PayRequestBillUtil.queryPersonStatus(ctx, personid))
/*  288 */               personid = payRequestBillInfo.getApplyer().getId().toString(); 
/*      */           } 
/*      */         } else {
/*  291 */           personid = payRequestBillInfo.getApplyer().getId().toString();
/*      */         } 
/*  293 */         person = PersonFactory.getLocalInstance(ctx).getPersonInfo((IObjectPK)new ObjectUuidPK(personid));
/*  294 */         applyer = person.getNumber();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  325 */         List<Map<String, String>> entrys = new ArrayList<Map<String, String>>();
/*  326 */         List<Map<String, String>> oanumbers = AppUnit.getSumPayRequestBillEntrys(ctx, id);
/*  327 */         String type = "";
/*  328 */         if (oanumbers != null && oanumbers.size() > 0) {
/*  329 */           for (Map<String, String> mp : oanumbers) {
/*  330 */             Map<String, String> map = new HashMap<String, String>();
/*  331 */             map.put("parentID", id);
/*  332 */             map.put("fnumber", ((String)mp.get("oanumber")).toString());
/*  333 */             map.put("Companynum", couInfo.getId().toString());
/*  334 */             map.put("Suppliername", couInfo.getName());
/*  335 */             map.put("requestAmount", ((String)mp.get("amount")).toString());
/*      */             
/*  337 */             type = AppUnit.getPurTypeByOANumber(((String)mp.get("oanumber")).toString(), isPrcosess);
/*  338 */             map.put("formtypezhi", type);
/*  339 */             Formtype = type;
/*  340 */             entrys.add(map);
/*      */           } 
/*      */         }
/*  343 */         String sql = "insert into eas_lolkk_fk (id,fnumber,bizDate,Formtype,InvoiceNumber,Suppliernum,Suppliername,Supplierbank,Supplierbanknum,Usedate,applyer,Companynum,Company,Gzamount,requestAmount,purpose,Jsfs,requestReason,Eassign,Eastime,oasign,Oatime,oafinishsign,paystate) values('" + 
/*  344 */           id + "','" + fnumber + "','" + bizDate + "','" + Formtype + "','" + invoiceNumber + "','" + 
/*  345 */           Suppliernum + "','" + Suppliername + "','" + Supplierbank + "','" + Supplierbanknum + "','" + 
/*  346 */           Usedate + "','" + applyer + "','" + Companynum + "','" + Company + "','" + 
/*  347 */           Gzamount + "'," + requestAmount + ",'" + purpose + "','" + 
/*  348 */           Jsfs + "','" + requestReason + "'," + Eassign + ",'" + Eastime + "'," + oasign + ",'" + Oatime + "',0,0)";
/*      */         
/*  350 */         String sqlEntry = "";
/*  351 */         for (int i = 0; i < entrys.size(); i++) {
/*  352 */           Map<String, String> map = entrys.get(i);
/*  353 */           sqlEntry = "insert into eas_lolkk_fk_sub(parentID,fnumber,Companynum,Companyname,requestAmount,formtypezhi) values('" + 
/*  354 */             (String)map.get("parentID") + 
/*  355 */             "','" + 
/*  356 */             (String)map.get("fnumber") + 
/*  357 */             "','" + 
/*  358 */             (String)map.get("Companynum") + 
/*  359 */             "','" + 
/*  360 */             (String)map.get("Suppliername") + 
/*  361 */             "'," + 
/*  362 */             (String)map.get("requestAmount") + "," + (String)map.get("formtypezhi") + ")";
/*  363 */           System.out.println("sqlEntry:" + sqlEntry);
/*  364 */           EAISynTemplate.execute(ctx, database, sqlEntry);
/*  365 */           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/*  366 */               DateBasetype.PaymentBillToMid, 
/*  367 */               payRequestBillInfo.getNumber(), 
/*  368 */               payRequestBillInfo.getString("PaymentBillToMid"), "单据保存成功");
/*      */         } 
/*      */         
/*  371 */         ArrayList<Map<String, String>> attachmentlist = AppUnit.getAttachmentList(ctx, id);
/*  372 */         if (attachmentlist != null && attachmentlist.size() > 0) {
/*  373 */           for (Map<String, String> mp : attachmentlist) {
/*  374 */             sqlEntry = "insert into eas_lolkk_fk_file(parentID,filename,filepath) values('" + 
/*  375 */               id + "','" + (String)mp.get("pre") + "','" + 
/*  376 */               (String)mp.get("path") + "')";
/*  377 */             EAISynTemplate.execute(ctx, database, sqlEntry);
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*  382 */         System.out.println("sql:" + sql);
/*  383 */         EAISynTemplate.execute(ctx, database, sql);
/*      */         
/*  385 */         result = "运行结束，插入成功。";
/*  386 */       } catch (EASBizException e) {
/*      */         
/*  388 */         e.printStackTrace();
/*  389 */         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/*  390 */             DateBasetype.PaymentBillToMid, "单据保存失败", 
/*  391 */             e.getMessage());
/*  392 */         String msg = "运行失败，异常是：" + e.toString();
/*  393 */         return msg;
/*      */       } 
/*      */     } else {
/*  396 */       result = "运行失败，数据已经存在。";
/*      */     } 
/*  398 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Boolean isExistsByBillId(Context ctx, String database, String billID) {
/*  403 */     return Boolean.valueOf(EAISynTemplate.existsoa(ctx, database, "eas_lolkk_fk", billID));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String _PurRequestFormOA(Context ctx, String dataBase1) throws BOSException {
/*  413 */     String sql = null;
/*      */     
/*  415 */     sql = "select id,fnumber,bizdate,purchType,purchModel,company,requestAmount,applyer,CGFK_APPLYER,isGift from eas_lolkk_cg where eassign = 0";
/*  416 */     Calendar cal = Calendar.getInstance();
/*  417 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  418 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, dataBase1, sql.toString());
/*  419 */     String demandTypeNo = "010";
/*  420 */     String fid = null;
/*  421 */     int submitInt = 0;
/*  422 */     boolean giftFlag = false;
/*  423 */     String unitID = "";
/*  424 */     String baseUnitID = "";
/*  425 */     BigDecimal qtymultiple = new BigDecimal(1);
/*      */     try {
/*  427 */       System.out.println("--------------------------" + list.size());
/*  428 */       for (Map<String, Object> map : list) {
/*  429 */         submitInt = 0;
/*  430 */         Boolean addOrUpdate = Boolean.valueOf(false);
/*  431 */         Boolean flag = Boolean.valueOf(true);
/*  432 */         fid = map.get("ID").toString();
/*  433 */         PurRequestInfo info = null;
/*  434 */         System.out.println("_--------------------------------------" + 
/*  435 */             map.get("FNUMBER") + "====" + map.get("COMPANY") + 
/*  436 */             "-----");
/*  437 */         if (map.get("COMPANY") == null || map.get("COMPANY").equals("")) {
/*  438 */           System.out
/*  439 */             .println("_--------------------------------------" + 
/*  440 */               map.get("FNUMBER"));
/*  441 */           updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
/*  442 */           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/*  443 */               DateBasetype.OA_PurRequest, info.getNumber(), 
/*  444 */               info.getString("OA_PurRequest"), "单据保存失败，" + 
/*  445 */               info.getNumber() + "的公司编码为空");
/*      */           continue;
/*      */         } 
/*  448 */         if (map.get("FNUMBER") != null && 
/*  449 */           !map.get("FNUMBER").equals("")) {
/*      */           
/*  451 */           if (PurRequestFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='" + map.get("FNUMBER") + "'")) {
/*      */ 
/*      */             
/*  454 */             addOrUpdate = Boolean.valueOf(true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  468 */             updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, fid);
/*      */             continue;
/*      */           } 
/*  471 */           info = new PurRequestInfo();
/*      */         } else {
/*      */           
/*  474 */           info = new PurRequestInfo();
/*      */         } 
/*      */ 
/*      */         
/*  478 */         info.setIsMergeBill(false);
/*  479 */         info.setPurchaseType(PurchaseTypeEnum.PURCHASE);
/*      */         
/*  481 */         BizTypeInfo bizTypeinfo = BizTypeFactory.getLocalInstance(ctx)
/*  482 */           .getBizTypeCollection("where number = '110'").get(0);
/*  483 */         info.setBizType(bizTypeinfo);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  500 */         RowTypeInfo rowTypeInfoy = RowTypeFactory.getLocalInstance(ctx)
/*  501 */           .getRowTypeInfo("where number = '010'");
/*      */         
/*  503 */         RowTypeInfo rowTypeInfod = RowTypeFactory.getLocalInstance(ctx)
/*  504 */           .getRowTypeInfo("where number = '210'");
/*      */         
/*  506 */         RowTypeInfo rowTypeInfog = RowTypeFactory.getLocalInstance(ctx)
/*  507 */           .getRowTypeInfo("where number = '200'");
/*      */ 
/*      */         
/*  510 */         CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
/*  511 */           .getCurrencyCollection("where number='BB01'").get(0);
/*  512 */         ObjectUuidPK orgPK = new ObjectUuidPK(
/*  513 */             map.get("COMPANY").toString());
/*  514 */         CompanyOrgUnitInfo xmcompany = 
/*  515 */           CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
/*  516 */         info.setCompanyOrgUnit(xmcompany);
/*  517 */         System.out.println("------------------所属公司：" + 
/*  518 */             xmcompany.getId() + "----" + xmcompany.getName());
/*  519 */         AdminOrgUnitInfo admin = AdminOrgUnitFactory.getLocalInstance(
/*  520 */             ctx).getAdminOrgUnitInfo((IObjectPK)orgPK);
/*  521 */         info.setAdminOrg(admin);
/*      */         
/*  523 */         StorageOrgUnitInfo storageorginfo = 
/*  524 */           StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo((IObjectPK)orgPK);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  531 */         PurchaseOrgUnitInfo purchaseorginfo = 
/*  532 */           PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo((IObjectPK)orgPK);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  539 */         PersonInfo person = PersonFactory.getLocalInstance(ctx)
/*  540 */           .getPersonCollection(
/*  541 */             "where number='" + 
/*  542 */             map.get("APPLYER").toString() + "'")
/*  543 */           .get(0);
/*  544 */         info.setPerson(person);
/*      */ 
/*      */         
/*  547 */         if (map.get("CGFK_APPLYER") != null && 
/*  548 */           !"".equals(map.get("CGFK_APPLYER").toString())) {
/*  549 */           PersonInfo CGFK_APPLYER = PersonFactory.getLocalInstance(
/*  550 */               ctx).getPersonCollection(
/*  551 */               "where number='" + 
/*  552 */               map.get("CGFK_APPLYER").toString() + "'")
/*  553 */             .get(0);
/*  554 */           info.put("caigourenyuan", CGFK_APPLYER);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  565 */         SimpleDateFormat formmat = new SimpleDateFormat(
/*  566 */             "yyyy-MM-dd hh:mm:ss");
/*  567 */         Date bizDate = null;
/*  568 */         String purchModel = "";
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  574 */           if (map.get("BIZDATE") != null && 
/*  575 */             !"".equals(map.get("BIZDATE").toString())) {
/*  576 */             bizDate = formmat.parse(map.get("BIZDATE").toString());
/*      */           } else {
/*  578 */             bizDate = new Date();
/*      */           } 
/*  580 */         } catch (ParseException e) {
/*      */           
/*  582 */           e.printStackTrace();
/*      */         } 
/*  584 */         info.setBizDate(bizDate);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  590 */         info.put("caigoushenqingdandanhao", map.get("FNUMBER"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  597 */         if (VerifyUtil.notNull(map.get("PURCHMODEL"))) {
/*  598 */           if ("gaozhi".equals(map.get("PURCHMODEL"))) {
/*  599 */             purchModel = "1";
/*  600 */           } else if ("dizhi".equals(map.get("PURCHMODEL"))) {
/*  601 */             purchModel = "2";
/*  602 */           } else if ("shebei".equals(map.get("PURCHMODEL"))) {
/*  603 */             purchModel = "3";
/*  604 */           } else if ("qixie".equals(map.get("PURCHMODEL"))) {
/*  605 */             purchModel = "4";
/*  606 */           } else if ("xinzeng".equals(map.get("PURCHMODEL"))) {
/*  607 */             purchModel = "5";
/*  608 */           } else if ("hulichuanpin".equals(map.get("PURCHMODEL"))) {
/*  609 */             purchModel = "6";
/*  610 */           } else if ("feiqixie".equals(map.get("PURCHMODEL"))) {
/*  611 */             purchModel = "7";
/*      */           } 
/*      */         }
/*  614 */         giftFlag = false;
/*      */         
/*  616 */         if (map.get("ISGIFT") != null && !"".equals(map.get("ISGIFT").toString())) {
/*  617 */           info.put("isGift", map.get("ISGIFT"));
/*  618 */           if (("1".equals(purchModel) || "3".equals(purchModel) || "4".equals(purchModel) || "7".equals(purchModel)) && 
/*  619 */             "1".equals(map.get("ISGIFT").toString())) {
/*  620 */             giftFlag = true;
/*      */           }
/*      */         } else {
/*      */           
/*  624 */           info.put("isGift", "0");
/*      */         } 
/*  626 */         if (VerifyUtil.notNull(purchModel))
/*      */         {
/*  628 */           info.put("danjuleixzing", purchModel);
/*      */         }
/*      */         
/*  631 */         BillTypeInfo billtype = new BillTypeInfo();
/*  632 */         billtype.setId(
/*  633 */             BOSUuid.read("510b6503-0105-1000-e000-0107c0a812fd463ED552"));
/*  634 */         info.setBillType(billtype);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  646 */         if (map.get("PURCHTYPE") != null && 
/*  647 */           map.get("PURCHTYPE").equals("10")) {
/*      */ 
/*      */           
/*  650 */           if (flag.booleanValue()) {
/*  651 */             PurRequestInfo infoBig = (PurRequestInfo)info.clone();
/*  652 */             sql = "select parentid,material,materialname,supplier,brand,guige,xh,artNo,price,qty,amount from eas_lolkk_cg_sub where parentid =" + 
/*  653 */               map.get("ID");
/*      */             
/*  655 */             List<Map<String, Object>> list1 = EAISynTemplate.query(
/*  656 */                 ctx, dataBase1, sql);
/*  657 */             BigDecimal totalAmountSmall = new BigDecimal(0);
/*  658 */             BigDecimal totalAmountBig = new BigDecimal(0);
/*      */             
/*  660 */             if (list1 != null && list1.size() > 0) {
/*  661 */               for (Map<String, Object> map1 : list1) {
/*  662 */                 PurRequestEntryInfo entryInfo = new PurRequestEntryInfo();
/*  663 */                 qtymultiple = new BigDecimal(1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 try {
/*  681 */                   EntityViewInfo viewInfo = new EntityViewInfo();
/*  682 */                   FilterInfo filter = new FilterInfo();
/*  683 */                   filter.getFilterItems().add(
/*  684 */                       new FilterItemInfo("number", 
/*  685 */                         map1.get("MATERIAL")
/*  686 */                         .toString(), 
/*  687 */                         CompareType.EQUALS));
/*  688 */                   viewInfo.setFilter(filter);
/*  689 */                   IMaterial imaterial = 
/*  690 */                     MaterialFactory.getLocalInstance(ctx);
/*  691 */                   MaterialCollection collection = imaterial
/*  692 */                     .getMaterialCollection(viewInfo);
/*  693 */                   MaterialInfo material = collection.get(0);
/*  694 */                   entryInfo.setMaterialName(
/*  695 */                       material.getName());
/*  696 */                   entryInfo.setNoNumMaterialModel(
/*  697 */                       material.getModel());
/*  698 */                   entryInfo.setMaterial(material);
/*  699 */                   MeasureUnitInfo unitInfo = new MeasureUnitInfo();
/*  700 */                   MeasureUnitInfo baseUnitInfo = new MeasureUnitInfo();
/*  701 */                   unitID = materialUnitId(ctx, map.get("COMPANY").toString(), material.getId().toString());
/*  702 */                   if (unitID != null && !"".equals(unitID)) {
/*  703 */                     unitInfo.setId(BOSUuid.read(unitID));
/*  704 */                     baseUnitInfo = material.getBaseUnit();
/*  705 */                     if (!material.getBaseUnit().getId().toString().equals(unitID)) {
/*  706 */                       qtymultiple = getmaterialMultiple(ctx, material.getId().toString(), unitID);
/*      */                     }
/*      */                   } else {
/*  709 */                     unitInfo = material.getBaseUnit();
/*  710 */                     baseUnitInfo = material.getBaseUnit();
/*      */                   } 
/*  712 */                   entryInfo.setBaseUnit(baseUnitInfo);
/*  713 */                   entryInfo.setUnit(unitInfo);
/*  714 */                 } catch (Exception e) {
/*  715 */                   updateFSign(ctx, dataBase1, "eas_lolkk_cg", 
/*  716 */                       2, fid);
/*  717 */                   AppUnit.insertLog(ctx, 
/*  718 */                       DateBaseProcessType.AddNew, 
/*  719 */                       DateBasetype.OA_PurRequest, 
/*  720 */                       info.getNumber(), 
/*  721 */                       map.get("FNUMBER").toString(), 
/*  722 */                       "单据保存失败，" + info.getNumber() + 
/*  723 */                       "物料编码不存在");
/*      */                 } 
/*      */                 
/*  726 */                 BigDecimal amount = new BigDecimal(0.0D);
/*  727 */                 BigDecimal price = new BigDecimal(0.0D);
/*  728 */                 if (map1.get("AMOUNT") != null && 
/*  729 */                   !"".equals(
/*  730 */                     map1.get("AMOUNT").toString().trim())) {
/*  731 */                   amount = new BigDecimal(
/*  732 */                       map1.get("AMOUNT").toString().trim());
/*      */                 }
/*  734 */                 if (map1.get("PRICE") != null && 
/*  735 */                   !"".equals(
/*  736 */                     map1.get("PRICE").toString().trim())) {
/*  737 */                   price = new BigDecimal(
/*  738 */                       map1.get("PRICE").toString().trim());
/*      */                 }
/*  740 */                 BigDecimal qty = new BigDecimal(
/*  741 */                     map1.get("QTY").toString());
/*      */ 
/*      */                 
/*  744 */                 if (VerifyUtil.notNull(map1.get("GUIGE"))) {
/*  745 */                   entryInfo.setNoNumMaterialModel(
/*  746 */                       map1.get("GUIGE").toString());
/*      */                 }
/*      */                 
/*  749 */                 if (VerifyUtil.notNull(map1.get("SUPPLIER")))
/*      */                 {
/*  751 */                   if (SupplierFactory.getLocalInstance(ctx)
/*  752 */                     .exists(
/*  753 */                       "where number='" + 
/*  754 */                       map1
/*  755 */                       .get("SUPPLIER") + 
/*  756 */                       "'")) {
/*  757 */                     SupplierInfo supplierInfo = 
/*  758 */                       SupplierFactory.getLocalInstance(ctx)
/*  759 */                       .getSupplierInfo(
/*  760 */                         " where number='" + 
/*  761 */                         map1
/*  762 */                         .get("SUPPLIER") + 
/*  763 */                         "'");
/*  764 */                     entryInfo.setSupplier(supplierInfo);
/*      */                   } 
/*      */                 }
/*      */                 
/*  768 */                 entryInfo.setPerson(person);
/*  769 */                 entryInfo.setPurchasePerson(person);
/*  770 */                 entryInfo.setReceivedOrgUnit(storageorginfo);
/*      */                 
/*  772 */                 entryInfo.setAdminOrgUnit(admin);
/*  773 */                 entryInfo.setBizDate(info.getBizDate());
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  778 */                 entryInfo.setQty(qty);
/*  779 */                 entryInfo.setAssociateQty(qty);
/*  780 */                 entryInfo.setBaseQty(qty.multiply(qtymultiple));
/*  781 */                 entryInfo.setRequestQty(qty);
/*  782 */                 entryInfo.setUnOrderedQty(BigDecimal.ZERO);
/*  783 */                 entryInfo.setUnOrderedBaseQty(BigDecimal.ZERO);
/*  784 */                 entryInfo.setAssistQty(BigDecimal.ZERO);
/*      */ 
/*      */ 
/*      */                 
/*  788 */                 entryInfo
/*  789 */                   .setExchangeRate(new BigDecimal("1.00"));
/*  790 */                 entryInfo.setPrice(price);
/*  791 */                 entryInfo.setTaxPrice(price);
/*  792 */                 entryInfo.setActualPrice(price);
/*  793 */                 entryInfo.setActualTaxPrice(price);
/*  794 */                 entryInfo.setTaxAmount(amount);
/*  795 */                 entryInfo.setCurrency(currency);
/*  796 */                 entryInfo.setStorageOrgUnit(storageorginfo);
/*  797 */                 entryInfo.setPurchaseOrgUnit(purchaseorginfo);
/*  798 */                 entryInfo.setPurchasePerson(person);
/*  799 */                 entryInfo.setLocalAmount(amount);
/*  800 */                 entryInfo.setLocalTaxAmount(amount);
/*  801 */                 entryInfo.setAmount(amount);
/*  802 */                 entryInfo.setParent(info);
/*  803 */                 entryInfo.setLocalTaxAmount(amount);
/*  804 */                 entryInfo.setActualPrice(price);
/*  805 */                 entryInfo.setActualTaxPrice(price);
/*  806 */                 entryInfo.setOrderedQty(qty);
/*  807 */                 entryInfo.setAssOrderBaseQty(qty);
/*      */                 
/*  809 */                 String gugexh = "";
/*  810 */                 if (map1.get("GUIGE") != null)
/*      */                 {
/*  812 */                   if (!map1.get("GUIGE").toString().equals("")) {
/*  813 */                     gugexh = map1.get("GUIGE").toString();
/*  814 */                     entryInfo.setNoNumMaterialModel(gugexh);
/*      */                   }  } 
/*  816 */                 entryInfo.put("xinghao", map1.get("XH"));
/*  817 */                 entryInfo.put("pinpai", map1.get("BRAND"));
/*  818 */                 entryInfo.put("huohao", map1.get("ARTNO"));
/*      */                 
/*  820 */                 entryInfo.setRequirementDate(bizDate);
/*  821 */                 entryInfo.setProposeDeliveryDate(bizDate);
/*  822 */                 entryInfo.setProposePurchaseDate(bizDate);
/*      */                 
/*  824 */                 if (giftFlag) {
/*  825 */                   entryInfo.setRowType(rowTypeInfoy);
/*  826 */                   info.getEntries().add(entryInfo);
/*  827 */                   totalAmountSmall = totalAmountSmall.add(amount); continue;
/*      */                 } 
/*  829 */                 if (price.compareTo(new BigDecimal(2000)) == -1) {
/*  830 */                   entryInfo.setRowType(rowTypeInfod);
/*  831 */                   info.getEntries().add(entryInfo);
/*  832 */                   totalAmountSmall = totalAmountSmall.add(amount); continue;
/*      */                 } 
/*  834 */                 entryInfo.setRowType(rowTypeInfog);
/*  835 */                 infoBig.getEntries().add(entryInfo);
/*  836 */                 totalAmountBig = totalAmountBig.add(amount);
/*      */               }
/*      */             
/*      */             }
/*      */             else {
/*      */               
/*  842 */               System.out
/*  843 */                 .println("entrty is empty _--------------------------------------" + 
/*  844 */                   map.get("FNUMBER"));
/*  845 */               updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
/*  846 */               AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/*  847 */                   DateBasetype.OA_PurRequest, 
/*  848 */                   info.getNumber(), map.get("FNUMBER").toString(), 
/*  849 */                   "单据保存失败，" + info.getNumber() + "的没有分录");
/*      */               
/*      */               continue;
/*      */             } 
/*  853 */             if (info.getEntries().size() > 0) {
/*  854 */               info.setTotalAmount(totalAmountSmall);
/*  855 */               info.setLocalTotalAmount(totalAmountSmall);
/*  856 */               DemandTypeInfo demandTypeInfo = new DemandTypeInfo();
/*  857 */               if (giftFlag) {
/*  858 */                 demandTypeInfo.setId(BOSUuid.read("d8iX3GB6dt3gUwEAAH8kOKvcMAg="));
/*      */               } else {
/*  860 */                 demandTypeInfo.setId(BOSUuid.read("nMyVhAcxRvyXPYq+xI49eqvcMAg="));
/*      */               } 
/*      */               
/*  863 */               info.setDemandType(demandTypeInfo);
/*      */               
/*  865 */               info.put("caigoushenqingdanjine", totalAmountSmall);
/*  866 */               PurRequestFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/*  871 */             if (infoBig.getEntries().size() > 0) {
/*  872 */               infoBig.setTotalAmount(totalAmountBig);
/*  873 */               infoBig.setLocalTotalAmount(totalAmountBig);
/*  874 */               DemandTypeInfo type2 = new DemandTypeInfo();
/*      */ 
/*      */               
/*  877 */               type2.setId(
/*  878 */                   BOSUuid.read("Sp/A4ZhGTD2izLb/R8/WfavcMAg="));
/*  879 */               infoBig.setDemandType(type2);
/*      */               
/*  881 */               infoBig
/*  882 */                 .put("caigoushenqingdanjine", 
/*  883 */                   totalAmountBig);
/*  884 */               PurRequestFactory.getLocalInstance(ctx).save(
/*  885 */                   (CoreBaseInfo)infoBig);
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  891 */             if (info.getEntries().size() > 0) {
/*  892 */               submitInt = 1;
/*  893 */               PurRequestFactory.getLocalInstance(ctx)
/*  894 */                 .submit((CoreBaseInfo)info);
/*      */             } 
/*      */             
/*  897 */             if (infoBig.getEntries().size() > 0) {
/*  898 */               submitInt = 2;
/*  899 */               PurRequestFactory.getLocalInstance(ctx).submit(
/*  900 */                   (CoreBaseInfo)infoBig);
/*      */             } 
/*  902 */             updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, 
/*  903 */                 map.get("ID").toString());
/*  904 */             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/*  905 */                 DateBasetype.OA_PurRequest, info.getNumber(), 
/*  906 */                 map.get("FNUMBER").toString(), "单据保存成功");
/*      */             
/*      */             continue;
/*      */           } 
/*  910 */           PurRequestFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
/*  911 */           submitInt = 1;
/*  912 */           PurRequestFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
/*      */           
/*  914 */           updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, 
/*  915 */               map.get("ID").toString());
/*  916 */           AppUnit.insertLog(ctx, DateBaseProcessType.Update, 
/*  917 */               DateBasetype.OA_PurRequest, info.getNumber(), 
/*  918 */               map.get("FNUMBER").toString(), "单据修改成功");
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  923 */         BigDecimal totalAmount = new BigDecimal(0);
/*  924 */         if (flag.booleanValue()) {
/*  925 */           sql = "select parentid,material,materialname,supplier,brand,guige,xh,artNo,price,qty,amount from eas_lolkk_cg_sub where parentid =" + 
/*  926 */             map.get("ID");
/*      */           
/*  928 */           List<Map<String, Object>> list1 = EAISynTemplate.query(
/*  929 */               ctx, dataBase1, sql);
/*      */           
/*  931 */           if (list1 != null && list1.size() > 0) {
/*  932 */             for (Map<String, Object> map1 : list1) {
/*  933 */               PurRequestEntryInfo entryInfo = new PurRequestEntryInfo();
/*  934 */               qtymultiple = new BigDecimal(1);
/*  935 */               entryInfo.setRowType(rowTypeInfoy);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/*  952 */                 EntityViewInfo viewInfo = new EntityViewInfo();
/*  953 */                 FilterInfo filter = new FilterInfo();
/*  954 */                 filter.getFilterItems().add(
/*  955 */                     new FilterItemInfo("number", 
/*  956 */                       map1.get("MATERIAL")
/*  957 */                       .toString(), 
/*  958 */                       CompareType.EQUALS));
/*  959 */                 viewInfo.setFilter(filter);
/*  960 */                 IMaterial imaterial = 
/*  961 */                   MaterialFactory.getLocalInstance(ctx);
/*  962 */                 MaterialCollection collection = imaterial
/*  963 */                   .getMaterialCollection(viewInfo);
/*  964 */                 MaterialInfo material = collection.get(0);
/*  965 */                 entryInfo.setMaterialName(
/*  966 */                     material.getName());
/*  967 */                 entryInfo.setNoNumMaterialModel(
/*  968 */                     material.getModel());
/*  969 */                 entryInfo.setMaterial(material);
/*  970 */                 MeasureUnitInfo unitInfo = new MeasureUnitInfo();
/*  971 */                 MeasureUnitInfo baseUnitInfo = new MeasureUnitInfo();
/*  972 */                 unitID = materialUnitId(ctx, map.get("COMPANY").toString(), material.getId().toString());
/*  973 */                 if (unitID != null && !"".equals(unitID)) {
/*  974 */                   unitInfo.setId(BOSUuid.read(unitID));
/*  975 */                   baseUnitInfo = material.getBaseUnit();
/*  976 */                   if (!material.getBaseUnit().getId().toString().equals(unitID)) {
/*  977 */                     qtymultiple = getmaterialMultiple(ctx, material.getId().toString(), unitID);
/*      */                   }
/*      */                 } else {
/*  980 */                   unitInfo = material.getBaseUnit();
/*  981 */                   baseUnitInfo = material.getBaseUnit();
/*      */                 } 
/*  983 */                 entryInfo.setBaseUnit(baseUnitInfo);
/*  984 */                 entryInfo.setUnit(unitInfo);
/*      */               }
/*  986 */               catch (Exception e) {
/*  987 */                 updateFSign(ctx, dataBase1, "eas_lolkk_cg", 
/*  988 */                     2, fid);
/*  989 */                 AppUnit.insertLog(ctx, 
/*  990 */                     DateBaseProcessType.AddNew, 
/*  991 */                     DateBasetype.OA_PurRequest, 
/*  992 */                     info.getNumber(), map.get("FNUMBER").toString(), 
/*  993 */                     "单据保存失败，" + info.getNumber() + 
/*  994 */                     "物料编码不存在");
/*      */               } 
/*      */               
/*  997 */               BigDecimal amount = new BigDecimal(0.0D);
/*  998 */               BigDecimal price = new BigDecimal(0.0D);
/*  999 */               if (map1.get("AMOUNT") != null && 
/* 1000 */                 !"".equals(
/* 1001 */                   map1.get("AMOUNT").toString().trim())) {
/* 1002 */                 amount = new BigDecimal(
/* 1003 */                     map1.get("AMOUNT").toString().trim());
/*      */               }
/* 1005 */               if (map1.get("PRICE") != null && 
/* 1006 */                 !"".equals(
/* 1007 */                   map1.get("PRICE").toString().trim())) {
/* 1008 */                 price = new BigDecimal(
/* 1009 */                     map1.get("PRICE").toString().trim());
/*      */               }
/* 1011 */               BigDecimal qty = new BigDecimal(
/* 1012 */                   map1.get("QTY").toString());
/*      */ 
/*      */               
/* 1015 */               if (VerifyUtil.notNull(map1.get("GUIGE"))) {
/* 1016 */                 entryInfo.setNoNumMaterialModel(
/* 1017 */                     map1.get("GUIGE").toString());
/*      */               }
/* 1019 */               if (VerifyUtil.notNull(map1.get("SUPPLIER")))
/*      */               {
/* 1021 */                 if (SupplierFactory.getLocalInstance(ctx)
/* 1022 */                   .exists(
/* 1023 */                     "where number='" + 
/* 1024 */                     map1
/* 1025 */                     .get("SUPPLIER") + 
/* 1026 */                     "'")) {
/* 1027 */                   SupplierInfo supplierInfo = 
/* 1028 */                     SupplierFactory.getLocalInstance(ctx)
/* 1029 */                     .getSupplierInfo(
/* 1030 */                       " where number='" + 
/* 1031 */                       map1
/* 1032 */                       .get("SUPPLIER") + 
/* 1033 */                       "'");
/* 1034 */                   entryInfo.setSupplier(supplierInfo);
/*      */                 } 
/*      */               }
/* 1037 */               entryInfo.setPerson(person);
/* 1038 */               entryInfo.setPurchasePerson(person);
/* 1039 */               entryInfo.setReceivedOrgUnit(storageorginfo);
/*      */ 
/*      */ 
/*      */               
/* 1043 */               entryInfo.setAdminOrgUnit(admin);
/* 1044 */               entryInfo.setBizDate(info.getBizDate());
/*      */ 
/*      */ 
/*      */               
/* 1048 */               entryInfo.setQty(qty);
/* 1049 */               entryInfo.setAssociateQty(qty);
/* 1050 */               entryInfo.setBaseQty(qty.multiply(qtymultiple));
/*      */               
/* 1052 */               entryInfo.setUnOrderedQty(BigDecimal.ZERO);
/* 1053 */               entryInfo.setUnOrderedBaseQty(BigDecimal.ZERO);
/* 1054 */               entryInfo.setAssistQty(BigDecimal.ZERO);
/*      */ 
/*      */ 
/*      */               
/* 1058 */               entryInfo
/* 1059 */                 .setExchangeRate(new BigDecimal("1.00"));
/* 1060 */               entryInfo.setPrice(price);
/* 1061 */               entryInfo.setTaxPrice(price);
/* 1062 */               entryInfo.setActualPrice(price);
/* 1063 */               entryInfo.setActualTaxPrice(price);
/* 1064 */               entryInfo.setTaxAmount(amount);
/* 1065 */               entryInfo.setCurrency(currency);
/* 1066 */               entryInfo.setStorageOrgUnit(storageorginfo);
/* 1067 */               entryInfo.setPurchaseOrgUnit(purchaseorginfo);
/* 1068 */               entryInfo.setPurchasePerson(person);
/* 1069 */               entryInfo.setLocalAmount(amount);
/* 1070 */               entryInfo.setLocalTaxAmount(amount);
/* 1071 */               entryInfo.setAmount(amount);
/* 1072 */               entryInfo.setParent(info);
/*      */               
/* 1074 */               String gugexh = "";
/* 1075 */               if (map1.get("GUIGE") != null)
/*      */               {
/* 1077 */                 if (!map1.get("GUIGE").toString().equals("")) {
/* 1078 */                   gugexh = map1.get("GUIGE").toString();
/* 1079 */                   entryInfo.setNoNumMaterialModel(gugexh);
/*      */                 }  } 
/* 1081 */               entryInfo.put("xinghao", map1.get("XH"));
/* 1082 */               entryInfo.put("pinpai", map1.get("BRAND"));
/* 1083 */               entryInfo.put("huohao", map1.get("ARTNO"));
/* 1084 */               entryInfo.setRequestQty(qty);
/* 1085 */               entryInfo.setRequirementDate(bizDate);
/* 1086 */               entryInfo.setProposeDeliveryDate(bizDate);
/* 1087 */               entryInfo.setProposePurchaseDate(bizDate);
/*      */ 
/*      */               
/* 1090 */               totalAmount = totalAmount.add(amount);
/* 1091 */               info.getEntries().add(entryInfo);
/*      */             } 
/*      */           } else {
/* 1094 */             System.out
/* 1095 */               .println("entrty is empty _--------------------------------------" + 
/* 1096 */                 map.get("FNUMBER"));
/* 1097 */             updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
/* 1098 */             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 1099 */                 DateBasetype.OA_PurRequest, 
/* 1100 */                 info.getNumber(), map.get("FNUMBER").toString(), 
/* 1101 */                 "单据保存失败，" + info.getNumber() + "的没有分录");
/*      */             continue;
/*      */           } 
/*      */         } 
/* 1105 */         info.put("caigoushenqingdanjine", totalAmount);
/* 1106 */         info.setTotalAmount(totalAmount);
/* 1107 */         info.setLocalTotalAmount(totalAmount);
/* 1108 */         DemandTypeInfo type = new DemandTypeInfo();
/*      */ 
/*      */         
/* 1111 */         type.setId(BOSUuid.read("d8iX3GB6dt3gUwEAAH8kOKvcMAg="));
/* 1112 */         info.setDemandType(type);
/*      */         
/* 1114 */         PurRequestFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
/* 1115 */         submitInt = 1;
/* 1116 */         PurRequestFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
/* 1117 */         updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, 
/* 1118 */             map.get("ID").toString());
/*      */         
/* 1120 */         if (addOrUpdate.booleanValue()) {
/* 1121 */           AppUnit.insertLog(ctx, DateBaseProcessType.Update, 
/* 1122 */               DateBasetype.OA_PurRequest, info.getNumber(), 
/* 1123 */               info.getString("OA_PurRequest"), "单据修改成功"); continue;
/*      */         } 
/* 1125 */         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 1126 */             DateBasetype.OA_PurRequest, info.getNumber(), 
/* 1127 */             info.getString("OA_PurRequest"), "单据保存成功");
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1136 */     catch (EASBizException e) {
/*      */       
/* 1138 */       e.printStackTrace();
/* 1139 */       String msg = "";
/* 1140 */       if (submitInt == 0) {
/* 1141 */         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 1142 */             DateBasetype.OA_PurRequest, String.valueOf(String.valueOf(fid)) + "单据保存失败", e.getMessage());
/* 1143 */         if (fid != null && !fid.equals("")) {
/* 1144 */           updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
/*      */         }
/* 1146 */         msg = "运行失败，异常是：" + e.toString();
/*      */       } else {
/* 1148 */         msg = "单据提交失败: " + e.toString();
/* 1149 */         updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, fid);
/*      */       } 
/* 1151 */       return msg;
/*      */     } 
/*      */ 
/*      */     
/* 1155 */     return super._PurRequestFormOA(ctx, dataBase1);
/*      */   } 
/*      */   public String syncPaymentBillFormOA(Context ctx, String database) throws BOSException {
/* 1168 */     String sql = null;
/* 1169 */     sql = " select bx.id,bx.fnumber,bx.bizDate,bx.isLoan,bx.payType,bx.isrentalfee,bx.company,bx.Dept,bx.supplierid,bx.Yhzh,bx.Khh,bx.applyer,  bx.Applyerbank,bx.Applyerbanknum,bx.Agency,bx.Amount,bx.Jsfs,bx.purchType,bx.purchModel,bx.Paystate,bx.Paystatetime  from eas_lolkk_bx bx   where bx.PURCHTYPE = '08' and bx.eassign = 0";
/*      */ 
/*      */     
/* 1172 */     Calendar cal = Calendar.getInstance();
/* 1173 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, sql.toString());
/* 1174 */     String fid = null;
/*      */     try {
/* 1176 */       System.out.println("--------------------------" + list.size());
/* 1177 */       for (Map<String, Object> map : list) {
/* 1178 */         fid = map.get("ID").toString();
/* 1179 */         PaymentBillInfo payInfo = new PaymentBillInfo();
/* 1180 */         if (map.get("COMPANY") == null || map.get("COMPANY").toString().equals("")) {
/* 1181 */           System.out.println("_--------------------------------------" + map.get("FNUMBER"));
/* 1182 */           updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 1183 */           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "单据保存失败," + payInfo.getNumber() + "的公司编码为空");
/*      */           continue;
/*      */         } 
/* 1186 */         if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("") && 
/* 1187 */           PaymentBillFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='" + map.get("FNUMBER") + "'")) {
/* 1188 */           updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/* 1193 */         payInfo.setSourceType(SourceTypeEnum.AP);
/* 1194 */         payInfo.setDescription("无");
/* 1195 */         payInfo.setIsExchanged(false);
/* 1196 */         payInfo.setExchangeRate(new BigDecimal("1.00"));
/* 1197 */         payInfo.setLastExhangeRate(new BigDecimal("0.00"));
/* 1198 */         payInfo.setIsInitializeBill(false);
/* 1199 */         CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
/* 1200 */         payInfo.setCurrency(currency);
/* 1201 */         payInfo.setFiVouchered(false);
/* 1202 */         payInfo.setIsLanding(false);
/*      */ 
/*      */ 
/*      */         
/* 1206 */         AsstActTypeInfo actType = new AsstActTypeInfo();
/* 1207 */         actType.setId(BOSUuid.read("YW3xsAEJEADgAAWgwKgTB0c4VZA="));
/* 1208 */         payInfo.setPayeeType(actType);
/* 1209 */         payInfo.setIsImport(false);
/* 1210 */         payInfo.setIsNeedPay(true);
/* 1211 */         payInfo.setIsReverseLockAmount(true);
/* 1212 */         payInfo.setPaymentBillType(CasRecPayBillTypeEnum.commonType);
/* 1213 */         PaymentBillTypeInfo billType = PaymentBillTypeFactory.getLocalInstance(ctx).getPaymentBillTypeInfo((IObjectPK)new ObjectUuidPK("NLGLdwEREADgAAHjwKgSRj6TKVs="));
/* 1214 */         payInfo.setPayBillType(billType);
/* 1215 */         PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx).getPaymentTypeInfo((IObjectPK)new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
/* 1216 */         payInfo.setPaymentType(paymentTypeInfo);
/*      */ 
/*      */         
/* 1219 */         SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
/* 1220 */           .getSettlementTypeInfo((IObjectPK)new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
/* 1221 */         payInfo.setSettlementType(settlementTypeInfo);
/*      */ 
/*      */         
/* 1224 */         payInfo.put("caigoushenqingdandanhao", map.get("FNUMBER").toString());
/*      */ 
/*      */         
/* 1227 */         ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY").toString());
/* 1228 */         CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
/* 1229 */         payInfo.setCompany(company);
/* 1230 */         System.out.println("------------------所属公司：" + company.getId() + "----" + company.getName());
/*      */ 
/*      */         
/* 1233 */         PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonCollection("where number='" + map.get("APPLYER").toString() + "'").get(0);
/*      */ 
/*      */ 
/*      */         
/* 1237 */         payInfo.setPayeeID(person.getId().toString());
/* 1238 */         payInfo.setPayeeName(person.getName());
/* 1239 */         payInfo.setPayeeNumber(person.getNumber());
/* 1240 */         payInfo.setPayeeBank(map.get("APPLYERBANK").toString());
/* 1241 */         payInfo.setPayeeAccountBank(map.get("APPLYERBANKNUM").toString());
/*      */ 
/*      */         
/* 1244 */         payInfo.put("kaihuhang", map.get("APPLYERBANK").toString());
/* 1245 */         payInfo.put("yinhangzhanghao", map.get("APPLYERBANKNUM").toString());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1250 */         payInfo.setBankAcctName(person.getName());
/*      */         
/* 1252 */         AdminOrgUnitInfo admin = null;
/* 1253 */         if (map.get("DEPT") != null && !"".equals(map.get("DEPT").toString())) {
/* 1254 */           admin = AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitInfo((IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
/* 1255 */           payInfo.setAdminOrgUnit(admin);
/*      */         }         
/* 1265 */         SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 1266 */         Date bizDate = null;
/*      */         try {
/* 1268 */           if (map.get("BIZDATE") != null && !"".equals(map.get("BIZDATE").toString())) {
/* 1269 */             bizDate = formmat.parse(map.get("BIZDATE").toString());
/*      */           } else {
/* 1271 */             bizDate = new Date();
/*      */           } 
/* 1273 */         } catch (ParseException e) {
/* 1274 */           e.printStackTrace();
/*      */         } 
/*      */         
/* 1277 */         payInfo.setBizDate(bizDate);
/* 1278 */         payInfo.setBillDate(new Date());        
/* 1307 */         BigDecimal totalAmount = new BigDecimal(map.get("AMOUNT").toString());
/* 1308 */         String entrySql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid ='" + map.get("ID").toString() + "' ";
/* 1309 */         List<Map<String, Object>> enrtyList = EAISynTemplate.query(ctx, database, entrySql.toString());
/* 1310 */         if (enrtyList != null && enrtyList.size() > 0) {
/* 1311 */           for (Map<String, Object> entryMap : enrtyList) {
/* 1312 */             BigDecimal amount = new BigDecimal(entryMap.get("AMOUNT").toString());
/* 1313 */             PaymentBillEntryInfo entryInfo = new PaymentBillEntryInfo();
/*      */             
/* 1315 */             if (entryMap.get("PAYTYPECODE") != null && !"".equals(entryMap.get("PAYTYPECODE").toString())) {
/* 1316 */               ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx)
/* 1317 */                 .getExpenseTypeInfo("where number ='" + entryMap.get("PAYTYPECODE").toString() + "'");
/* 1318 */               entryInfo.setExpenseType(typeinfo);
/*      */             } else {
/* 1320 */               ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
/* 1321 */               entryInfo.setExpenseType(typeinfo);
/*      */             } 
/*      */             
/* 1324 */             entryInfo.setCurrency(currency);
/* 1325 */             entryInfo.setAmount(amount);
/* 1326 */             entryInfo.setAmountVc(BigDecimal.ZERO);
/* 1327 */             entryInfo.setLocalAmt(amount);
/* 1328 */             entryInfo.setLocalAmtVc(BigDecimal.ZERO);
/* 1329 */             entryInfo.setUnVcAmount(amount);
/* 1330 */             entryInfo.setUnVcLocAmount(amount);
/* 1331 */             entryInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO);
/* 1332 */             entryInfo.setRebate(BigDecimal.ZERO);
/* 1333 */             entryInfo.setRebateAmtVc(BigDecimal.ZERO);
/* 1334 */             entryInfo.setRebateLocAmt(BigDecimal.ZERO);
/* 1335 */             entryInfo.setRebateLocAmtVc(BigDecimal.ZERO);
/* 1336 */             entryInfo.setActualAmt(amount);
/* 1337 */             entryInfo.setActualAmtVc(BigDecimal.ZERO);
/* 1338 */             entryInfo.setActualLocAmt(amount);
/* 1339 */             entryInfo.setActualLocAmtVc(BigDecimal.ZERO);
/* 1340 */             entryInfo.setUnLockAmt(amount);
/* 1341 */             entryInfo.setUnLockLocAmt(amount);
/* 1342 */             entryInfo.setLockAmt(BigDecimal.ZERO);
/*      */             
/* 1344 */             entryInfo.setPayableDate(new Date());
/*      */             
/* 1346 */             if (entryMap.get("REMARK") != null) {
/* 1347 */               entryInfo.setRemark(entryMap.get("REMARK").toString());
/*      */             }
/*      */ 
/*      */             
/* 1351 */             payInfo.getEntries().addObject((IObjectValue)entryInfo);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 1356 */           System.out.println("entrty is empty _---------------------------" + map.get("FNUMBER").toString());
/* 1357 */           updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 1358 */           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "单据没有分录");
/*      */           
/*      */           continue;
/*      */         } 
/* 1362 */         payInfo.setActPayAmtVc(BigDecimal.ZERO);
/* 1363 */         payInfo.setActPayAmt(totalAmount);
/* 1364 */         payInfo.setActPayLocAmtVc(BigDecimal.ZERO);
/* 1365 */         payInfo.setAmount(totalAmount);
/* 1366 */         payInfo.setLocalAmt(totalAmount);
/* 1367 */         payInfo.setAccessoryAmt(0);
/* 1368 */         payInfo.setBgAmount(BigDecimal.ZERO);
/* 1369 */         payInfo.setVerifiedAmt(BigDecimal.ZERO);
/* 1370 */         payInfo.setVerifiedAmtLoc(BigDecimal.ZERO);
/* 1371 */         payInfo.setUnVerifiedAmt(totalAmount);
/* 1372 */         payInfo.setUnVerifiedAmtLoc(totalAmount);
/* 1373 */         payInfo.setBgCtrlAmt(totalAmount);
/*      */         
/* 1375 */         payInfo.setBillStatus(com.kingdee.eas.fi.cas.BillStatusEnum.SAVE);       
/* 1382 */         PaymentBillFactory.getLocalInstance(ctx).save((CoreBaseInfo)payInfo);
/* 1383 */         updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString()); 
/*      */       }  
/*      */     }
/* 1395 */     catch (EASBizException e) {
/*      */       
/* 1397 */       e.printStackTrace();
/* 1398 */       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.PaymentBillToMid, String.valueOf(String.valueOf(fid)) + "单据保存失败", e.getMessage());
/* 1399 */       String msg = "运行失败，异常是：" + e.toString();
/* 1400 */       System.out.println("--------------------" + msg);
/* 1401 */       return msg;
/*      */     } 
/*      */     
/* 1404 */     return super.syncPaymentBillFormOA(ctx, database);
/*      */   } 
/*      */   public void updateFSign(Context ctx, String database, String tableName, int fSign, String fid) throws BOSException {
/* 1419 */     String updateSql = "UPDATE " + 
/* 1420 */       tableName + 
/* 1421 */       " set eassign = " + 
/* 1422 */       fSign + 
/* 1423 */       " , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where ID = '" + 
/* 1424 */       fid + "'";
/* 1425 */     System.out.print("--------------" + updateSql);
/* 1426 */     EAISynTemplate.execute(ctx, database, updateSql);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFSign(Context ctx, String database, String tableName, int fSign, String fid, String sqlServer) throws BOSException {
/* 1431 */     String updateSql = "UPDATE " + tableName + " set eassign = " + fSign + 
/* 1432 */       " , EASTIME = CONVERT(varchar,GETDATE(),120) where ID = '" + 
/* 1433 */       fid + "'";
/* 1434 */     System.out.print("--------------" + updateSql);
/* 1435 */     EAISynTemplate.execute(ctx, database, updateSql);
/*      */   }
/*      */ 
/*      */   
/*      */   public void syncPayApply(Context ctx, String database) throws BOSException {
/* 1440 */     String sql = "select id ,fnumber from eas_lolkk_fk where oafinishsign = 1 and eassign=1 and paystate = 0 and CONVERT(varchar(7), bizdate, 120) >= CONVERT(varchar(7), dateadd(month,-3,getdate()) , 120)  order by bizdate desc";
/* 1441 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, sql.toString());
/* 1442 */     IPayRequestBill ibiz = PayRequestBillFactory.getLocalInstance(ctx);
/* 1443 */     for (Map<String, Object> map : list) {
/* 1444 */       if (map.get("ID") != null && !"".equals(map.get("ID").toString())) {
/* 1445 */         ObjectUuidPK objectUuidPK = new ObjectUuidPK(map.get("ID").toString());
/*      */         try {
/* 1447 */           PayRequestBillInfo info = ibiz.getPayRequestBillInfo((IObjectPK)objectUuidPK);
/* 1448 */           if (info != null && 
/* 1449 */             info.getBillStatus() != BillStatusEnum.AUDITED) {
/* 1450 */             ibiz.audit((IObjectPK)objectUuidPK);
/*      */           }
/*      */         }
/* 1453 */         catch (EASBizException e) {
/* 1454 */           e.printStackTrace();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1460 */     String sqlNo = "select id ,fnumber from eas_lolkk_fk where oafinishsign = 2 and eassign=1 and paystate = 2 and CONVERT(varchar(7), bizdate, 120) >= CONVERT(varchar(7), dateadd(month,-3,getdate()) , 120)  order by bizdate desc";
/* 1461 */     List<Map<String, Object>> listNo = EAISynTemplate.query(ctx, database, sqlNo.toString());
/* 1462 */     for (Map<String, Object> map : listNo) {
/* 1463 */       if (map.get("ID") != null && !"".equals(map.get("ID").toString())) {
/* 1464 */         ObjectUuidPK objectUuidPK = new ObjectUuidPK(map.get("ID").toString());
/*      */         try {
/* 1466 */           PayRequestBillInfo info = ibiz.getPayRequestBillInfo((IObjectPK)objectUuidPK);
/* 1467 */           if (info != null) {
/* 1468 */             if (info.getBillStatus() == BillStatusEnum.SUBMITED) {
/* 1469 */               ibiz.unfreeze((IObjectPK)objectUuidPK, (CoreBillBaseInfo)info);
/* 1470 */               updateBillStatus(ctx, objectUuidPK.toString()); continue;
/* 1471 */             }  if (info.getBillStatus() == BillStatusEnum.AUDITED)
/*      */             {
/* 1473 */               if (!ExistsMapping(ctx, info.getId().toString())) {
/* 1474 */                 ibiz.unpassAudit((IObjectPK)objectUuidPK, (CoreBillBaseInfo)info);
/* 1475 */                 ibiz.unfreeze((IObjectPK)objectUuidPK, (CoreBillBaseInfo)info);
/* 1476 */                 updateBillStatus(ctx, objectUuidPK.toString());
/*      */               } 
/*      */             }
/*      */           } 
/* 1480 */         } catch (EASBizException e) {
/* 1481 */           e.printStackTrace();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }   
/*      */   private static boolean ExistsMapping(Context ctx, String fromid) {
/* 1496 */     boolean isExists = false;
/*      */     
/* 1498 */     String sql = "select count(1) as C from t_bot_relation  where FSrcEntityID = 'D001019A' and FDestEntityID = '40284E81' and FSrcObjectID ='" + fromid + "' ";
/*      */     try {
/* 1500 */       IRowSet rs = DBUtil.executeQuery(ctx, sql);
/* 1501 */       if (rs != null && rs.size() > 0) {
/* 1502 */         while (rs.next()) {
/* 1503 */           if (rs.getObject("C") != null && !"".equals(rs.getObject("C").toString())) {
/* 1504 */             if (Integer.parseInt(rs.getObject("C").toString()) == 0) {
/* 1505 */               isExists = false; continue;
/*      */             } 
/* 1507 */             isExists = true;
/*      */           }
/*      */         
/*      */         } 
/*      */       }
/* 1512 */     } catch (BOSException e) {
/* 1513 */       e.printStackTrace();
/* 1514 */     } catch (SQLException e) {
/* 1515 */       e.printStackTrace();
/*      */     } 
/* 1517 */     return isExists;
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateBillStatus(Context ctx, String fid) {
/* 1522 */     String sql = " update T_AP_PayRequestBill set FBillStatus = 2 where fid ='" + fid + "' and FBillStatus = 4";
/*      */     try {
/* 1524 */       DBUtil.execute(ctx, sql);
/* 1525 */       StringBuffer sbr = new StringBuffer("update t_ap_Otherbillentry set FLockUnVerifyAmt = FRecievePayAmount, FLockUnVerifyAmtLocal = FRecievePayAmountLocal  ,FLockVerifyAmt = 0, FLockVerifyAmtLocal= 0 ");
/* 1526 */       sbr.append(" where  FPARENTID ='").append(fid).append("'");
/* 1527 */       DBUtil.execute(ctx, sbr);
/* 1528 */     } catch (BOSException e) {
/* 1529 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void mobilePaymentBillBizDate(Context ctx, String ids, String date, String type) throws BOSException {
/* 1536 */     if (type != null && "cas".equals(type)) {
/* 1537 */       if (VerifyUtil.notNull(ids)) {
/* 1538 */         String sql = "/*dialect*/ update T_CAS_PaymentBill set cfoldbizdate = FBizDate, FBillDate = to_date('" + date + "','yyyy-mm-dd'),FBizDate = to_date('" + date + "','yyyy-mm-dd') WHERE FID in (" + ids + ")";
/* 1539 */         DBUtil.execute(ctx, sql);
/*      */       } 
/* 1541 */     } else if (type != null && "other".equals(type) && 
/* 1542 */       VerifyUtil.notNull(ids)) {
/* 1543 */       String sql = "/*dialect*/ update T_AP_OtherBill set FBillDate = to_date('" + date + "','yyyy-mm-dd'),FBizDate = to_date('" + date + "','yyyy-mm-dd') WHERE FID in (" + ids + ")";
/* 1544 */       DBUtil.execute(ctx, sql);
/*      */     } 
/*      */   }
/*      */   private String apOtherNotShichang(Context ctx, String database, Map<String, Object> map) throws BOSException {
/* 1558 */     String fid = null;
/*      */     try {
/* 1560 */       String faccount = "2241.96";
/* 1561 */       String actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 1562 */       Boolean addOrUpdate = Boolean.valueOf(false);
/* 1563 */       fid = map.get("ID").toString();
/* 1564 */       OtherBillInfo info = null;
/* 1565 */       System.out.println("_--------------------------------------" + map.get("FNUMBER") + "====" + map.get("COMPANY") + "-----" + map.get("SUPPLIER"));
/* 1566 */       if (map.get("COMPANY") == null || map.get("COMPANY").toString().equals("")) {
/* 1567 */         System.out.println("_--------------------------------------" + map.get("FNUMBER"));
/* 1568 */         updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 1569 */         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败," + info.getNumber() + "的公司编码为空");
/* 1570 */         return "";
/*      */       } 
/* 1572 */       if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {
/*      */         
/* 1574 */         if (OtherBillFactory.getLocalInstance(ctx).exists(
/* 1575 */             "where caigoushenqingdandanhao ='" + 
/* 1576 */             map.get("FNUMBER") + "'")) {
/* 1577 */           updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
/* 1578 */           return "";
/*      */         } 
/* 1580 */         info = new OtherBillInfo();
/*      */       } else {
/*      */         
/* 1583 */         info = new OtherBillInfo();
/*      */       } 
/* 1585 */       info.setIsReversed(false);
/* 1586 */       info.setIsReverseBill(false);
/* 1587 */       info.setIsTransBill(false);
/* 1588 */       info.setIsAllowanceBill(false);
/* 1589 */       info.setIsImportBill(false);
/* 1590 */       info.setIsExchanged(false);
/* 1591 */       info.setIsInitializeBill(false);
/* 1592 */       PaymentTypeInfo paymentTypeInfo = 
/* 1593 */         PaymentTypeFactory.getLocalInstance(ctx)
/* 1594 */         .getPaymentTypeInfo(
/* 1595 */           (IObjectPK)new ObjectUuidPK(
/* 1596 */             "2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
/* 1597 */       info.setPaymentType(paymentTypeInfo);
/* 1598 */       info.setBillType(OtherBillType.OtherPay);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1604 */       CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
/* 1605 */         .getCurrencyCollection("where number='BB01'").get(0);
/*      */       
/* 1607 */       ObjectUuidPK orgPK = new ObjectUuidPK(
/* 1608 */           map.get("COMPANY").toString());
/* 1609 */       CompanyOrgUnitInfo xmcompany = 
/* 1610 */         CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
/* 1611 */       info.setCompany(xmcompany);
/* 1612 */       System.out.println("------------------所属公司：" + 
/* 1613 */           xmcompany.getId() + "----" + xmcompany.getName());
/*      */ 
/*      */       
/* 1616 */       AdminOrgUnitInfo admin = null;
/* 1617 */       if (map.get("DEPT") != null && !"".equals(map.get("DEPT"))) {
/* 1618 */         admin = 
/* 1619 */           AdminOrgUnitFactory.getLocalInstance(ctx)
/* 1620 */           .getAdminOrgUnitInfo(
/* 1621 */             (IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
/* 1622 */         info.setAdminOrgUnit(admin);
/* 1623 */         CostCenterOrgUnitInfo CostCenter = 
/* 1624 */           CostCenterOrgUnitFactory.getLocalInstance(ctx)
/* 1625 */           .getCostCenterOrgUnitInfo(
/* 1626 */             (IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
/* 1627 */         info.setCostCenter(CostCenter);
/*      */       } 
/*      */       
/* 1630 */       if (map.get("YHZH") != null && !"".equals(map.get("YHZH").toString())) {
/* 1631 */         info.put("yinhangzhanghao", map.get("YHZH"));
/*      */       }
/* 1633 */       if (map.get("APPLYERBANKNUM") != null && !"".equals(map.get("APPLYERBANKNUM").toString())) {
/* 1634 */         info.setRecAccountBank(map.get("APPLYERBANKNUM").toString());
/*      */       }
/*      */       
/* 1637 */       if (map.get("KHH") != null && !"".equals(map.get("KHH").toString())) {
/* 1638 */         info.put("kaihuhang", map.get("KHH"));
/*      */       }
/* 1640 */       if (map.get("APPLYERBANK") != null && !"".equals(map.get("APPLYERBANK").toString())) {
/* 1641 */         info.setRecBank(map.get("APPLYERBANK").toString());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1649 */       PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo((IObjectPK)orgPK);
/* 1650 */       info.setPurOrg(purchaseorginfo);
/*      */       
/* 1652 */       String personId = getPersonIdByNumber(ctx, map.get("APPLYER").toString());
/* 1653 */       ObjectUuidPK objectUuidPK1 = new ObjectUuidPK(BOSUuid.read(personId));
/* 1654 */       PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo((IObjectPK)objectUuidPK1);
/* 1655 */       info.setPerson(person);
/*      */ 
/*      */ 
/*      */       
/* 1659 */       SettlementTypeInfo settlementTypeInfo = 
/* 1660 */         SettlementTypeFactory.getLocalInstance(ctx)
/* 1661 */         .getSettlementTypeInfo(
/* 1662 */           (IObjectPK)new ObjectUuidPK(
/* 1663 */             "e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
/* 1664 */       info.setSettleType(settlementTypeInfo);
/*      */       
/* 1666 */       SimpleDateFormat formmat = new SimpleDateFormat(
/* 1667 */           "yyyy-MM-dd hh:mm:ss");
/* 1668 */       Date bizDate = null;
/*      */       try {
/* 1670 */         if (map.get("BIZDATE") != null && 
/* 1671 */           !"".equals(map.get("BIZDATE").toString())) {
/* 1672 */           bizDate = formmat.parse(map.get("BIZDATE").toString());
/*      */         } else {
/* 1674 */           bizDate = new Date();
/*      */         } 
/* 1676 */       } catch (ParseException e) {
/*      */         
/* 1678 */         e.printStackTrace();
/*      */       } 
/* 1680 */       info.setBizDate(bizDate);
/* 1681 */       info.setBillDate(new Date());
/* 1682 */       info.setCurrency(currency);
/*      */ 
/*      */ 
/*      */       
/* 1686 */       info.setExchangeRate(new BigDecimal("1.00"));
/* 1687 */       OtherBillType otherBillType = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1699 */       otherBillType = OtherBillType.OtherPay;
/* 1700 */       info.setBillType(otherBillType);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1706 */       info.put("caigoushenqingdandanhao", map.get("FNUMBER"));
/*      */       
/* 1708 */       info.put("OAcaigoushenqingdanjine", map.get("AMOUNT"));
/* 1709 */       String jk = null;
/* 1710 */       if (map.get("ISLOAN").toString().equals("0")) {
/* 1711 */         jk = "否";
/* 1712 */       } else if (map.get("ISLOAN").toString().equals("1")) {
/* 1713 */         jk = "是";
/*      */       } 
/*      */       
/* 1716 */       info.put("shifoujiekuan", jk);
/*      */       
/* 1718 */       String zlf = null;
/* 1719 */       if (map.get("ISRENTALFEE").toString().equals("0")) {
/* 1720 */         zlf = "否";
/* 1721 */       } else if (map.get("ISRENTALFEE").toString().equals("1")) {
/* 1722 */         zlf = "是";
/*      */       } 
/*      */       
/* 1725 */       info.put("shifouzulinfei", zlf);
/*      */       
/* 1727 */       String djlx = null;
/*      */       
/* 1729 */       String isAdminDept = "0";
/* 1730 */       String[] deptArry = { "企划部", "渠道部", "网电部", "网络部", "新媒体部", "咨询部", "营销中心" };
/* 1731 */       if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04") || map.get("PURCHTYPE").toString().equals("09")) {
/* 1732 */         djlx = "费用报销";
/* 1733 */         actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
/* 1734 */         faccount = "2241.97";
/* 1735 */         if (admin != null && admin.getName() != null) {
/* 1736 */           if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
/* 1737 */             isAdminDept = "2";
/*      */           } else {
/* 1739 */             isAdminDept = "1";
/*      */           }
/*      */         
/*      */         }
/* 1743 */       } else if (map.get("PURCHTYPE").toString().equals("02")) {
/* 1744 */         djlx = "采购付款";
/* 1745 */         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 1746 */         faccount = "2241.96";
/* 1747 */       } else if (map.get("PURCHTYPE").toString().equals("03")) {
/* 1748 */         djlx = "市场投放";
/* 1749 */         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 1750 */         faccount = "2241.96";
/* 1751 */       } else if (map.get("PURCHTYPE").toString().equals("04")) {
/* 1752 */         djlx = "差旅费报销";
/* 1753 */         actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
/* 1754 */         faccount = "2241.97";
/* 1755 */         if (admin != null && admin.getName() != null) {
/* 1756 */           if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
/* 1757 */             isAdminDept = "2";
/*      */           } else {
/* 1759 */             isAdminDept = "1";
/*      */           } 
/*      */         }
/* 1762 */       } else if (map.get("PURCHTYPE").toString().equals("05") || map.get("PURCHTYPE").toString().equals("10")) {
/* 1763 */         djlx = "对外付款";
/* 1764 */         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 1765 */         faccount = "2241.96";
/* 1766 */       } else if (map.get("PURCHTYPE").toString().equals("06")) {
/* 1767 */         djlx = "合同专用付款";
/* 1768 */         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 1769 */         faccount = "2241.96";
/* 1770 */       } else if (map.get("PURCHTYPE").toString().equals("07")) {
/* 1771 */         djlx = "技加工";
/* 1772 */         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 1773 */         faccount = "2241.96";
/*      */       } 
/*      */ 
/*      */       
/* 1777 */       info.put("yingfudanjuleixing", djlx);
/* 1778 */       info.put("shifouguanlibumen", isAdminDept);
/* 1779 */       info.put("fapiaohao", "OA0000");
/*      */       
/* 1781 */       String companytype = SupplyInfoLogUnit.getComapnyTypeByNumber(ctx, xmcompany.getNumber());
/* 1782 */       if (companytype != null && !"".equals(companytype)) {
/* 1783 */         info.put("CompanyType", companytype);
/*      */       }
/*      */       
/* 1786 */       AccountViewInfo accountInfo = new AccountViewInfo();
/* 1787 */       accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
/* 1788 */           "where number = '" + faccount + "' and companyID ='" + map.get("COMPANY").toString() + "' ");
/*      */ 
/*      */       
/* 1791 */       AsstActTypeInfo actType = AsstActTypeFactory.getLocalInstance(ctx).getAsstActTypeInfo((IObjectPK)new ObjectUuidPK(actTypePk));
/* 1792 */       info.setAsstActType(actType);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1797 */       if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04") || map.get("PURCHTYPE").toString().equals("09")) {
/* 1798 */         info.setAsstActID(person.getId().toString());
/* 1799 */         info.setAsstActName(person.getName());
/* 1800 */         info.setAsstActNumber(person.getNumber());
/*      */       } else {
/*      */         try {
/* 1803 */           SupplierInfo supplierInfo = 
/* 1804 */             SupplierFactory.getLocalInstance(ctx).getSupplierInfo(
/* 1805 */               " where number='" + map.get("SUPPLIERID") + "'");
/* 1806 */           info.setAsstActID(supplierInfo.getId().toString());
/* 1807 */           info.setAsstActName(supplierInfo.getName());
/* 1808 */           info.setAsstActNumber(supplierInfo.getNumber());
/* 1809 */         } catch (Exception e) {
/* 1810 */           updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 1811 */           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 1812 */               DateBasetype.OA_OtherBill, info.getNumber(), 
/* 1813 */               info.getString("OA_OtherBill"), "没有该供应商编码");
/* 1814 */           return "";
/*      */         } 
/*      */       } 
/*      */       
/* 1818 */       VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
/* 1819 */       info.setSourceBillType(billTypeEnum);
/*      */       
/* 1821 */       BizTypeInfo bizTypeInfo = BizTypeFactory.getLocalInstance(ctx)
/* 1822 */         .getBizTypeInfo("where number = 110");
/* 1823 */       info.setBizType(bizTypeInfo);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1831 */       String sql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid =" + 
/* 1832 */         map.get("ID");
/* 1833 */       List<Map<String, Object>> list1 = EAISynTemplate.query(ctx, 
/* 1834 */           database, sql);
/* 1835 */       BigDecimal totalAmount = new BigDecimal(0);
/* 1836 */       BigDecimal totalyjk = new BigDecimal(0);
/* 1837 */       BigDecimal totalytbk = new BigDecimal(0);
/* 1838 */       if (list1 != null && list1.size() > 0) {
/* 1839 */         for (Map<String, Object> map1 : list1) {
/* 1840 */           OtherBillentryInfo entryInfo = new OtherBillentryInfo();
/*      */           
/* 1842 */           if (map1.get("PAYTYPECODE") != null && !"".equals(map1.get("PAYTYPECODE").toString())) {
/* 1843 */             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='" + map1.get("PAYTYPECODE").toString() + "'");
/* 1844 */             entryInfo.setExpenseItem(typeinfo);
/*      */           } else {
/* 1846 */             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
/* 1847 */             entryInfo.setExpenseItem(typeinfo);
/*      */           } 
/*      */           
/* 1850 */           BigDecimal qty = new BigDecimal(map1.get("QTY").toString());
/* 1851 */           BigDecimal price = new BigDecimal(map1.get("PRICE").toString());
/* 1852 */           BigDecimal amount = new BigDecimal(map1.get("AMOUNT").toString());
/*      */           
/* 1854 */           if (price.compareTo(BigDecimal.ZERO) < 0) {
/* 1855 */             price = price.negate();
/* 1856 */             qty = qty.negate();
/*      */           } 
/* 1858 */           entryInfo.setPrice(price);
/* 1859 */           entryInfo.setTaxPrice(price);
/* 1860 */           entryInfo.setActualPrice(price);
/* 1861 */           entryInfo.setRealPrice(price);
/* 1862 */           entryInfo.setQuantity(qty);
/* 1863 */           entryInfo.setBaseQty(BigDecimal.ZERO);
/* 1864 */           entryInfo.setDiscountRate(BigDecimal.ZERO);
/* 1865 */           entryInfo.setDiscountAmount(BigDecimal.ZERO);
/* 1866 */           entryInfo.setDiscountAmountLocal(BigDecimal.ZERO);
/* 1867 */           entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO);
/* 1868 */           entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO);
/* 1869 */           entryInfo.setAssistQty(BigDecimal.ZERO);
/* 1870 */           entryInfo.setWittenOffBaseQty(BigDecimal.ZERO);
/* 1871 */           entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO);
/* 1872 */           entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO);
/* 1873 */           entryInfo.setVerifyQty(BigDecimal.ZERO);
/* 1874 */           entryInfo.setLockVerifyQty(BigDecimal.ZERO);
/* 1875 */           entryInfo.setLocalUnwriteOffAmount(amount);
/* 1876 */           entryInfo.setAmount(amount);
/* 1877 */           entryInfo.setAmountLocal(amount);
/* 1878 */           entryInfo.setTaxAmount(BigDecimal.ZERO);
/* 1879 */           entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
/* 1880 */           entryInfo.setTaxRate(BigDecimal.ZERO);
/* 1881 */           entryInfo.setUnVerifyAmount(amount);
/* 1882 */           entryInfo.setUnVerifyAmountLocal(amount);
/* 1883 */           entryInfo.setLockUnVerifyAmt(amount);
/* 1884 */           entryInfo.setLockUnVerifyAmtLocal(amount);
/*      */           
/* 1886 */           entryInfo.setApportionAmount(BigDecimal.ZERO);
/* 1887 */           entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
/* 1888 */           entryInfo.setUnApportionAmount(amount);
/* 1889 */           entryInfo.setRecievePayAmount(amount);
/* 1890 */           entryInfo.setRecievePayAmountLocal(amount);
/* 1891 */           entryInfo.setCompany(map.get("COMPANY").toString());
/* 1892 */           entryInfo.setAccount(accountInfo);
/*      */           
/* 1894 */           if (map1.get("REMARK") != null) {
/* 1895 */             entryInfo.setRemark(map1.get("REMARK").toString());
/*      */           }
/* 1897 */           if (map1.get("YJK") != null && !"".equals(map1.get("YJK").toString()))
/* 1898 */             totalyjk = totalyjk.add((BigDecimal)map1.get("YJK")); 
/* 1899 */           if (map1.get("YTBK") != null && !"".equals(map1.get("YTBK").toString()))
/* 1900 */             totalytbk = totalytbk.add((BigDecimal)map1.get("YTBK")); 
/* 1901 */           entryInfo.setParent((ArApBillBaseInfo)info);
/* 1902 */           entryInfo.setUnwriteOffBaseQty(qty);
/* 1903 */           entryInfo.put("pinpai", map.get("BRAND"));
/* 1904 */           entryInfo.put("huohao", map.get("ATRNO"));
/* 1905 */           totalAmount = totalAmount.add(amount);
/* 1906 */           info.getEntries().addObject((IObjectValue)entryInfo);
/*      */         } 
/*      */       } else {
/* 1909 */         System.out.println("entrty is empty _--------------------------------------" + map.get("FNUMBER"));
/* 1910 */         updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 1911 */         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 1912 */             DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据没有分录");
/* 1913 */         return "";
/*      */       } 
/* 1915 */       info.setAmount(totalAmount);
/* 1916 */       info.setTotalTax(BigDecimal.ZERO);
/* 1917 */       info.setTotalTaxAmount(totalAmount);
/* 1918 */       info.setTotalAmount(totalAmount);
/* 1919 */       info.setAmountLocal(totalAmount);
/* 1920 */       info.setTotalAmountLocal(totalAmount);
/* 1921 */       info.setTotalTaxLocal(BigDecimal.ZERO);
/* 1922 */       info.setThisApAmount(totalAmount);
/* 1923 */       info.setUnVerifyAmount(totalAmount);
/* 1924 */       info.setUnVerifyAmountLocal(totalAmount);
/* 1925 */       info.put("yingtuibukuan", totalytbk);
/* 1926 */       info.put("yuanjiekuan", totalyjk);
/* 1927 */       OtherBillPlanInfo otherBillPlanInfo = new OtherBillPlanInfo();
/* 1928 */       otherBillPlanInfo.setLockAmount(totalAmount);
/* 1929 */       otherBillPlanInfo.setLockAmountLoc(info.getAmountLocal());
/* 1930 */       otherBillPlanInfo.setRecievePayAmount(totalAmount);
/* 1931 */       otherBillPlanInfo.setRecievePayAmountLocal(info.getAmountLocal());
/* 1932 */       info.getPayPlan().add(otherBillPlanInfo);
/* 1933 */       OtherBillFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
/* 1934 */       updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
/*      */       try {
/* 1936 */         System.out.println("------------------info所属公司1111：" + 
/* 1937 */             info.getCompany().getId() + "----" + info.getCompany().getName());
/* 1938 */         info.setBillStatus(BillStatusEnum.SAVE);
/* 1939 */         OtherBillFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
/* 1940 */         System.out.println("------------------info所属公司2222：" + 
/* 1941 */             info.getCompany().getId() + "----" + info.getCompany().getName());
/*      */         
/* 1943 */         OtherBillFactory.getLocalInstance(ctx).audit((IObjectPK)new ObjectUuidPK(info.getId().toString()));
/*      */         
/* 1945 */         if (addOrUpdate.booleanValue()) {
/* 1946 */           AppUnit.insertLog(ctx, DateBaseProcessType.Update, 
/* 1947 */               DateBasetype.OA_OtherBill, info.getNumber(), 
/* 1948 */               info.getString("OA_OtherBill"), "单据修改成功");
/*      */         } else {
/* 1950 */           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 1951 */               DateBasetype.OA_OtherBill, info.getNumber(), 
/* 1952 */               info.getString("OA_OtherBill"), "单据审核成功");
/*      */         } 
/* 1954 */       } catch (Exception e2) {
/*      */         
/* 1956 */         logger.error(e2.getMessage());
/* 1957 */         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 1958 */             DateBasetype.OA_OtherBill, info.getNumber(), 
/* 1959 */             info.getString("OA_OtherBill"), "单据保存成功，提交审核失败。");
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1964 */     catch (EASBizException e) {
/* 1965 */       e.printStackTrace();
/* 1966 */       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 1967 */           DateBasetype.OA_OtherBill, String.valueOf(String.valueOf(fid)) + "单据保存失败", e.getMessage());
/* 1968 */       if (fid != null && !fid.equals("")) {
/* 1969 */         updateFSign(ctx, database, "eas_lolkk_bx", 2, fid);
/*      */       }
/* 1971 */       String msg = "运行失败，异常是：" + e.toString();
/* 1972 */       return msg;
/*      */     } 
/* 1974 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String apOtherIsShichang(Context ctx, String database, Map<String, Object> map) throws BOSException {
/* 1986 */     String fid = null;
/*      */     try {
/* 1988 */       String faccount = "2241.96";
/* 1989 */       String actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 1990 */       fid = map.get("ID").toString();
/* 1991 */       OtherBillInfo info = new OtherBillInfo();
/*      */ 
/*      */       
/* 1994 */       String sql = "select parentID,fnumber,company,Dept,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid =" + 
/* 1995 */         map.get("ID");
/* 1996 */       List<Map<String, Object>> list1 = EAISynTemplate.query(ctx, database, sql);
/*      */       
/* 1998 */       if (list1 != null && list1.size() > 0) {
/* 1999 */         for (Map<String, Object> map1 : list1) {
/* 2000 */           info = new OtherBillInfo();
/* 2001 */           BigDecimal totalyjk = new BigDecimal(0);
/* 2002 */           BigDecimal totalytbk = new BigDecimal(0);
/* 2003 */           Boolean addOrUpdate = Boolean.valueOf(false);
/* 2004 */           if (map1.get("COMPANY") == null || map1.get("COMPANY").toString().equals("")) {
/* 2005 */             System.out.println("_--------------------------------------" + map.get("FNUMBER"));
/* 2006 */             updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 2007 */             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败," + info.getNumber() + "的分录上的公司编码为空");
/* 2008 */             return "";
/*      */           } 
/* 2010 */           if (map1.get("DEPT") == null || map1.get("DEPT").toString().equals("")) {
/* 2011 */             System.out.println("_--------------------------------------" + map.get("FNUMBER"));
/* 2012 */             updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 2013 */             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败," + info.getNumber() + "的分录上的部门编码为空");
/* 2014 */             return "";
/*      */           } 
/* 2016 */           if (map1.get("FNUMBER") == null || map1.get("FNUMBER").toString().equals("")) {
/* 2017 */             System.out.println("_--------------------------------------" + map.get("FNUMBER"));
/* 2018 */             updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 2019 */             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败," + info.getNumber() + "的分录上的编码为空");
/* 2020 */             return "";
/*      */           } 
/*      */           
/* 2023 */           AccountViewInfo accountInfo = new AccountViewInfo();
/* 2024 */           accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
/* 2025 */               "where number = '" + faccount + "' and companyID ='" + map1.get("COMPANY").toString() + "' ");
/*      */           
/* 2027 */           OtherBillentryInfo entryInfo = new OtherBillentryInfo();
/*      */           
/* 2029 */           if (map1.get("PAYTYPECODE") != null && !"".equals(map1.get("PAYTYPECODE").toString())) {
/* 2030 */             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx)
/* 2031 */               .getExpenseTypeInfo("where number ='" + map1.get("PAYTYPECODE").toString() + "'");
/* 2032 */             entryInfo.setExpenseItem(typeinfo);
/*      */           } else {
/* 2034 */             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
/* 2035 */             entryInfo.setExpenseItem(typeinfo);
/*      */           } 
/*      */           
/* 2038 */           BigDecimal qty = new BigDecimal(map1.get("QTY").toString());
/* 2039 */           BigDecimal price = new BigDecimal(map1.get("PRICE").toString());
/* 2040 */           BigDecimal amount = new BigDecimal(map1.get("AMOUNT").toString());
/*      */           
/* 2042 */           if (price.compareTo(BigDecimal.ZERO) < 0) {
/* 2043 */             price = price.negate();
/* 2044 */             qty = qty.negate();
/*      */           } 
/*      */           
/* 2047 */           entryInfo.setPrice(price);
/* 2048 */           entryInfo.setTaxPrice(price);
/* 2049 */           entryInfo.setActualPrice(price);
/* 2050 */           entryInfo.setRealPrice(price);
/* 2051 */           entryInfo.setQuantity(qty);
/* 2052 */           entryInfo.setBaseQty(BigDecimal.ZERO);
/* 2053 */           entryInfo.setDiscountRate(BigDecimal.ZERO);
/* 2054 */           entryInfo.setDiscountAmount(BigDecimal.ZERO);
/* 2055 */           entryInfo.setDiscountAmountLocal(BigDecimal.ZERO);
/* 2056 */           entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO);
/* 2057 */           entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO);
/* 2058 */           entryInfo.setAssistQty(BigDecimal.ZERO);
/* 2059 */           entryInfo.setWittenOffBaseQty(BigDecimal.ZERO);
/* 2060 */           entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO);
/* 2061 */           entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO);
/* 2062 */           entryInfo.setVerifyQty(BigDecimal.ZERO);
/* 2063 */           entryInfo.setLockVerifyQty(BigDecimal.ZERO);
/* 2064 */           entryInfo.setLocalUnwriteOffAmount(amount);
/* 2065 */           entryInfo.setAmount(amount);
/* 2066 */           entryInfo.setAmountLocal(amount);
/* 2067 */           entryInfo.setTaxAmount(BigDecimal.ZERO);
/* 2068 */           entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
/* 2069 */           entryInfo.setTaxRate(BigDecimal.ZERO);
/* 2070 */           entryInfo.setUnVerifyAmount(amount);
/* 2071 */           entryInfo.setUnVerifyAmountLocal(amount);
/* 2072 */           entryInfo.setLockUnVerifyAmt(amount);
/* 2073 */           entryInfo.setLockUnVerifyAmtLocal(amount);
/*      */           
/* 2075 */           entryInfo.setApportionAmount(BigDecimal.ZERO);
/* 2076 */           entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
/* 2077 */           entryInfo.setUnApportionAmount(amount);
/* 2078 */           entryInfo.setRecievePayAmount(amount);
/* 2079 */           entryInfo.setRecievePayAmountLocal(amount);
/* 2080 */           entryInfo.setCompany(map1.get("COMPANY").toString());
/* 2081 */           entryInfo.setAccount(accountInfo);
/* 2082 */           if (map1.get("YJK") != null && !"".equals(map1.get("YJK")))
/* 2083 */             totalyjk = (BigDecimal)map1.get("YJK"); 
/* 2084 */           if (map1.get("YTBK") != null && !"".equals(map1.get("YTBK"))) {
/* 2085 */             totalytbk = (BigDecimal)map1.get("YTBK");
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2093 */           entryInfo.setUnwriteOffBaseQty(qty);
/* 2094 */           entryInfo.put("pinpai", map.get("BRAND"));
/* 2095 */           entryInfo.put("huohao", map.get("ATRNO"));
/*      */           
/* 2097 */           if (map1.get("REMARK") != null) {
/* 2098 */             entryInfo.setRemark(map1.get("REMARK").toString());
/*      */           }
/* 2100 */           if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {
/*      */             
/* 2102 */             if (OtherBillFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='" + map.get("FNUMBER") + "_" + map1.get("FNUMBER") + "'")) {
/*      */               continue;
/*      */             }
/*      */             
/* 2106 */             info = new OtherBillInfo();
/*      */           } else {
/*      */             
/* 2109 */             info = new OtherBillInfo();
/*      */           } 
/* 2111 */           entryInfo.setParent((ArApBillBaseInfo)info);
/* 2112 */           info.getEntries().addObject((IObjectValue)entryInfo);
/*      */           
/* 2114 */           info.setIsReversed(false);
/* 2115 */           info.setIsReverseBill(false);
/* 2116 */           info.setIsTransBill(false);
/* 2117 */           info.setIsAllowanceBill(false);
/* 2118 */           info.setIsImportBill(false);
/* 2119 */           info.setIsExchanged(false);
/* 2120 */           info.setIsInitializeBill(false);
/*      */           
/* 2122 */           PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx)
/* 2123 */             .getPaymentTypeInfo((IObjectPK)new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
/* 2124 */           info.setPaymentType(paymentTypeInfo);
/*      */           
/* 2126 */           info.setBillType(OtherBillType.OtherPay);
/*      */           
/* 2128 */           ObjectUuidPK orgPK = new ObjectUuidPK(map1.get("COMPANY").toString());
/* 2129 */           CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
/* 2130 */           info.setCompany(company);
/* 2131 */           System.out.println("------------------所属公司：" + company.getId() + "----" + company.getName());
/*      */ 
/*      */           
/* 2134 */           AdminOrgUnitInfo admin = null;
/* 2135 */           if (map1.get("DEPT") != null && !"".equals(map1.get("DEPT"))) {
/* 2136 */             admin = AdminOrgUnitFactory.getLocalInstance(ctx)
/* 2137 */               .getAdminOrgUnitInfo((IObjectPK)new ObjectUuidPK(map1.get("DEPT").toString()));
/* 2138 */             info.setAdminOrgUnit(admin);
/* 2139 */             CostCenterOrgUnitInfo CostCenter = CostCenterOrgUnitFactory.getLocalInstance(ctx)
/* 2140 */               .getCostCenterOrgUnitInfo((IObjectPK)new ObjectUuidPK(map1.get("DEPT").toString()));
/* 2141 */             info.setCostCenter(CostCenter);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 2146 */           if (map.get("YHZH") != null && !"".equals(map.get("YHZH").toString())) {
/* 2147 */             info.put("yinhangzhanghao", map.get("YHZH"));
/*      */           }
/* 2149 */           if (map.get("APPLYERBANKNUM") != null && !"".equals(map.get("APPLYERBANKNUM").toString())) {
/* 2150 */             info.setRecAccountBank(map.get("APPLYERBANKNUM").toString());
/*      */           }
/*      */           
/* 2153 */           if (map.get("KHH") != null && !"".equals(map.get("KHH").toString())) {
/* 2154 */             info.put("kaihuhang", map.get("KHH"));
/*      */           }
/* 2156 */           if (map.get("APPLYERBANK") != null && !"".equals(map.get("APPLYERBANK").toString())) {
/* 2157 */             info.setRecBank(map.get("APPLYERBANK").toString());
/*      */           }
/*      */ 
/*      */           
/* 2161 */           PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo((IObjectPK)orgPK);
/* 2162 */           info.setPurOrg(purchaseorginfo);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2168 */           String personId = getPersonIdByNumber(ctx, map.get("APPLYER").toString());
/* 2169 */           ObjectUuidPK objectUuidPK1 = new ObjectUuidPK(BOSUuid.read(personId));
/* 2170 */           PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo((IObjectPK)objectUuidPK1);
/* 2171 */           info.setPerson(person);
/*      */ 
/*      */           
/* 2174 */           SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
/* 2175 */             .getSettlementTypeInfo((IObjectPK)new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
/* 2176 */           info.setSettleType(settlementTypeInfo);
/*      */ 
/*      */ 
/*      */           
/* 2180 */           SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 2181 */           Date bizDate = null;
/*      */           try {
/* 2183 */             if (map.get("BIZDATE") != null && !"".equals(map.get("BIZDATE").toString())) {
/* 2184 */               bizDate = formmat.parse(map.get("BIZDATE").toString());
/*      */             } else {
/* 2186 */               bizDate = new Date();
/*      */             } 
/* 2188 */           } catch (ParseException e) {
/* 2189 */             e.printStackTrace();
/*      */           } 
/*      */           
/* 2192 */           info.setBizDate(bizDate);
/* 2193 */           info.setBillDate(new Date());
/*      */           
/* 2195 */           CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
/* 2196 */           info.setCurrency(currency);
/*      */ 
/*      */           
/* 2199 */           info.setExchangeRate(new BigDecimal("1.00"));
/*      */           
/* 2201 */           OtherBillType otherBillType = null;
/*      */ 
/*      */           
/* 2204 */           otherBillType = OtherBillType.OtherPay;
/* 2205 */           info.setBillType(otherBillType);
/*      */ 
/*      */ 
/*      */           
/* 2209 */           info.put("caigoushenqingdandanhao", (new StringBuilder()).append(map.get("FNUMBER")).append("_").append(map1.get("FNUMBER")).toString());
/*      */ 
/*      */           
/* 2212 */           info.put("OAcaigoushenqingdanjine", map1.get("AMOUNT"));
/*      */           
/* 2214 */           String companType = SupplyInfoLogUnit.getComapnyTypeByNumber(ctx, company.getNumber());
/* 2215 */           if (companType != null && !"".equals(companType)) {
/* 2216 */             info.put("CompanyType", companType);
/*      */           }
/*      */           
/* 2219 */           String jk = null;
/* 2220 */           if (map.get("ISLOAN").toString().equals("0")) {
/* 2221 */             jk = "否";
/* 2222 */           } else if (map.get("ISLOAN").toString().equals("1")) {
/* 2223 */             jk = "是";
/*      */           } 
/*      */           
/* 2226 */           info.put("shifoujiekuan", jk);
/*      */           
/* 2228 */           String zlf = null;
/* 2229 */           if (map.get("ISRENTALFEE").toString().equals("0")) {
/* 2230 */             zlf = "否";
/* 2231 */           } else if (map.get("ISRENTALFEE").toString().equals("1")) {
/* 2232 */             zlf = "是";
/*      */           } 
/*      */           
/* 2235 */           info.put("shifouzulinfei", zlf);
/*      */           
/* 2237 */           String djlx = null;
/* 2238 */           String isAdminDept = "0";
/* 2239 */           String[] deptArry = { "企划部", "渠道部", "网电部", "网络部", "新媒体部", "咨询部", "营销中心" };
/* 2240 */           if (map.get("PURCHTYPE").toString().equals("01")) {
/* 2241 */             djlx = "费用报销";
/* 2242 */             actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
/* 2243 */             faccount = "2241.97";
/* 2244 */             if (admin != null && admin.getName() != null) {
/* 2245 */               if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
/* 2246 */                 isAdminDept = "2";
/*      */               } else {
/* 2248 */                 isAdminDept = "1";
/*      */               }
/*      */             
/*      */             }
/* 2252 */           } else if (map.get("PURCHTYPE").toString().equals("02")) {
/* 2253 */             djlx = "采购付款";
/* 2254 */             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 2255 */             faccount = "2241.96";
/* 2256 */           } else if (map.get("PURCHTYPE").toString().equals("03")) {
/* 2257 */             djlx = "市场投放";
/* 2258 */             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 2259 */             faccount = "2241.96";
/* 2260 */           } else if (map.get("PURCHTYPE").toString().equals("04")) {
/* 2261 */             djlx = "差旅费报销";
/* 2262 */             actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
/* 2263 */             faccount = "2241.97";
/* 2264 */             if (admin != null && admin.getName() != null) {
/* 2265 */               if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
/* 2266 */                 isAdminDept = "2";
/*      */               } else {
/* 2268 */                 isAdminDept = "1";
/*      */               } 
/*      */             }
/* 2271 */           } else if (map.get("PURCHTYPE").toString().equals("05")) {
/* 2272 */             djlx = "对外付款";
/* 2273 */             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 2274 */             faccount = "2241.96";
/* 2275 */           } else if (map.get("PURCHTYPE").toString().equals("06")) {
/* 2276 */             djlx = "合同专用付款";
/* 2277 */             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 2278 */             faccount = "2241.96";
/* 2279 */           } else if (map.get("PURCHTYPE").toString().equals("07")) {
/* 2280 */             djlx = "技加工";
/* 2281 */             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
/* 2282 */             faccount = "2241.96";
/*      */           } 
/*      */ 
/*      */           
/* 2286 */           info.put("yingfudanjuleixing", djlx);
/* 2287 */           info.put("shifouguanlibumen", isAdminDept);
/* 2288 */           info.put("fapiaohao", "OA0000");
/*      */ 
/*      */ 
/*      */           
/* 2292 */           AsstActTypeInfo actType = AsstActTypeFactory.getLocalInstance(ctx).getAsstActTypeInfo((IObjectPK)new ObjectUuidPK(actTypePk));
/* 2293 */           info.setAsstActType(actType);
/*      */ 
/*      */           
/* 2296 */           if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04")) {
/* 2297 */             info.setAsstActID(person.getId().toString());
/* 2298 */             info.setAsstActName(person.getName());
/* 2299 */             info.setAsstActNumber(person.getNumber());
/*      */           } else {
/*      */             try {
/* 2302 */               SupplierInfo supplierInfo = SupplierFactory.getLocalInstance(ctx).getSupplierInfo(" where number='" + map.get("SUPPLIERID") + "'");
/* 2303 */               info.setAsstActID(supplierInfo.getId().toString());
/* 2304 */               info.setAsstActName(supplierInfo.getName());
/* 2305 */               info.setAsstActNumber(supplierInfo.getNumber());
/* 2306 */             } catch (Exception e) {
/* 2307 */               updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 2308 */               AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), 
/* 2309 */                   info.getString("OA_OtherBill"), "分录上的" + map.get("NUMBER") + "没有该供应商编码");
/*      */               
/*      */               continue;
/*      */             } 
/*      */           } 
/* 2314 */           VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
/* 2315 */           info.setSourceBillType(billTypeEnum);
/*      */           
/* 2317 */           BizTypeInfo bizTypeInfo = BizTypeFactory.getLocalInstance(ctx).getBizTypeInfo("where number = 110");
/* 2318 */           info.setBizType(bizTypeInfo);
/*      */           
/* 2320 */           info.setAmount(amount);
/* 2321 */           info.setTotalTax(BigDecimal.ZERO);
/* 2322 */           info.setTotalTaxAmount(amount);
/* 2323 */           info.setTotalAmount(amount);
/* 2324 */           info.setAmountLocal(amount);
/* 2325 */           info.setTotalAmountLocal(amount);
/* 2326 */           info.setTotalTaxLocal(BigDecimal.ZERO);
/* 2327 */           info.setThisApAmount(amount);
/* 2328 */           info.setUnVerifyAmount(amount);
/* 2329 */           info.setUnVerifyAmountLocal(amount);
/* 2330 */           info.put("yingtuibukuan", totalytbk);
/* 2331 */           info.put("yuanjiekuan", totalyjk);
/*      */           
/* 2333 */           OtherBillPlanInfo otherBillPlanInfo = new OtherBillPlanInfo();
/* 2334 */           otherBillPlanInfo.setLockAmount(amount);
/* 2335 */           otherBillPlanInfo.setLockAmountLoc(info.getAmountLocal());
/* 2336 */           otherBillPlanInfo.setRecievePayAmount(amount);
/* 2337 */           otherBillPlanInfo.setRecievePayAmountLocal(info.getAmountLocal());
/* 2338 */           info.getPayPlan().add(otherBillPlanInfo);
/*      */           
/* 2340 */           OtherBillFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
/*      */           
/*      */           try {
/* 2343 */             OtherBillFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
/*      */             
/* 2345 */             OtherBillFactory.getLocalInstance(ctx).audit((IObjectPK)new ObjectUuidPK(info.getId().toString()));
/* 2346 */             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 2347 */                 DateBasetype.OA_OtherBill, info.getNumber(), 
/* 2348 */                 info.getString("OA_OtherBill"), "采购申请单单号为" + map.get("FNUMBER") + "_" + map1.get("FNUMBER") + "单据审核成功");
/* 2349 */           } catch (Exception e2) {
/*      */             
/* 2351 */             logger.error(e2.getMessage());
/* 2352 */             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 2353 */                 DateBasetype.OA_OtherBill, info.getNumber(), 
/* 2354 */                 info.getString("OA_OtherBill"), "采购申请单单号为" + map.get("FNUMBER") + "_" + map1.get("FNUMBER") + "单据保存成功,提交审核失败。");
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 2369 */         System.out.println("entrty is empty _--------------------------------------" + map.get("FNUMBER"));
/* 2370 */         updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
/* 2371 */         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, 
/* 2372 */             map.get("FNUMBER").toString(), info.getString("OA_OtherBill"), "单据没有分录");
/* 2373 */         return "";
/*      */       } 
/*      */ 
/*      */       
/* 2377 */       updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
/*      */     }
/* 2379 */     catch (EASBizException e) {
/* 2380 */       e.printStackTrace();
/* 2381 */       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 2382 */           DateBasetype.OA_OtherBill, String.valueOf(String.valueOf(fid)) + "单据保存失败", e.getMessage());
/* 2383 */       if (fid != null && !fid.equals("")) {
/* 2384 */         updateFSign(ctx, database, "eas_lolkk_bx", 2, fid);
/*      */       }
/* 2386 */       String msg = "运行失败，异常是：" + e.toString();
/* 2387 */       return msg;
/*      */     } 
/*      */ 
/*      */     
/* 2391 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateNoPeople(Context ctx, String database) throws BOSException {
/* 2403 */     String updateSql = "UPDATE  eas_lolkk_bx  set eassign = 2 , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='职员不存在'  where ID in (select bx.id from eas_lolkk_bx bx left JOIN EAS_PERSON_MIDTABLE  person on person.FNUMBER = bx.APPLYER where bx.eassign = 0 and person.FNUMBER is null )";
/*      */     
/* 2405 */     System.out.print("--------------" + updateSql);
/* 2406 */     EAISynTemplate.execute(ctx, database, updateSql);
/*      */     
/* 2408 */     updateSql = "update EAS_LOLKK_bx set EASSIGN = -1, EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='PAYTYPECODE为空' where id in (select DISTINCT PARENTID from EAS_LOLKK_BX_SUB where PAYTYPECODE is null ) and EASSIGN = 0";
/* 2409 */     System.out.print("--------------" + updateSql);
/* 2410 */     EAISynTemplate.execute(ctx, database, updateSql);
/*      */     
/* 2412 */     updateSql = "update EAS_LOLKK_bx set EASSIGN = -2, EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='PAYTYPECODE不存在' where id in (select DISTINCT PARENTID from EAS_LOLKK_BX_SUB where PAYTYPECODE not in (select FNUMBER from EAS_PAYTYPE_OA_MIDTABLE)) and EASSIGN = 0";
/* 2413 */     System.out.print("--------------" + updateSql);
/* 2414 */     EAISynTemplate.execute(ctx, database, updateSql);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String updateMidPayStatus(Context ctx) throws BOSException {
/* 2421 */     String noPayBillSql = "select a.id,a.oanumber, a.fbillid ,a.danjuType FROM eas_paymentbillstatus  a where a.status = 0 ";
/* 2422 */     IRowSet noPayBill = DbUtil.executeQuery(ctx, noPayBillSql);
/* 2423 */     String oanumber = null;
/*      */     try {
/* 2425 */       while (noPayBill.next()) {
/* 2426 */         oanumber = noPayBill.getString("OANUMBER");
/* 2427 */         String fbillid = noPayBill.getString("FBILLID");
/* 2428 */         String danjuType = noPayBill.getString("DANJUTYPE");
/* 2429 */         String id = noPayBill.getString("id");
/* 2430 */         String number = oanumber;
/* 2431 */         if (VerifyUtil.notNull(oanumber)) {
/* 2432 */           if (oanumber != null && !"".equals(oanumber) && !"-1".equals(oanumber)) {
/* 2433 */             String sql = null;
/* 2434 */             if (danjuType.equals("市场投放")) {
/* 2435 */               if (oanumber.indexOf("_") != -1 && (oanumber.split("_")).length > 1) {
/*      */                 
/* 2437 */                 oanumber = oanumber.split("_")[1];
/* 2438 */                 sql = "update eas_lolkk_bx_sub set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='" + oanumber + "'";
/*      */               } else {
/* 2440 */                 sql = "update eas_lolkk_bx set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='" + oanumber + "'";
/*      */               } 
/*      */             } else {
/* 2443 */               sql = "update eas_lolkk_bx set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='" + oanumber + "'";
/*      */             } 
/*      */             
/* 2446 */             EAISynTemplate.execute(ctx, "04", sql);
/*      */           } 
/*      */           
/* 2449 */           if (fbillid != null && !"".equals(fbillid) && !"-1".equals(fbillid)) {
/* 2450 */             String sql3 = "update eas_lolkk_fk set Paystate = 1 where id ='" + fbillid + "'";
/*      */             
/* 2452 */             EAISynTemplate.execute(ctx, "03", sql3);
/*      */           } 
/*      */ 
/*      */           
/* 2456 */           String updateStatusSql = " update eas_paymentbillstatus set  STATUS =  1  where id=" + id;
/* 2457 */           DbUtil.execute(ctx, updateStatusSql);
/*      */           
/* 2459 */           AppUnit.insertLog(ctx, DateBaseProcessType.Update, DateBasetype.OA_OtherBill, oanumber, oanumber, "修改付款状态成功");
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 2464 */     } catch (BOSException e) {
/* 2465 */       AppUnit.insertLog(ctx, DateBaseProcessType.Update, DateBasetype.OA_OtherBill, oanumber, oanumber, "修改付款状态异常");
/* 2466 */       e.printStackTrace();
/* 2467 */     } catch (SQLException e) {
/*      */       
/* 2469 */       e.printStackTrace();
/*      */     } 
/* 2471 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _IsExistDownstreamBill(Context ctx, String id) throws BOSException {
/* 2477 */     boolean flag = true;
/*      */     try {
/* 2479 */       flag = AppUnit.isExistDownstreamBill(ctx, id);
/* 2480 */     } catch (EASBizException e) {
/* 2481 */       e.printStackTrace();
/* 2482 */       flag = true;
/*      */     } 
/* 2484 */     return flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _PurvspJDFromOA(Context ctx, String database) throws BOSException {
/* 2492 */     VSPJDSupport.savePurRequest(ctx, database);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _ReceConfirmVSPJD(Context ctx, String database) throws BOSException {
/* 2502 */     VSPJDSupport.savePurReceivalBill(ctx, database);
/*      */   }
/*      */   
/*      */   private static Timestamp string2Time(String dateString) throws ParseException {
/* 2506 */     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
/* 2507 */     dateFormat.setLenient(false);
/* 2508 */     Date timeDate = dateFormat.parse(dateString);
/* 2509 */     Timestamp dateTime = new Timestamp(timeDate.getTime());
/* 2510 */     return dateTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Date string2Date(String dateString) throws Exception {
/* 2520 */     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
/* 2521 */     dateFormat.setLenient(false);
/* 2522 */     Date timeDate = dateFormat.parse(dateString);
/* 2523 */     Date dateTime = new Date(timeDate.getTime());
/* 2524 */     return dateTime;
/*      */   }
/*      */   
/*      */   private static String getPersonIdByNumber(Context ctx, String number) {
/* 2528 */     String pid = "";
/* 2529 */     if (number != null && !"".equals(number)) {
/* 2530 */       String sql = "select FID from t_bd_person where fnumber ='" + number + "'";
/*      */       try {
/* 2532 */         IRowSet rs = DBUtil.executeQuery(ctx, sql);
/* 2533 */         if (rs != null && rs.size() > 0) {
/* 2534 */           while (rs.next()) {
/* 2535 */             if (rs.getObject("FID") != null && !"".equals(rs.getObject("FID").toString())) {
/* 2536 */               pid = rs.getObject("FID").toString();
/*      */             }
/*      */           } 
/*      */         }
/* 2540 */       } catch (BOSException e) {
/* 2541 */         e.printStackTrace();
/* 2542 */       } catch (SQLException e) {
/* 2543 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/* 2546 */     return pid;
/*      */   }
/*      */   
/*      */   private static String materialUnitId(Context ctx, String orgId, String materialId) {
/* 2550 */     String unitId = "";
/* 2551 */     if (orgId != null && !"".equals(orgId) && materialId != null && !"".equals(materialId)) {
/* 2552 */       String sql = "select FUnitID from T_BD_MaterialPurchasing where FMaterialID ='" + materialId + "' and FOrgUnit ='" + orgId + "'";
/*      */       try {
/* 2554 */         IRowSet rs = DBUtil.executeQuery(ctx, sql);
/* 2555 */         if (rs != null && rs.size() > 0) {
/* 2556 */           while (rs.next()) {
/* 2557 */             if (rs.getObject("FUnitID") != null && !"".equals(rs.getObject("FUnitID").toString())) {
/* 2558 */               unitId = rs.getObject("FUnitID").toString();
/*      */             }
/*      */           } 
/*      */         }
/* 2562 */       } catch (BOSException e) {
/* 2563 */         e.printStackTrace();
/* 2564 */       } catch (SQLException e) {
/* 2565 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/* 2568 */     return unitId;
/*      */   }
/*      */   
/*      */   private static BigDecimal getmaterialMultiple(Context ctx, String materialId, String unitId) {
/* 2572 */     BigDecimal multiple = new BigDecimal(1);
/* 2573 */     if (materialId != null && !"".equals(materialId) && unitId != null && !"".equals(unitId)) {
/*      */       
/* 2575 */       boolean flag = false;
/* 2576 */       String sql = "select FBaseUnit from T_BD_Material where FID = '" + materialId + "'";
/*      */       try {
/* 2578 */         IRowSet rs = DBUtil.executeQuery(ctx, sql);
/* 2579 */         if (rs != null && rs.size() > 0) {
/* 2580 */           while (rs.next()) {
/* 2581 */             if (rs.getObject("FBaseUnit") != null && !"".equals(rs.getObject("FBaseUnit").toString()) && 
/* 2582 */               !unitId.equals(rs.getObject("FBaseUnit").toString())) flag = true;
/*      */           
/*      */           } 
/*      */         }
/* 2586 */       } catch (BOSException e) {
/* 2587 */         e.printStackTrace();
/* 2588 */       } catch (SQLException e) {
/* 2589 */         e.printStackTrace();
/*      */       } 
/*      */       
/* 2592 */       if (flag) {
/* 2593 */         sql = "select FBaseConvsRate from t_bd_multimeasureunit where fmaterialid ='" + materialId + "'and FMeasureUnitID='" + unitId + "'";
/*      */         try {
/* 2595 */           IRowSet rs = DBUtil.executeQuery(ctx, sql);
/* 2596 */           if (rs != null && rs.size() > 0) {
/* 2597 */             while (rs.next()) {
/* 2598 */               if (rs.getObject("FBaseConvsRate") != null && !"".equals(rs.getObject("FBaseConvsRate").toString())) {
/* 2599 */                 multiple = new BigDecimal(rs.getObject("FBaseConvsRate").toString());
/*      */               }
/*      */             } 
/*      */           }
/* 2603 */         } catch (BOSException e) {
/* 2604 */           e.printStackTrace();
/* 2605 */         } catch (SQLException e) {
/* 2606 */           e.printStackTrace();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2611 */     return multiple;
/*      */   }
/*      */ }
