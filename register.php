<?
        $con = mysqli_connect("mysql.hostinger.in","u521844001_hack","hackingbad","u521844001_sqldb") or die('Unable to connect');
	
        $name = $_POST['name'];
	$username = $_POST['username'];
	$password = $_POST['password'];

	$sql = mysqli_prepare($con,"INSERT INTO Users(Username, Password, Name) VALUES (?,?,?)");
	mysqli_stmt_bind_param($sql,"sss", $username, $password, $name);
	mysqli_stmt_execute($sql);
	mysqli_stmt_close($sql);
	
	mysqli_close($con);
?>


			