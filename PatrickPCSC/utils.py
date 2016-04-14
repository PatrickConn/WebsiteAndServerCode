from ESCGProject import app
from Crypto.Cipher import AES
from Crypto import Random

#from ESCGProject.database import db_session, init_db
from ESCGProject.models import Card, Card_Detail, User

import random

import os
def pad(s):
	# Taken from https://www.youtube.com/watch?v=lsflaKpeB7Q
	return s + ((16-len(s) % 16) * '{')

# Taken from https://www.youtube.com/watch?v=lsflaKpeB7Q
def encrypt(card_id):
	plaintext = str(card_id)
	if not len(plaintext) %  16:
		plaintext = pad(plaintext)
	plaintext = pad(plaintext)
	encryptobj = AES.new('This is a key123', AES.MODE_CBC, 'This is an IV456')
	ciphertext = encryptobj.encrypt(plaintext)
	return list(ciphertext)

# Taken from https://www.youtube.com/watch?v=lsflaKpeB7Q
def decrypt(ciphertext):
	decryptobj = AES.new('This is a key123', AES.MODE_CBC, 'This is an IV456')
	plaintext = decryptobj.decrypt(ciphertext).decode('utf-8')
	l = plaintext.count('{')
	return plaintext[:len(plaintext)-l]


def getImages(isWinner,no_of_cards):
	imagelist = []
	finallist = []
	for f in os.listdir("ESCGProject/static"):
		imagelist.append(f)
	i = 0
	if isWinner==False:
		while(i < no_of_cards):
			image = imagelist[random.randrange(0, len(imagelist), 1)]
			if finallist.count(image)==2:
				pass
			else:
				finallist.append(image)
				i = i + 1
		return finallist
	else:
		win_image = imagelist[random.randrange(0, len(imagelist), 1)]
		i = 0
		while i < no_of_cards:
			if i < 3:
				finallist.append(win_image)
				i = i + 1
			else:
				image = imagelist[random.randrange(0, len(imagelist), 1)]
				if image == win_image:
					pass
				else:
					finallist.append(image)
					i = i + 1
		random.shuffle(finallist)
		return finallist
