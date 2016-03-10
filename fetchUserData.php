<?

        $con = mysqli_connect("mysql.hostinger.in","u521844001_hack","hackingbad","u521844001_sqldb") or die('Unable to connect');
        $username = $_POST['username'];
	$password = $_POST['password'];

	$sql = mysqli_prepare($con,"SELECT * FROM Users WHERE Username = ? AND Password = ?");
	mysqli_stmt_bind_param($sql,"ss", $username, $password);
	mysqli_stmt_execute($sql);

	mysqli_stmt_store_result($sql);
	mysqli_stmt_bind_result($sql, $userId, $username, $password, $name);

	$user = array();

	while(mysqli_stmt_fetch($sql)) {
		$user[name] = $name;
		$user[username] = $username;
		$user[password] = $password;

	}

	echo json_encode($user);

	mysqli_stmt_close($sql);
	
	mysqli_close($con);
?>



		