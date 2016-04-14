import paypalrestsdk
import logging

import random
import string

import requests

from requests.auth import HTTPBasicAuth

import json

def MakeAPayment(cardType,cardNo,month,year,cvv2,first,last,totalAmount):
  PAYPAL_MODE = "sandbox"
  PAYPAL_CLIENT_ID = "Adfps2XwOPyVtlwhF7owQDxVJCm5lXrQ1QiCM9NakYgH-Gc0GKdFPZtshb9K_yaZCEf6aQLNGmb4Mbcj"
  PAYPAL_CLIENT_SECRET = "EEa1fIigzo2GjEA8KlUEQ8q-5R4T2JCR9pRta3ZTAD9xCodChhQKNK5luKA1ovrg-Yf60LGQdKsFZnZY"

paypalrestsdk.configure({
    "mode": PAYPAL_MODE, # sandbox or live
    "client_id": PAYPAL_CLIENT_ID,
    "client_secret": PAYPAL_CLIENT_SECRET })

  payment = paypalrestsdk.Payment({
    "intent": "sale",
    "payer": {
      "payment_method": "credit_card",
      "funding_instruments": [{
        "credit_card": {
          "type": cardType,
          "number": cardNo,
          "expire_month": month,
          "expire_year": year,
          "cvv2": cvv2,
          "first_name": first,
          "last_name": last }}]},
    "transactions": [{
      "item_list": {
        "items": [{
          "name": "ESCG",
          "sku": "ESCG",
          "price": totalAmount,
          "currency": "USD",
          "quantity": 1 }]},
      "amount": {
        "total": totalAmount,
        "currency": "USD" },
      "description": "PCScratchCard" }]})

  if payment.create():
    print("Payment created successfully")
    return True
  else:
    print(payment.error)
    return False

def PayOut(user_email,total_amount):

 # Get Authorize Toke
  logging.basicConfig(level=logging.INFO)
 # PAYPAL_MODE = os.environ.get("PAYPAL_MODE", None)
 # PAYPAL_CLIENT_ID = os.environ.get("PAYPAL_CLIENT_ID",None)
 # PAYPAL_CLIENT_SECRET = os.environ.get("PAYPAL_CLIENT_SECRET",None)
  PAYPAL_MODE = "sandbox"
  PAYPAL_CLIENT_ID = "AZ77-MasbvnoFEsoQAlk-5jPQb2PR31hUOy-5MIHVXiEgF6LyioAajnhC8dFhTJD_IMZtD4Gv4BH17ML"
  PAYPAL_CLIENT_SECRET = "EEKLn873Svy4cejjFQfRusR1jgXbKvOR1-BH2v5ysVq5QR6jgYZGGR9WnPJZBgDJsre5QZzI8ednpx7t"
  url = "https://api.sandbox.paypal.com/v1/oauth2/token"
  headers = {'Accept': 'application/json','Accept-Language': 'en_US'}
  payload = {'grant_type': 'client_credentials'}
  token_response = requests.post(url, auth=HTTPBasicAuth(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET), headers=headers, data=payload)
  body = json.loads(token_response.text)
  print(token_response.status_code)
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
  print(payout_response)
  payout_body = json.loads(payout_response.text)
  status = payout_body['batch_header']
  return status['batch_status']
