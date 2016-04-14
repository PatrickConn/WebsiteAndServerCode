<?php

DEFINE ('DBUSER', 'PatrickPCSC');
DEFINE ('DBPW', 'p30401');
DEFINE ('DBHOST', '127.0.0.1');
DEFINE ('DBNAME', 'PatrickPCSC$temp');

$con= mysqli_connect("127.0.0.1","PatrickPCSC","p30401");
if (!$con){
die("Can not connect: " . mysql_error());
}
mysqli_select_db($con,DBNAME);
$response = array();
if (isset($_GET["User"])) {
    $Name = "test";


    // get a product from products table
	$query = "SELECT * FROM user WHERE Name = '$Name'";
    $result = mysqli_query($con,$query);

    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {

            $result = mysqli_fetch_array($result);
            $product = array();
            $product["Login"] = 1;
            $response["success"] = 1;
            $response["coaches"] = array();
            array_push($response["coaches"], $product);
            echo json_encode($response);

        } else {
            $product = array();
            $product["Login"] = 0;
			$response["success"] = 0;
			$response["coaches"] = array();
			array_push($response["coaches"], $product);
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product found";

        // echo no users JSON
        echo json_encode($response);
    }
}else {
        // no player found
        $response["success"] = 0;
        $response["message"] = "Required field(s) is missing";

        // echo no users JSON
        echo json_encode($response);
    }
function Login()
{
    if(empty($_POST['username']))
    {
        $this->HandleError("UserName is empty!");
        return false;
    }

    if(empty($_POST['password']))
    {
        $this->HandleError("Password is empty!");
        return false;
    }

    $username = trim($_POST['username']);
    $password = trim($_POST['password']);

    if(!$this->CheckLoginInDB($username,$password))
    {
        return false;
    }

    session_start();

    $_SESSION[$this->GetLoginSessionVar()] = $username;

    return true;
}

function CheckLoginInDB($username,$password)
{
    if(!$this->DBLogin())
    {
        $this->HandleError("Database login failed!");
        return false;
    }
    $username = $this->SanitizeForSQL($username);
    $pwdmd5 = md5($password);
    $qry = "Select Name, Pass from $this->coaches ".
        " where Name='$username' and Pass='$pwdmd5' ".
        " and confirmcode='y'";

    $result = mysqli_query($qry,$this->connection);

    if(!$result || mysqli_num_rows($result) <= 0)
    {
        $this->HandleError("Error logging in. ".
            "The username or password does not match");
        return false;
    }
    return true;
}

function CheckLogin()
{
     session_start();

     $sessionvar = $this->GetLoginSessionVar();

     if(empty($_SESSION[$sessionvar]))
     {
        return false;
     }
     return true;
}
?>