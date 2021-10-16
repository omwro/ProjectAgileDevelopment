from API import app
from flask import json

API_URL = "/api/v1"


#   Test the authentication (to retrieve a token)
def test_auth():
    response = app.test_client().get(
        API_URL+'/auth',
        headers=({'AUTH_USER': 'crash', 'AUTH_PASS': 'Pad2019!'})
    )

    assert response.status_code == 200


#   Create a new user and save it
def test_user():
    response = app.test_client().post(
        API_URL + '/users/register',
        data=json.dumps({'username': 'testuser3', 'gender': 'Man'}),
        content_type='application/json',
    )

    assert response.status_code == 200

