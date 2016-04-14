# Patrick Connauhotn
# C00167985
from flask import Flask,flash,redirect,url_for, render_template, request, jsonify, session,request
import DBcm
from threading import Thread
import hashlib , uuid
from functools import wraps
import base64
import smtplib
import base64
from passlib.hash import sha256_crypt
import paypalrestsdk
import logging
import random
import string
import sys
import mysql.connector
from mysql.connector.constants import ClientFlag
from requests.auth import HTTPBasicAuth
import json
import ctypes
import flask_login
import requests
from werkzeug.datastructures import ImmutableOrderedMultiDict
import sqlparse
import requests
from requests.auth import HTTPBasicAuth
import json
from OpenSSL import SSL
from Crypto.Cipher import DES, AES
from datetime import datetime, timedelta
import sys
import re
import base64
from Crypto import Random
from simplecrypt import encrypt, decrypt
#this is the databaes contachen
DBconfig = { 'host': '127.0.0.1',
                 'user': 'PatrickPCSC',
                 'password': 'p30401',
                 'database': 'PatrickPCSC$PCSC' }


app = Flask(__name__)

#this will check if the user is login are not.
def check_login(func):
	@wraps(func)
	def wrapped_function(*args, **kwargs):
		if 'logged_in' in session:
			return func(*args, **kwargs)
		return render_template('error.html' , error='You are NOT logged in. Please login to continue.',)
	return wrapped_function

#this will generation a rendom string with number anf ltters for the email confrom
#http://stackoverflow.com/questions/2257441/random-string-generation-with-upper-case-letters-and-digits-in-python
def id_generator(size=10, chars=string.ascii_uppercase + string.digits):
	return ''.join(random.choice(chars) for _ in range(size))

#this will send a email to the user so the can verify there account.
def sendEmail(email,confirmationCode):
	Sender = "c00167985@gmail.com"
	Sender_password = "Patpassword"
	To =  str(email)
	Subject = "Vrify you're accont"
	Text = """This link will verify you're accont \n http://http://patrickpcsc.pythonanywhere.com/show/page/3 \n
	Enter you'er user name and this key thank you.\n""" + confirmationCode
    #this is how to massage is constuced
	message = """\From: %s\nTo: %s\nSubject: %s\nText: %s
	""" % (Sender, ",".join(To), Subject, Text)
	Body = '\r\n'.join(['To: %s' % To,
                'From: %s' % Sender,
                'Subject: %s' % Subject,
                '', Text])

	smtpserver = smtplib.SMTP("smtp.gmail.com",587)
	smtpserver.ehlo()
	smtpserver.starttls()
	smtpserver.ehlo()
	smtpserver.login(Sender , Sender_password)
	smtpserver.sendmail(Sender, To, Body)
	smtpserver.close()

#this will delpay any page that of the /show/page1 in the link
@app.route('/show/page/<num>')
def display_page(num:int) -> 'html':
    tplt = 'page' + str(num) + '.html'
    return render_template(tplt)

#this will add the the user the table users_table in my database
def Add_User(username:str, password:str, Email:str,First_Name:str,Last_Name:str):
    #this is where the user info is encrypted be for and it to the database
    cipher = AESCipher(key='mykey')
    username = cipher.encryptstrin(username,iv)
    First_Name = cipher.encryptstrin(First_Name,iv)
    Last_Name = cipher.encryptstrin(Last_Name,iv)
    # this is how the user info is add to the database
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "insert into Users_Table (User_First_Name,User_Last_Name,User_Name, User_Password, User_Email ,User_Balance) values (%s, %s, %s,%s,%s,%s)"
        cursor.execute(_SQL, (First_Name,Last_Name,username,password , Email, 0,))

#this will send the confirmation code the table ConfirmationCode_Table so wiht the use trys to confirm there email i have a code to
#to conpaer it to the user id and usernaem are also stord.
def Code(username:str, id:int, code:str):
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "insert into ConfirmationCode_Table (User_Name,User_Id,Code) values (%s, %s, %s)"
        cursor.execute(_SQL, (username,id ,code,))

