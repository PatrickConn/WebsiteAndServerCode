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
from collections import namedtuple
import requests
from collection_json import Collection
import itertools
import json

temp ="""{"template": {"data": [
        {"name": "name", "value": "test'5"},
        {"name": "description", "value": "test4.0"}]}}"""
		
temp2 = """{"template": {"data": [
        {"name": "game_id", "value": "5"},
		{"name": "player_id", "value": "5"},
		{"name": "score_id", "value": "5"}]}}"""

def table_list():
	data = requests.get('http://localhost:5000/table/list').text
	collection = json.loads(data)
	print(json.dumps(collection, sort_keys=True, indent=4))

def table_showall():
	data = requests.get('http://localhost:5000/table/showall/games').text
	collection = json.loads(data)
	print(json.dumps(collection, sort_keys=True, indent=4))

def table_post():
	r = requests.post('http://localhost:5000/table/post/games', data={'payload':temp}).text
	collection = json.loads(r)
	print(json.dumps(collection, sort_keys=True, indent=4))
	#print(r)

def table_showone():
	data = requests.get('http://localhost:5000/table/showone/players/2').text
	collection = json.loads(data)
	print(json.dumps(collection, sort_keys=True, indent=4))

def table_post_get():
	data = requests.get('http://localhost:5000/table/post/players').text
	collection = json.loads(data)
	print(json.dumps(collection, sort_keys=True, indent=4))
		
table_showall()
