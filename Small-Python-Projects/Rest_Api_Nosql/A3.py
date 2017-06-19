# Patrick Connauhotn 
# C00167985
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
from bson.objectid import ObjectId

from pymongo import MongoClient
import sys


connection = MongoClient('localhost', 27017)
db = connection['FantasyDB']



app = Flask(__name__)	
Table = namedtuple("Table","Name")

@app.route('/table/list')
def table_list():
	test = ""
	test += """{"collection" :{"version" : "1.0","href" : "http://localhost:5000/table/list", "items" :[{ "href": "http://localhost:5000/table/list", "data": [ """ 
	for each_movie in db.collection_names():
		test += """{"name":" """ + each_movie + """", "value": "http://localhost:5000/table/showall/""" + each_movie + """"},""" 
	test = test[:-1]
	test += """ ]}]}}"""
	return str(test)
	#this data conforms to the Collection+JSON media-type but to convert the string to json the clinet side will have use the code below 
	#collection = json.loads(test)
	#print(json.dumps(collection, sort_keys=True, indent=4))
	
@app.route('/table/showall/<test2>')
def table_showall(test2:str):
	collection = db[test2]
	data = collection.find()
	test = ""
	temp = ""
	
	test += """{"collection":{"version": "1.0","href": "http://localhost:5000/table/showall/"""+test2+"""","""
	test += """ "items":[ """
	for each_movie in  collection.find():	
		test += """{ "href": "http://localhost:5000/table/showone/"""+test2+"""/"""+str(each_movie.get('_id'))
		test += """ ","data": [ """ 
		for key, value in each_movie.items() :
			value = str(value)
			if type(value) is list:
				csvSring = ""
				for row in value:
					csvSring += "'"+row+"'" + ","
				csvSring = csvSring[:-1]
				test += """{ "name": " """ + str(key) + """", "value": " """ + str(csvSring) + """ " },""" 								
			elif value.find('[')!=-1:
				#str(value)
				value = value.replace('[' , '')
				value = value.replace(']' , '')
				test += """{ "name": " """ + str(key) + """", "value": " """ + str(value) + """ " },""" 
			else:
				test += """{ "name": " """ + str(key) + """", "value": " """ + str(value) + """ " },""" 
		test = test[:-1]
		test += """]},""" 
	test = test[:-1]
	test += """ ],"""
	for each_movie in  collection.find().limit(1):	
		test +=   """  "template": {"data" : [ """
		for key, value in each_movie.items() :
			if(str(key) != '_id'):
				test += """ { "name" : " """ + str(key) + """" , "value":" " },""" 
		test = test[:-1]
		test += """]}}}"""
	return str(test)
	#this data conforms to the Collection+JSON media-type but to convert the string to json the clinet side will have use the code below 
	#collection = json.loads(test)
	#print(json.dumps(collection, sort_keys=True, indent=4))
	

# payload ="""{"template": {"data": [
        # {"name": "name", "value": "test5"},
        # {"name": "description", "value": "test4.0"}]}}"""

@app.route('/table/post/<table>', methods=['POST','GET'])
def table_post(table:str):
	if request.method == 'POST':
		collection = db[table]
		data = Template.from_json(request.form['payload'])
		mods = data.to_dict()['template']['data']
		names = []
		values = []
		for x in mods:
			values.append(x['value'])
			names.append(x['name'])
		add = {}
		for row,row2 in zip(names,values):
			row2 = row2
			add[row] = row2
		id = collection.insert(add)
		

		test = ""
		test += """{"collection":{"version" : "1.0","href": "http://localhost:5000/table/post/"""+table+"""","""
		test += """ "links": [
			   { "href" : "http://localhost:5000/table/showall/""" + table+""""},
			   { "href" : "http://localhost:5000/table/showone/"""+table+"""/"""+str(id)+""""}]}}"""
		return str(test)
	else:
		test = ""
		test += """{"collection":{"version": "1.0","href": "http://localhost:5000/table/showall/"""+table+"""","""
		for each_movie in  collection.find().limit(1):	
			test +=   """  "template": {"data" : [ """
			for key, value in each_movie.items() :
				if(str(key) != '_id'):
					test += """ { "name" : " """ + str(key) + """" , "value":" " },""" 
			test = test[:-1]
		test += """]}}}"""
		return str(test)
		
	#this data conforms to the Collection+JSON media-type but to convert the string to json the clinet side will have use the code below 
	#collection = json.loads(test)
	#print(json.dumps(collection, sort_keys=True, indent=4))
	


@app.route('/table/showone/<test2>/<num>')
def table_showone(test2:str,num:int):
	collection = db[test2]
	data = collection.find()
	test = ""
	test += """{"collection":{"version" : "1.0","href": "http://localhost:5000/table/showone/"""+test2+"""/"""+num+"""","""
	test += """ "links": [
          { "href" : "http://localhost:5000/table/showall/""" + test2+""""},
          { "href" : "http://localhost:5000/table/showone/"""+test2+"""/"""+str(num)+""""},
		  { "href" : "http://localhost:5000/table/post/""" + test2+""""}],"""
	test += """ "items":["""
	for each_movie in  collection.find( {"_id": ObjectId(num)} ):	
		test += """{ "href": "http://localhost:5000/table/showone/"""+test2+"""/"""+str(num)
		test += """", "data": [ """
		for key, value in each_movie.items() :
			value = str(value)
			if type(value) is list:
				csvSring = ""
				for row in value:
					csvSring += "'"+row+"'" + ","
				csvSring = csvSring[:-1]
				test += """{ "name": " """ + str(key) + """", "value": " """ + str(csvSring) + """ " },""" 								
			elif value.find('[')!=-1:
				#str(value)
				value = value.replace('[' , '')
				value = value.replace(']' , '')
				test += """{ "name": " """ + str(key) + """", "value": " """ + str(value) + """ " },""" 
			else:
				test += """{ "name": " """ + str(key) + """", "value": " """ + str(value) + """ " },""" 
		test = test[:-1]
		test += """]},""" 
	test = test[:-1]
	test += """ ],"""
	for each_movie in  collection.find().limit(1):	
		test +=   """  "template": {"data" : [ """
		for key, value in each_movie.items() :
			if(str(key) != '_id'):
				test += """ { "name" : " """ + str(key) + """" , "value":" " },""" 
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
		