#this is where i add my user to the database
@app.route('/data', methods=['POST'])
def Add_New_User() -> 'html':
    error = None
    #get the user info form the forum
    User1 = request.form['User']
    Password1 = request.form['Password']
    Email = request.form['Email']
    Fname = request.form['Fname']
    Lname = request.form['Lname']
    cipher = AESCipher(key='mykey')
    User = cipher.encryptstrin(User1,iv)
    #all of the user info is got form the forum.
	#the i check to see if the username is alrady taken if so return an error
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Name FROM Users_Table WHERE User_Name=%s"
        cursor.execute(_SQL, (User,))
        data = cursor.fetchall ()
    if not data:
        #this will encrypt the password
        Password1 = sha256_crypt.encrypt(Password1)
        confirmationCode = id_generator()
        #call the sendemail
        sendEmail(Email,confirmationCode)
        # then add the user to the database
        Add_User(User1, Password1, Email,Fname,Lname)
        # set the loging session to true
        session['logged_in'] = True
        #gets the user id
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Id FROM Users_Table WHERE User_Name=%s"
            cursor.execute(_SQL, (User,))
            data = cursor.fetchall()
        id = ','.join(str(v) for v in data[0])
        cipher = AESCipher(key='mykey')
        id = cipher.encryptstrin(id,iv)
        # sevse the user id
        session['User_ID'] = id
        session['UserName'] = User
        # the add the user ditasl and the user confrom code the database
        Code(User,id,confirmationCode)
        return render_template('results.html',
                        the_title = 'Thank you for signing up you can go and play',
                        User=User1,
                        Email=Email,)
    else:
        error = 'Invalid credentials'
        return render_template('signUp.html' , error=error,)

# this is where the user login in it checks if the user a vifid there acconcct frist.
@app.route('/login', methods=['POST'])
def Login_User() -> 'html':
    User = request.form['User']
    Password1 = request.form['Password']
    cipher = AESCipher(key='mykey')
    User = cipher.encryptstrin(User,iv)
    # this is to check if the user can conform there email
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Confirmed FROM Users_Table WHERE User_Name=%s"
        cursor.execute(_SQL, (User,))
        data = cursor.fetchall()
    error = ""
    # if is no data then the user eder enter the wrong user or they a not on the database
    if not data:
        error = 'Your username or password was incorrect'
        return render_template('page1.html' , error=error,)
    #this get the User_Confirmed from the data
    test = ','.join(str(v) for v in data[0])

    #if the User_Confirmed is not equal to 1 then they have not valadate the account an they have to
    if test != "1":
        error = 'Your accont in not validatait please validatia your accont'
        return render_template('page1.html' , error=error,)
    else:
        #get the user password of the database
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Password FROM Users_Table WHERE User_Name=%s"
            cursor.execute(_SQL, (User,))
            data = cursor.fetchall()
    if not data:
        error = 'Sorry you login faled please go back and try again'
        return render_template('page1.html' , error=error,)
    else:
        # get the user password form the data
        test = ','.join(str(v) for v in data[0])
        if sha256_crypt.verify(Password1 , test):#this will conpaer the password the user enters and the password that is on the database
            #sets the login ession to true so the user can move arand the website
            session['logged_in'] = True
            with DBcm.UseDatabase(DBconfig) as cursor:
                _SQL = "SELECT User_Id FROM Users_Table WHERE User_Name=%s"
                cursor.execute(_SQL, (User,))
                data = cursor.fetchall()
            id = ','.join(str(v) for v in data[0])
            with DBcm.UseDatabase(DBconfig) as cursor:
                _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Name=%s"
                cursor.execute(_SQL, (User,))
                data = cursor.fetchall()
            Balance = ','.join(str(v) for v in data[0])
            cipher = AESCipher(key='mykey')
            id = cipher.encryptstrin(id,iv)
            session['User_ID'] = id #then set the user id and username so they can be user in odder functions
            session['UserName'] = User
            #this retuen tot user to the home page with there blaance os it can be diplayd on the screen
            return render_template('home.html',login="1",User_Balance = Balance)
        else:
            error = 'Sorry you login faled please go back and try again'
            return render_template('page1.html' , error=error, )


