# Patrick Connauhotn 
from flask import Flask, render_template, request, jsonify, session
import DBcm
from threading import Thread
import hashlib , uuid
from functools import wraps
import base64
import smtplib
import base64
from passlib.hash import sha256_crypt
DBconfig = { 'host': '127.0.0.1',
                 'user': 'pat',
                 'password': 'p30401',
                 'database': 'UserDB' }

				 
app = Flask(__name__)	

def check_login(func):
	@wraps(func)
	def wrapped_function(*args, **kwargs):
		if 'logged_in' in session:
			return func(*args, **kwargs)
		return 'You are NOT logged in. Please login to continue.'
	return wrapped_function

def sendEmail(email):
	Sender = "c00167985@gmail.com"
	Sender_password = "Patpassword"
	To = email
	Subject = "Vrify you're accont"
	Text = """This link will verify you're accont \n http://127.0.0.1:5000/show/page/3"""

	#message = """\From: %s\nTo: %s\nSubject: %s\nText: %s
	#""" % (Sender, ",".join(To), Subject, Text)
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
	
def check_Admin(func):
	@wraps(func)
	def wrapped_function(*args, **kwargs):
		if 'Admin' in session:
			return func(*args, **kwargs)
		return 'You are NOT a Admin. Please.'
	return wrapped_function

@app.route('/show/page/<num>')
def display_page(num:int) -> 'html':
    tplt = 'page' + str(num) + '.html'
    return render_template(tplt,
                           the_title='This is my Assignment pages')
						  
						  
						  

@app.route('/page2')
@check_login
@check_Admin
def page2_route():
	return 'This page checks  the login and if the user is an Admin'

@app.route('/page3')
@check_login
def page3_route():
	return 'This page checks the login'




def Add_User(User:str, Password:str, Email:str, Admin:bool):				 
	with DBcm.UseDatabase(DBconfig) as cursor:
		_SQL = "insert into UserTable (User, Password, Email, Admin) values (%s, %s, %s ,%s)"
		cursor.execute(_SQL, (User, Password, Email, Admin,))
		

    
@app.route('/data', methods=['POST'])
def Add_New_User() -> 'html':
	User1 = request.form['User']
	Password1 = request.form['Password']
	Email = request.form['Email']	
	Admin2 = request.form['Admin']
	Admin = False
	if Admin2 == 'Admin':
		Admin = True	
	with DBcm.UseDatabase(DBconfig) as cursor:
		_SQL = "SELECT User FROM UserTable WHERE User=%s"
		cursor.execute(_SQL, (User1,))
		data = cursor.fetchall ()	
		if not data:	
			Password1 = sha256_crypt.encrypt(Password1)
			sendEmail(Email)
			Add_User(User1, Password1, Email , Admin)
			return render_template('results.html',
							the_title = 'You have ',
							User=User1,
							Email=Email,)
		else:
			return 'Sorry that user name has been teken'
	


@app.route('/login', methods=['POST'])
def Login_User() -> 'html':
    User = request.form['User']
    Password1 = request.form['Password']
    with DBcm.UseDatabase(DBconfig) as cursor:
        _SQL = "SELECT confrom FROM usertable WHERE User=%s"
        cursor.execute(_SQL, (User,))
        data = cursor.fetchall()
        test = ','.join(str(v) for v in data[0])
        if test != "1":
            return 'Your accont in not validation please validation your accont'
        else:
            with DBcm.UseDatabase(DBconfig) as cursor:
                _SQL = "SELECT Password FROM usertable WHERE User=%s"
                cursor.execute(_SQL, (User,))
                data = cursor.fetchall()
                if not data:
                    return 'Sorry you login faled please go back and try again 1'
                else:
                    test = ','.join(str(v) for v in data[0])
                    if sha256_crypt.verify(Password1 , test):
                        with DBcm.UseDatabase(DBconfig) as cursor:
                            _SQL = "SELECT Admin FROM usertable WHERE User=%s"
                            cursor.execute(_SQL, (User,))
                            data = cursor.fetchall()
                            test = ','.join(str(v) for v in data[0])
                            session['logged_in'] = True
                            if test == "1":
                                session['Admin'] = True
                            return 'You are login'
                    else:
                        return 'Sorry you login faled please go back and try again'
	
@app.route('/Validation', methods=['POST'])
def Validation_User() -> 'html':
	User = request.form['User']
	Password = request.form['Password']		
	confrom = True			
	with DBcm.UseDatabase(DBconfig) as cursor:
		_SQL = "UPDATE UserTable SET confrom=%s WHERE User=%s"
		cursor.execute(_SQL, (confrom, User))
	return 'Your account has been validation'
			
@app.route('/logout')
def logout():
    session.clear()
	#session.pop('Admin')
    return 'You are now logged out. Please come again soon.'
	
@app.route('/home')
def home():
    return render_template('home.html',
							the_title = 'This is the home page',)


	

@app.route('/status')
def show_status():
    if 'logged_in' in session:
        return 'You are currently logged in.'
    return 'You are NOT logged in. Please login to continue.'

app.secret_key = 'password'




if __name__ == "__main__":
    app.run(debug=True)
