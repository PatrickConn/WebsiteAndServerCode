# Patrick Connauhotn 
# C00167985
# this is how i tested my code 


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
import itertools
import json

temp ="""{"template": {"data": [
        {"name": "name", "value": "test5"},
        {"name": "description", "value": "test4.0"}]}}"""
		
temp2 = """{"template": {"data": [
        {"name": "name", "value": "Horny's"},
		{"name": "dob", "value": "(1992,2,13,7,47)"},
		{"name": "loves", "value": "'carrot','papaya'"},
		{"name": "weight", "value": "582"},
		{"name": "gender", "value": "M"},
        {"name": "vampires", "value": "63"}]}}"""

def table_list():
	data = requests.get('http://localhost:5000/table/list').text
	collection = json.loads(data)
	print(json.dumps(collection, sort_keys=True, indent=4))

def table_showall():
	data = requests.get('http://localhost:5000/table/showall/unicorns').text
	collection = json.loads(data)
	print(json.dumps(collection, sort_keys=True, indent=4))

def table_post():
	r = requests.post('http://localhost:5000/table/post/unicorns', data={'payload':temp2}).text
	collection = json.loads(r)
	print(json.dumps(collection, sort_keys=True, indent=4))
	#print(r)

def table_showone():
	data = requests.get('http://localhost:5000/table/showone/unicorns/56d6cf13ed28056f03023359').text
	collection = json.loads(data)
	print(json.dumps(collection, sort_keys=True, indent=4))

def table_post_get():
	data = requests.get('http://localhost:5000/table/post/unicorns').text
	collection = json.loads(data)
	print(json.dumps(collection, sort_keys=True, indent=4))
		
table_post()