# this is the login in for the andriod app
@app.route('/login/andriod', methods=['GET','POST'])
def Log_Andriod():
    test = ""
    error = ""
    username = request.args.get('User')
    password = request.args.get('Pass')
    username = parseToken(username) # this is where the user info is decryption so i can verfie it
    password = parseToken(password)
    cipher = AESCipher(key='mykey')
    username = cipher.encryptstrin(username,iv)#this is where the iser info is encrypred to cheack the database
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Confirmed FROM Users_Table WHERE User_Name=%s"
        cursor.execute(_SQL, (username,))
        data = cursor.fetchall()
    if not data:
        error = 'Your username or password was incorrect'
        return str({"success":1,"login":[{"Login":"2"},{"Error":error}]})#this will return an error in the jons fromat so the app can get.
    test = ','.join(str(v) for v in data[0])

    if test != "1":
        error = 'Your accont in not validatait please validatia your accont'
        return str({"success":1,"login":[{"Login":"2"},{"Error":error}]})
    else:
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Password FROM Users_Table WHERE User_Name=%s"
            cursor.execute(_SQL, (username,))
            data = cursor.fetchall()
    if not data:
        error = 'Sorry you login faled please go back and try again'
        return str({"success":1,"login":[{"Login":"2"},{"Error":error}]})
    else:
        test = ','.join(str(v) for v in data[0])
        if sha256_crypt.verify(password , test):
            with DBcm.UseDatabase(DBconfig) as cursor:
                _SQL = "SELECT User_Id FROM Users_Table WHERE User_Name=%s"
                cursor.execute(_SQL, (username,))
                data = cursor.fetchall()
            id = ','.join(str(v) for v in data[0])
            cipher = AESCipher(key='mykey')
            encrypted = cipher.encryptstrin(id,iv)
            return str({"success":1,"login":[{"Login":"1"},{"User_Id":encrypted}]})
        else:
            error = 'Sorry you login faled please go back and try again'
            return str({"success":1,"login":[{"Login":"2"},{"Error":error}]})



# this is a Sign_up of the android side i do not return the user id so i can be shere the vifre there email
@app.route('/android/Sign_up', methods=['GET','POST'])
def Sign_up():
    username = request.args.get("User")
    password = request.args.get("Pass")
    Email = request.args.get("Email")
    First_Name = request.args.get("firstName")
    Last_Name = request.args.get("lastName")
    user = parseToken(username)
    password = parseToken(password)
    #Email = parseToken(Email)
    First_Name = parseToken(First_Name)
    Last_Name = parseToken(Last_Name)
    cipher = AESCipher(key='mykey')
    User1 = cipher.encryptstrin(user,iv)
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Name FROM Users_Table WHERE User_Name=%s"
        cursor.execute(_SQL, (User1,))
        data = cursor.fetchall ()
    if not data:
        Password1 = sha256_crypt.encrypt(password)
        confirmationCode = id_generator()
        #call the sendemail
        sendEmail(Email,confirmationCode)
        Code(User1,0,confirmationCode)
        Add_User(user, Password1, Email,First_Name,Last_Name)
        return str({"success":1,"Add":[{"Login":"1"},]})
    else:
        error = 'Invalid credentials'
        return str({"success":1,"Add":[{"Login":"2"},{"Error":error}]})

# this is where the android app checks the user balanese
@app.route('/android/CheckMyBalanece', methods=['GET','POST'])
def CheckMyBalanece():
    username = request.args.get("User")
    id = request.args.get("User_Id")
    username = parseToken(username)
    cipher = AESCipher(key='mykey')
    user = cipher.encryptstrin(username,iv)
    new_cipher = AESCipher(key='mykey')
    id = new_cipher.decrypstrin(id,iv)
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s AND User_Name=%s"
        cursor.execute(_SQL, (id,user))
        data = cursor.fetchall ()
    if data:
        Balance = ','.join(str(v) for v in data[0])
        return str({"success":1,"Add":[{"Login":"1"},{"Balance":Balance}]})
    else:
        error = 'Invalid credentials'
        return str({"success":1,"Add":[{"Login":"2"},{"Error":error}]})
