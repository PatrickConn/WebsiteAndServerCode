# Patrick Connauhotn 
# testA2.py is how i test my code if  you need it. also at the end this code is the code i use to test this code 
from flask import Flask, render_template, request, jsonify, session
import DBcm
from threading import Thread
import hashlib , uuid
from functools import wraps
import base64
import smtplib
import base64
from passlib.hash import sha256_crypt
from collections import namedtuple
import requests
from collection_json import Collection
from collection_json import Template
import itertools
import json

DBconfig = { 'host': '127.0.0.1',
                 'user': 'gamesadmin',
                 'password': 'gamesadminpasswd',
                 'database': 'GamesDB' }

				 
app = Flask(__name__)	
Table = namedtuple("Table","Name")

@app.route('/table/list')
def table_list():
	with DBcm.UseDatabase(DBconfig) as cursor:
		_SQL = "show tables"
		cursor.execute(_SQL)
		data = [Table(*row) for row in cursor.fetchall()]
	test = ""
	test += """{"collection" :{"version" : "1.0","href" : "http://localhost:5000/table/list", "items" :[{ "href": "http://localhost:5000/table/list", "data": [ """ 
	for row in data: 
		test += """{"name":" """ + row.Name + """", "value": "http://localhost:5000/table/showall/""" + row.Name + """"},""" 
	test = test[:-1]
	test += """ ]}]}}"""
	return str(test)
	#this data conforms to the Collection+JSON media-type but to convert the string to json the clinet side will have use the code below 
	#collection = json.loads(test)
	#print(json.dumps(collection, sort_keys=True, indent=4))
	
@app.route('/table/showall/<test2>')
def table_showall(test2:str):
	with DBcm.UseDatabase(DBconfig) as cursor:
		_SQL = "SELECT * FROM " + test2 
		cursor.execute(_SQL,)
		data = cursor.fetchall()
		_SQL = "SHOW columns FROM " + test2  #this is used to get the columns name and value.
		cursor.execute(_SQL,)
		data2 = cursor.fetchall()
	temp = ','.join(str(v) for v in data2[0])
	frint = temp.find(',')
	test3 = temp[:frint]
	test = ""
	test += """{"collection":{"version": "1.0","href": "http://localhost:5000/table/showall/"""+test2+"""","""
	test += """ "items":[ """
	for row in data:
		index = -1
		test += """{ "href": "http://localhost:5000/table/showone/"""+test2+"""/"""+str(row[int("0")])
		test += """ ","data": [ """ 
		for row2 in data2:
			index += 1
			test += """{ "name": " """ + str(row2[0]) + """", "value": " """ + str(row[int(index)]) + """ " },""" 
		test = test[:-1]
		test += """]},""" 
	test = test[:-1]
	test += """ ],"""
	test +=   """  "template": {"data" : [ """
	for row2 in data2:
		if  str(row2[0]) != "id":
			test += """ { "name" : " """ + str(row2[0]) + """" , "value":" """+str(row2[1])+"""" },""" 
	test = test[:-1]
	test += """]}}}"""
	return str(test)
	#this data conforms to the Collection+JSON media-type but to convert the string to json the clinet side will have use the code below 
	#collection = json.loads(test)
	#print(json.dumps(collection, sort_keys=True, indent=4))
	

payload ="""{"template": {"data": [
        {"name": "name", "value": "test5"},
        {"name": "description", "value": "test4.0"}]}}"""

@app.route('/table/post/<table>', methods=['POST','GET'])
def table_post(table:str):
	if request.method == 'POST':
		data = Template.from_json(request.form['payload'])
		mods = data.to_dict()['template']['data']
		names = []
		values = []
		for x in mods:
			values.append(x['value'])
			names.append(x['name'])
		_SQL = """insert into """ + table + """ ("""
		for row in names:
			if  str(row[0]) != "id":
				_SQL += """""" + str(row) + ""","""
		_SQL = _SQL[:-1]
		_SQL += """) values ("""
		for row2 in names:
			_SQL +=  """%s,"""
		_SQL = _SQL[:-1]
		_SQL += ")"
		with DBcm.UseDatabase(DBconfig) as cursor:
			cursor.execute(_SQL,(values))
		_SQL3 = "SELECT id FROM "+table+" ORDER BY id DESC LIMIT 1"
		with DBcm.UseDatabase(DBconfig) as cursor:
			cursor.execute(_SQL3,)
			data5 = cursor.fetchall()
			temp = ','.join(str(v) for v in data5[0])
		test = ""
		test += """{"collection":{"version" : "1.0","href": "http://localhost:5000/table/post/"""+table+"""","""
		test += """ "links": [
			  { "href" : "http://localhost:5000/table/showall/""" + table+""""},
			  { "href" : "http://localhost:5000/table/showone/"""+table+"""/"""+temp+""""}]}}"""
		return str(test)
	else:
		with DBcm.UseDatabase(DBconfig) as cursor:
			_SQL = "SHOW columns FROM " + table  #this is used to get the columns name and value.
			cursor.execute(_SQL,)
			data2 = cursor.fetchall()
		test = ""
		test += """{"collection":{"version": "1.0","href": "http://localhost:5000/table/showall/"""+table+"""","""
		test +=   """  "template": {"data" : [ """
		for row2 in data2:
			if  str([0]) != "id":
				test += """ { "name" : " """ + str(row2[0]) + """" , "value":" """+str(row2[1])+"""" },""" 
		test = test[:-1]
		test += """]}}}"""
		return str(test)
		
	#this data conforms to the Collection+JSON media-type but to convert the string to json the clinet side will have use the code below 
	#collection = json.loads(test)
	#print(json.dumps(collection, sort_keys=True, indent=4))
	


