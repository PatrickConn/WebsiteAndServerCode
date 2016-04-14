# Patrick Connauhotn 
# C00167985


from flask import Flask, render_template, request, jsonify, session
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
import requests
import sys
import mysql.connector 
from mysql.connector.constants import ClientFlag
from requests.auth import HTTPBasicAuth
import json
import re
from urllib.parse import urlparse
from urllib.parse import urljoin
from bs4 import BeautifulSoup, Comment

DBconfig = { 'host': '127.0.0.1',
                 'user': 'pat',
                 'password': 'p30401',
                 'database': 'UserDB' }

				 
app = Flask(__name__)	

@app.route('/show/page/<num>')
def display_page(num:int) -> 'html':
    tplt = 'page' + str(num) + '.html'
    return render_template(tplt,
                           the_title='This is my Assignment pages')
						  


    
@app.route('/data', methods=['POST'])
def Add_New_User() -> 'html':
	User1 = request.form['User']
	Password1 = request.form['Password']
	Email = request.form['Email']	
	sanitizeHtml(User1)
	return render_template('results.html',the_title = User1,)


def sanitizeHtml(value, base_url=None):
    rjs = r'[\s]*(&#x.{1,7})?'.join(list('javascript:'))
    rvb = r'[\s]*(&#x.{1,7})?'.join(list('vbscript:'))
    re_scripts = re.compile('(%s)|(%s)' % (rjs, rvb), re.IGNORECASE)
    validTags = 'p i strong b u a h1 h2 h3 pre br img'.split()
    validAttrs = 'href src width height'.split()
    urlAttrs = 'href src'.split() # Attributes which should have a URL
    soup = BeautifulSoup(value)
    for comment in soup.findAll(text=lambda text: isinstance(text, Comment)):
        # Get rid of comments
        comment.extract()
    for tag in soup.findAll(True):
        if tag.name not in validTags:
            tag.hidden = True
        attrs = tag.attrs
        tag.attrs = []
        for attr, val in attrs:
            if attr in validAttrs:
                val = re_scripts.sub('', val) # Remove scripts (vbs & js)
                if attr in urlAttrs:
                    val = urljoin(base_url, val) # Calculate the absolute url
                tag.attrs.append((attr, val))

    return soup.renderContents().decode('utf8')
		

app.secret_key = 'password'





if __name__ == "__main__":
    app.run(debug=True)