#this is whetre the app get the max balanesce for the payout on the pheone
@app.route('/android/CheckMyBalanece/payout', methods=['GET','POST'])
def CheckMyBalaneceA():
    username = request.args.get("User")
    id = request.args.get("User_Id")
    username = parseToken(username)
    cipher = AESCipher(key='mykey')
    user = cipher.encryptstrin(username,iv)
    new_cipher = AESCipher(key='mykey')
    id = new_cipher.decrypstrin(id,iv)
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s AND User_Name=%s"
        cursor.execute(_SQL, (id,user))
        data = cursor.fetchall ()
    if data:
        Balance = ','.join(str(v) for v in data[0])
        return str({"success":1,"Add":[{"Login":"1"},{"Balance":Balance}]})
    else:
        error = 'Invalid credentials'
        return str({"success":1,"Add":[{"Login":"2"},{"Error":error}]})



# thi is how the user valdes there accont
@app.route('/Validation', methods=['POST'])
def Validation_User() -> 'html':
    User = request.form['User']
    Password = request.form['Password']
    Key = request.form['Key']
    cipher = AESCipher(key='mykey')
    User = cipher.encryptstrin(User,iv)
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT Code FROM ConfirmationCode_Table WHERE User_Name=%s"
        cursor.execute(_SQL, (User,))
        data = cursor.fetchall()
    if data:
        code = ','.join(str(v) for v in data[0])
        if code == Key:
            with DBcm.UseDatabase(DBconfig) as cursor:
                _SQL = "UPDATE Users_Table SET User_Confirmed=%s WHERE User_Name=%s"
                cursor.execute(_SQL, (1, User))
            error = 'Your account has been validation'
            return render_template('page2.html' , good=error,)
        else:
            error = 'The Key was wrong'
            return render_template('page3.html' , error=error,)
    else:
        error = 'Invalid credentials'
        return render_template('page3.html' , error=error,)

# logout the user
@app.route('/logout')
def logout():
	session.clear() #clears all the sessions off
	return render_template('home.html',login="0",)

# this is the home page if the login session is true the we get the user balance so it can be displad
@app.route('/home')
def home():
    session['add'] = 0.000
    if 'logged_in' in session:
        new_cipher = AESCipher(key='mykey')
        id = session['User_ID']
        id = new_cipher.decrypstrin(id,iv)

        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s "
            cursor.execute(_SQL, (id ,))
            data = cursor.fetchall()
        Balance = ','.join(str(v) for v in data[0])
        return render_template('home.html',login="1",User_Balance = Balance)
    else:
	    return render_template('home.html',login="0")

#this use in the encrypetn it
iv = b'Z\x12\x1f\x8d\x9dL\xea\xbb\xe4\xb6B\xce-D\xbb\x9f'
#this will jset show the home
@app.route('/')
def ssl():
    session['add'] = 0.000
    if 'logged_in' in session:
        new_cipher = AESCipher(key='mykey')
        id = new_cipher.decrypstrin(session['User_ID'],iv)
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s"
            cursor.execute(_SQL, (id ,))
            data = cursor.fetchall()
        Balance = ','.join(str(v) for v in data[0])
        return render_template('home.html',login="1",User_Balance = Balance)
    else:
	    return render_template('home.html',login="0")
#this will show the add moeny page i think
@app.route('/PayPal')
@check_login
def PayPal():
    return render_template('PayPal.html',the_title='Add You Crad Details below',)

# @app.route('/addBack', methods=[ 'POST','GET'])
# @check_login
# def addBack():
#     session['add'] = 0.000
#this will show the page the user see when to buy a new card
@app.route('/PlayCards')
@check_login
def PlayCards():
	return render_template('PlayCards.html')


# this is where there amont of moeny the user whats to add seavd
@app.route('/Addmoney', methods=[ 'POST'])
@check_login
def DepositMoney():
    AddMoney = request.form['a3']
    if 'logged_in' in session:
        with DBcm.UseDatabase(DBconfig) as cursor:
            session['add'] = float(request.form['a3'])
        return render_template('PayPalConform.html',amont=session['add'],)
    else:
        return render_template('home.html',error='sorry it falid',)


app.secret_key = 'password'

