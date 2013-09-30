// *** retrieve LDAP data starting from a root/starting node
<?php

// Setup configuration here
$ldap_svr='ldaps://LDAP_SVR';
$ldap_svc_acct='CN=LDAP-USER,OU=SvcAccount,OU=UserAcct,';
$ldap_svc_acct_dn='DC=department,DC=domain,DC=com';
$ldap_svc_acct_passwd='LDAP-PASSWD';

$ldap_search_start_dn='(cn=LdapRootUser*)';
$ldap_search_base_dn='OU=UserAcct,DC=department,DC=domain,DC=com';
$ldap_search_person_cn='/CN=Person*/';
$ldap_search_direct_report_key='directreports';

$con=ldap_connect($ldap_svr);
ldap_set_option($con, LDAP_OPT_PROTOCOL_VERSION, 3);

var_dump($con);

$bind=ldap_bind($con,
	$ldap_svc_acct .
	$ldap_svc_acct_dn,
	$ldap_svc_acct_passwd);
	
if ($bind==FALSE) {
	var_dump($bind);
}
var_dump($con);

// this is the starting point of the query - from this point, it 
// recurses into the LDAP tree to retrieve all of the users
$db_conn=openDbConnection();
// CFG: set the CN to the root node of your LDAP database (e.g. the CEO)
getResult($db_conn, $con, $ldap_search_start_dn);
$db_conn=null;

ldap_close($con);
//fin

function getResult($db_conn, $con, $qry, $gid=-1, $mgr_id=null) {
	print ("Got [" . $qry . "]\n");
	$rslt=ldap_search($con, $ldap_search_base_dn,
		$qry);
	$vals=ldap_get_entries($con, $rslt);
	$idx=0;
	if ($vals['count'] > 1) {
		print 'Ambiguous query '.$qry.' returned '
			.$vals['count']."results\n";
		//print_r($vals);
		$rcrd_idx=disambiguateEntry($db_conn, $vals, $mgr_id);
		print "dis idx " . $rcrd_idx . "\n";
		if ($rcrd_idx!=-1) $idx=$rcrd_idx;
		else return;
	}
	else if ($vals['count'] <= 0) {
		print 'Lookup failed for '.$qry."\n";
		return;
	}

	$person=$vals[$idx]['objectcategory'][0];
	// only look for people in the organization
	$is_person=preg_match($ldap_search_person_cn, $person);
	if ($is_person==0) return;

	$reports=@$vals[$idx][$ldap_search_direct_report_key];
	if ($reports!=null) {
	foreach ($reports as $rpt) {
		$rpt_qry=makeNextQry($rpt);
		if ($rpt_qry==null) continue;
			getResult($db_conn, $con, $rpt_qry, $gid, $uid);
		}
	}
}

function makeNextQry($rpt) {
	$rpt_qry=null;
	$name=array();
	$matches=preg_match('/CN=(\w+)\\\,[ ]*(\w+)[ ]*[\(]*(\w*)[\)]*/',
		$rpt, $name);
	if ($matches > 0) {
		if ($name[3]!=null)
			$rpt_qry='(samaccountname='.$name[3].')';
		else
			$rpt_qry = '(cn='.$name[1].', '.$name[2].'*)';
	}
	else {
		print "no match for " . $rpt . "\n";
	}
	return $rpt_qry;
}

function disambiguateEntry($db_conn, $vals, $mgr_uid) {
	$idx=-1;

	$mgr_uname=getMgrUnameByUid($db_conn, $mgr_uid);
	if ($mgr_uname==null)
		return;

	$cnt=$vals['count'];
	for ($i=0; $i<$cnt; $i++) {
		$entry_mgr=$vals[$i]['manager'][0];
		print "mgr " . $entry_mgr . ' ' . $mgr_uid
			. " " . $mgr_uname. "\n";
		$mgr_marr=array();
		$mgr_match=preg_match('/'.$mgr_uname.'/',
			$vals[$i]['manager'][0], $mgr_marr);
		if ($mgr_match > 0) {
			$idx=$i;
			break;
		}
	}
	if ($idx==-1)
		print "unable to disambiguate\n";
	print "disambiguateEntry " . $idx . "\n";
	return $idx;
}
?>