@app.route('/table/showone/<test2>/<num>')
def table_showone(test2:str,num:int):
	with DBcm.UseDatabase(DBconfig) as cursor:
		_SQL = "SHOW columns FROM " + test2
		cursor.execute(_SQL,)
		data2 = cursor.fetchall()
		_SQL2 = "SELECT * FROM " + test2 + " WHERE id=%s "
		cursor.execute(_SQL2,(num,))
		data = cursor.fetchall()
	test = ""
	test += """{"collection":{"version" : "1.0","href": "http://localhost:5000/table/showone/"""+test2+"""/"""+num+"""","""
	test += """ "links": [
          { "href" : "http://localhost:5000/table/showall/""" + test2+""""},
          { "href" : "http://localhost:5000/table/showone/"""+test2+"""/"""+str(num)+""""},
		  { "href" : "http://localhost:5000/table/post/""" + test2+""""}],"""
	test += """ "items":["""
	for row in data:
		index = -1
		test += """{ "href": "http://localhost:5000/table/showone/"""+test2+"""/"""+str(num)
		test += """", "data": [ """
		for row2 in data2:
			index += 1
			test += """{ "name": " """ + str(row2[0]) + """ ", "value": " """ + str(row[int(index)]) + """ "},""" 
		test = test[:-1]
		test += """]},""" 
	test = test[:-1]
	test += """ ],"""
	test +=   """  "template": {"data" : [ """
	for row2 in data2:
		if  str(row2[0]) != "id":
			test += """ {"name": " """ + str(row2[0]) + """ ", "value": " """+str(row2[1])+""""},""" 
	test = test[:-1]
	test += """]}}}"""
	return str(test)
	#this data conforms to the Collection+JSON media-type but to convert the string to json the clinet side will have use the code below 
	#collection = json.loads(test)
	#print(json.dumps(collection, sort_keys=True, indent=4))
	


app.secret_key = 'password'

if __name__ == "__main__":
    app.run(debug=True)
	
# Patrick Connauhotn 
# C00167985
# this is how i tested my code 


# from flask import Flask, render_template, request, jsonify, session
# import DBcm
# from threading import Thread
# import hashlib , uuid
# from functools import wraps
# import base64
# import smtplib
# import base64
# from passlib.hash import sha256_crypt
# from collections import namedtuple
# import requests
# from collection_json import Collection
# import itertools
# import json

# temp ="""{"template": {"data": [
        # {"name": "name", "value": "test5"},
        # {"name": "description", "value": "test4.0"}]}}"""
		
# temp2 = """{"template": {"data": [
        # {"name": "handle", "value": "test5"},
		# {"name": "first", "value": "test5"},
		# {"name": "last", "value": "test5"},
		# {"name": "email", "value": "test5"},
        # {"name": "passwd", "value": "test4.0"}]}}"""

# def table_list():
	# data = requests.get('http://localhost:5000/table/list').text
	# collection = json.loads(data)
	# print(json.dumps(collection, sort_keys=True, indent=4))

# def table_showall():
	# data = requests.get('http://localhost:5000/table/showall/games').text
	# collection = json.loads(data)
	# print(json.dumps(collection, sort_keys=True, indent=4))

# def table_post():
	# r = requests.post('http://localhost:5000/table/post/players', data={'payload':temp2})
	# collection = json.loads(data)
	# print(json.dumps(collection, sort_keys=True, indent=4))

# def table_showone(test2:str,num:int):
	# data = requests.get('http://localhost:5000/table/showone/players/2').text
	# collection = json.loads(data)
	# print(json.dumps(collection, sort_keys=True, indent=4))
		