# this is where there  user balances is updata after the payment is a good
@app.route('/PayPalPrchase',methods=['POST','GET'])
@check_login
def purchase():
    new_cipher = AESCipher(key='mykey')
    id = session['User_ID']
    id = new_cipher.decrypstrin(id,iv)
    User = session['UserName']
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "UPDATE Users_Table SET User_Balance=User_Balance+%s WHERE User_Id=%s AND User_Name=%s"
        cursor.execute(_SQL, (float(session['add']),id,User))
        session['add'] = 0.000
    return redirect(url_for(".home"))

# this is a message service that automatically notifies merchants of events related to PayPal transactions.
@app.route('/ipn/',methods=['POST'])
def ipn():
	try:
		arg = ''
		request.parameter_storage_class = ImmutableOrderedMultiDict
		values = request.form
		for x, y in values.iteritems():
			arg += "&{x}={y}".format(x=x,y=y)
        #this ask paypal did they send use this data
		validate_url = 'https://www.sandbox.paypal.com' \
					   '/cgi-bin/webscr?cmd=_notify-validate{arg}' \
					   .format(arg=arg)
		r = requests.get(validate_url)
		if r.text == 'VERIFIED':#this paypal response to us asking if paypal send us that
			try:
			    # all the data form the payment
				payer_email =  thwart(request.form.get('payer_email'))
				unix = int(time.time())
				payment_date = thwart(request.form.get('payment_date'))
				username = thwart(request.form.get('custom'))
				last_name = thwart(request.form.get('last_name'))
				payment_gross = thwart(request.form.get('payment_gross'))
				payment_fee = thwart(request.form.get('payment_fee'))
				payment_net = float(payment_gross) - float(payment_fee)
				payment_status = thwart(request.form.get('payment_status'))
				txn_id = thwart(request.form.get('txn_id'))
			except Exception as e:
				with open('/tmp/ipnout.txt','a') as f:
					data = 'ERROR WITH IPN DATA\n'+str(values)+'\n'
					f.write(data)

			with open('/tmp/ipnout.txt','a') as f:
				data = 'SUCCESS\n'+str(values)+'\n'
				f.write(data)

			c,conn = connection()
			c.execute("INSERT INTO ipn (unix, payment_date, username, last_name, payment_gross, payment_fee, payment_net, payment_status, txn_id) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)",
						(unix, payment_date, username, last_name, payment_gross, payment_fee, payment_net, payment_status, txn_id))
			conn.commit()
			c.close()
			conn.close()
			gc.collect()

		else:
			with open('/tmp/ipnout.txt','a') as f:
				data = 'FAILURE\n'+str(values)+'\n'
				f.write(data)

		return r.text
	except Exception as e:
		return str(e)


#http://stackoverflow.com/questions/15899873/generate-random-numbers-with-known-discrete-probabilities-in-python
def weighted_random(weights):
    number = random.random() * sum(weights.values())
    for k,v in weights.items():
        if number < v:
           # print ("break" , v)
            break
        number -= v
        #print (number)
    return k

def xrange(x):
    return iter(range(x))


# the following values can be any non-negative numbers, no need of sum=100
weights = {'0': 125.0,
           '10': 15.0,
           '25': 4.0,
           '50': 3.0,
           '100': 1.0,
           '200': 0.5}



#http://stackoverflow.com/questions/2257441/random-string-generation-with-upper-case-letters-and-digits-in-python
def id_generator(size=10, chars=string.ascii_uppercase + string.digits):
	return ''.join(random.choice(chars) for _ in range(size))

