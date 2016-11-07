<%@page import="ly.snmp.core.model.SNMPVersion"%>
<%@page import="ly.snmp.core.monitor.Monitor"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Basic Form - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="../js/css/snmpui.css">
	<script type="text/javascript" src="../js/easyui/jquery.min.js"></script>
	<script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
</head>
<body>
	<div style="margin:20px 0;"></div>
	<div class="easyui-panel" title="Add Device" style="width:400px">
		<div style="padding:10px 60px 20px 60px">
	    <form id="ff" action="/SNMPUI/addDevice" method="post">
	    	<table cellpadding="5">
	    		<tr>
	    			<td>IP:</td>
	    			<td><input class="easyui-textbox" type="text" name="ip" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>Version:</td>
	    			<td>
	    				<select class="easyui-combobox" name="version">
	    				<% for(SNMPVersion version : SNMPVersion.values()){ %>
							<option value="<%= version.toString() %>"><%= version.toString() %></option>
						<%} %>
	    				</select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>Community:</td>
	    			<td><input class="easyui-textbox" type="text" name="community" data-options="required:true"></input></td>
	    		</tr>
	    		<tr>
	    			<td>Oids:</td>
	    			<td><input class="easyui-textbox" name="oids" data-options="multiline:true" style="height:60px"></input></td>
	    		</tr>
	    		<tr>
	    			<td>Monitors:</td>
	    			<td>
	    				<select class="easyui-combobox" data-options="multiple:true" name="monitors">
							<option value="cpu">CPU</option>
							<option value="disk">Disk</option>
							<option value="memory">Memory</option>
						</select>
	    			</td>
	    		</tr>
	    	</table>
	    </form>
	    <div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">Submit</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">Clear</a>
	    </div>
	    </div>
	</div>
	<script>
		function submitForm(){
			$('#ff').form('submit');
			window.location.href='/SNMPUI/jsp/devices.jsp';
		}
		function clearForm(){
			$('#ff').form('clear');
		}
	</script>
</body>
</html>