<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lexical Analysis and Edit Distance</title>
	<style>
		fieldset 
		{
		    display: block;
		    border: bold;
		    width: 30%;
		}
	</style>
</head>
<body>
<h1></h1>
	<form name="myform" action="LexerDistanceServlet" method="post">
		<fieldset>
			<legend></legend>
			 Input 1: <br><textarea rows="5" cols="30" name="s2a"></textarea>
			 <br>
			 Input 2: <br><textarea rows="5" cols="30" name="s2b"></textarea>
			 <br><br>
			 <input type="submit" value="Submit!">
		</fieldset>
	</form>
</body>
</html>