# this is where the user buy there cards
@app.route('/GetCards', methods=['POST','GET'])
@check_login
def GetCards() -> 'html':
    Value = weighted_random(weights)# fiest get ther value of the card
    Balance = ""
    error = "did not wock"
    #this checks if there has enough money to buy a card
    new_cipher = AESCipher(key='mykey')
    id = session['User_ID']
    id = new_cipher.decrypstrin(id,iv)
    User = session['UserName']
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s AND User_Name=%s"
        cursor.execute(_SQL,(id,User))
        data = cursor.fetchall()
    OLDBalance = ','.join(str(v) for v in data[0])
    if float(OLDBalance) < 2.000:#checks to see if the user has enough money
        error="Sorry you need to add money to your accont to buy a new card"
        return render_template('error.html',error=error,)
    # will only get here if the user has enough money
    #then is will subtraket the cost of the card from the user balance
    NewBalance = float(OLDBalance) - 2
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "UPDATE Users_Table SET User_Balance=%s WHERE User_Id=%s AND User_Name=%s"
        cursor.execute(_SQL, (NewBalance,id,User))
	#this find out if there card is a Win or a lose
    if Value == '0':
        winOrLose = "Lose"
    else:
        winOrLose = "WIN"
    if Value == '0':
	    pass
    else:
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s AND User_Name=%s"
            cursor.execute(_SQL,(id,User))
            data = cursor.fetchall()
        OLDBalance = ','.join(str(v) for v in data[0])
        NewBalance = float(OLDBalance) + float(Value)
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "UPDATE Users_Table SET User_Balance=%s WHERE User_Id=%s AND User_Name=%s"
            cursor.execute(_SQL, (NewBalance,id,User))
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "insert into BackUp_Table (User_id, New_Balanec, Old_Balanec ,Card_Name,Card_value)values (%s, %s, %s,%s,%s)"
            cursor.execute(_SQL, (id, NewBalance, OLDBalance, winOrLose,Value))
    return render_template('PlayCards.html',error=winOrLose+Value,Value=Value)

#this find out if the user has enguht money to withdraw
@app.route('/PayOut')
@check_login
def max_pay_out():
    new_cipher = AESCipher(key='mykey')
    id = new_cipher.decrypstrin(session['User_ID'],iv)
    User = session['UserName']
    with DBcm.UseDatabase(DBconfig) as cursor:
	    _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s AND User_Name=%s"
	    cursor.execute(_SQL,(id,User))
	    data = cursor.fetchall()
    OLDBalance = ','.join(str(v) for v in data[0])
    if( float(OLDBalance) <= 0.000):
        return render_template('error.html',error="Sorry you do not have any money to withdraw")
    else:
	    return render_template('page4.html',max=OLDBalance)

# this is where ther users balance is updata after the with draw there money
@app.route('/PayPalSuccess', methods=['POST','GET'])
@check_login
def success():
    Email = request.form['User']
    Amount = request.form['amount']
    new_cipher = AESCipher(key='mykey')
    id = new_cipher.decrypstrin(session['User_ID'],iv)
    User =session['UserName']
    with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s AND User_Name=%s"
            cursor.execute(_SQL,(id,User))
            data = cursor.fetchall()
    OLDBalance = ','.join(str(v) for v in data[0])
    if float(Amount) > float(OLDBalance):
        return render_template('error.html',error="You try to withdraw more money than is in your account")
    Success = PayOut(Email,Amount)
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "UPDATE Users_Table SET User_Balance=User_Balance-%s WHERE User_Id=%s AND User_Name=%s"
        cursor.execute(_SQL, (Amount,id,User))
    return redirect(url_for(".home"))
    #return redirect(url_for(".logout"))

@app.route('/android/payout', methods=['POST','GET'])
def Apayout():
    Email = request.args.get("PayEmail")
    Amount = request.args.get("amount")
    User_Id = request.args.get("User_Id")
    UserName = request.args.get("User")
    new_cipher = AESCipher(key='mykey')
    id = new_cipher.decrypstrin(User_Id,iv)
    with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s"
            cursor.execute(_SQL,(id,))
            data = cursor.fetchall()
    OLDBalance = ','.join(str(v) for v in data[0])
    if float(Amount) > float(OLDBalance):
        return str({"success":1,"Add":[{"Login":"2"},{"Balance":"You trid to withdraw more money than is in your account"}]})
    Success = PayOut(Email,Amount)
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "UPDATE Users_Table SET User_Balance=User_Balance-%s WHERE User_Id=%s"
        cursor.execute(_SQL, (Amount,id,))
    #Balance = "Thank you for playing. Your account has been update"
    return str({"success":1,"Add":[{"Login":"1"},{"Balance":"Thank you. Please come again"}]})

#this is where the user can withdraw the money form ther account
#the money is taken from the app account.
def PayOut(user_email,total_amount):
	logging.basicConfig(level=logging.INFO)
	PAYPAL_MODE = "sandbox"
	PAYPAL_CLIENT_ID = "AXwzmtJ78RmGMzUAaQeYhD8gekNg4hDjxmccdfKYYu-OrS8I3lAI-i0JjA1G-lvaY9mjG2xXqD_d7pv1"
	PAYPAL_CLIENT_SECRET = "EKThiQ3ZKh8sl5rfLmMYTSTzBEpEjfjOq_L_yDPpf8GtJQBk97FE5Yi0WHCYUOUumFm60x5Hz8tFEWZn"
	url = "https://api.sandbox.paypal.com/v1/oauth2/token"
	headers = {'Accept': 'application/json','Accept-Language': 'en_US'}
	payload = {'grant_type': 'client_credentials'}
	token_response = requests.post(url, auth=HTTPBasicAuth(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET), headers=headers, data=payload)
	body = json.loads(token_response.text)
	authoize_token = body['token_type'] + " " + body['access_token']


	sender_batch_id = ''.join(random.choice(string.ascii_uppercase) for i in range(12))
	payouturl = "https://api.sandbox.paypal.com/v1/payments/payouts/"
	payoutheaders = {'Content-Type':'application/json','Authorization': ""+authoize_token}
	payoutpayload =  {
    "sender_batch_header": {
        "sender_batch_id": sender_batch_id,
        "email_subject": "You have a Payout!",
        "recipient_type": "EMAIL"
    },
    "items": [
        {
            "amount": {
                "value": total_amount,
                "currency": "USD"
            },
            "receiver": user_email
        }
    ]
  }

	paramvalue = {'sync_mode': 'true'}
	payout_response = requests.post(payouturl, headers=payoutheaders, data=json.dumps(payoutpayload), params=paramvalue)
	payout_body = json.loads(payout_response.text)
	status = payout_body['batch_header']
	return str(status['batch_status'])

#this is where the android get it cards
@app.route('/android/BuyACard', methods=['POST','GET'])
def AndroidGetCards():
    Value = weighted_random(weights)
    Balance = ""
    error = "did not wock"
    User_Id = request.args.get("User_id")
    UserName = request.args.get("UserName")
    UserName = parseToken(UserName)
    new_cipher = AESCipher(key='mykey')
    User_Id = new_cipher.decrypstrin(User_Id,iv)
    cipher = AESCipher(key='mykey')
    UserName = cipher.encryptstrin(UserName,iv)
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s"
        cursor.execute(_SQL,(User_Id,))
        data = cursor.fetchall()
    OLDBalance = ','.join(str(v) for v in data[0])
    if float(OLDBalance) < 2.000:
        error="Sorry you need to add money to your accont to buy a new card"
        return str({"success":1,"buy":[{"buyAcard":"0"},{"Error":error}]})
    OLDBalance = float(OLDBalance) - 2.000
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "UPDATE Users_Table SET User_Balance=%s WHERE User_Id=%s AND User_Name=%s"
        cursor.execute(_SQL, (OLDBalance,User_Id,UserName))
    if Value == '0':
        winOrLose = "Lose"
        NewBalance = float(OLDBalance) + float(Value)
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "UPDATE Users_Table SET User_Balance=%s WHERE User_Id=%s AND User_Name=%s"
            cursor.execute(_SQL, (NewBalance,User_Id,UserName))
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "insert into BackUp_Table (User_id, New_Balanec, Old_Balanec ,Card_Name,Card_value)values (%s, %s, %s,%s,%s)"
            cursor.execute(_SQL, (User_Id, NewBalance, OLDBalance, winOrLose,Value))
        return str({"success":1,"buy":[{"buyAcard":"1"},{"NewBalance":NewBalance},{"OLDBalance":OLDBalance},{"winOrLose":winOrLose},{"Value":Value}]})
    else:
        winOrLose = "WIN"
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s"
            cursor.execute(_SQL,(User_Id,))
            data = cursor.fetchall()
        OLDBalance = ','.join(str(v) for v in data[0])
        NewBalance = float(OLDBalance) + float(Value)
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "UPDATE Users_Table SET User_Balance=%s WHERE User_Id=%s AND User_Name=%s"
            cursor.execute(_SQL, (NewBalance,User_Id,UserName))
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "insert into BackUp_Table (User_id, New_Balanec, Old_Balanec ,Card_Name,Card_value)values (%s, %s, %s,%s,%s)"
            cursor.execute(_SQL, (User_Id, NewBalance, OLDBalance, winOrLose,Value))
        return str({"success":1,"buy":[{"buyAcard":"1"},{"NewBalance":NewBalance},{"OLDBalance":OLDBalance},{"winOrLose":winOrLose},{"Value":Value}]})

#this is there the user account is updata.
@app.route('/android/addmoney', methods=['POST','GET'])
def addmoney():
    username = request.args.get("User")
    id = request.args.get("userId")
    amont = request.args.get("Amont")
    username = parseToken(username)
    amont = parseToken(amont)
    new_cipher = AESCipher(key='mykey')
    id = new_cipher.decrypstrin(id,iv)
    cipher = AESCipher(key='mykey')
    username = cipher.encryptstrin(username,iv)
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s"
        cursor.execute(_SQL,(id,))
        data = cursor.fetchall()
    old = ','.join(str(v) for v in data[0])
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "UPDATE Users_Table SET User_Balance=User_Balance+%s WHERE User_Name=%s AND User_Id=%s"
        cursor.execute(_SQL, (amont,username,id,))
        data = cursor.rowcount
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT User_Balance FROM Users_Table WHERE User_Id=%s"
        cursor.execute(_SQL,(id,))
        data = cursor.fetchall()
    new = ','.join(str(v) for v in data[0])
    if float(new) > float(old):
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "insert into BackUp_Payment_Table (User_id, Users_Name, amont ,OLD_Balance,NEW_Balance)values (%s, %s, %s,%s,%s)"
            cursor.execute(_SQL, (id, username, amont, old,new))
        return str({"success":1,"Add":[{"Login":"1"}]})
    else:
        with DBcm.UseDatabase(DBconfig) as cursor:
            _SQL = "insert into BackUp_Payment_Table (User_id, Users_Name, amont ,OLD_Balance,NEW_Balance)values (%s, %s, %s,%s,%s)"
            cursor.execute(_SQL, (id, username, amont, old,new))
        error = 'Sorry'
        return str({"success":1,"Add":[{"Login":"2"},{"Error":error}]})

# this is where the user info is decryption that is sent form the adroid app
def parseToken(str):
    cipher = DES.new('password',DES.MODE_CFB, 'password')
    resolved=cipher.decrypt(base64.b64decode(str))
    return resolved.decode('utf-8')

class AESCipher(object):
    """
    A classical AES Cipher. Can use any size of data and any size of password thanks to padding.
    Also ensure the coherence and the type of the data with a unicode to byte converter.
    """
    def __init__(self, key):
        self.bs = 32
        self.key = hashlib.sha256(AESCipher.str_to_bytes(key)).digest()

    @staticmethod
    def str_to_bytes(data):
        u_type = type(b''.decode('utf8'))
        if isinstance(data, u_type):
            return data.encode('utf8')
        return data

    def _pad(self, s):
        return s + (self.bs - len(s) % self.bs) * AESCipher.str_to_bytes(chr(self.bs - len(s) % self.bs))

    @staticmethod
    def _unpad(s):
        return s[:-ord(s[len(s)-1:])]

    #iv = Random.new().read(AES.block_size)
    def encryptstrin(self, raw,iv):
        raw = self._pad(AESCipher.str_to_bytes(raw))
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        return base64.b64encode(iv + cipher.encrypt(raw)).decode('utf-8')

    def decrypstrin(self, enc,iv):
        enc = base64.b64decode(enc)
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        return self._unpad(cipher.decrypt(enc[AES.block_size:])).decode('utf-8')

    def decrypandroid(self, enc):
        ##enc = base64.b64decode(enc)
        cipher = DES.new('password',DES.MODE_CFB, 'password')
        resolved=cipher.decrypt(base64.b64decode(enc))
        return resolved.decode('utf-8')



if __name__ == "__main__":
   # app.run(host='127.0.0.1',port='12344',
        #debug = False/True, ssl_context=context)
    app.run(debug = True)