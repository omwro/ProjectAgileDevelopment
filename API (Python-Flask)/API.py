from models.User import *
from models.Employee import *
from models.Animal import *
from flask import Flask, jsonify, request, make_response
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
import jwt
import datetime
from functools import wraps

"""
    +----------------------------
    |   API (Routing/Methods)
    +----------------------------
    |   @author     Mehmet TETIK
    |   @version    1.0
    +----------------------------
"""

user = User()
employee = Employee()
animal = Animal()
app = Flask(__name__, static_folder='static')
app.config.from_object('config.BaseConfig')
link = "/api/v1"

# Request limiter
limiter = Limiter(
    app,
    key_func=get_remote_address,
    # Default values
    default_limits=["2500 per day", "150 per hour"]
)


"""
    Authentication route to retrieve an access token
"""
@limiter.limit("200/hour")
@app.route(link+"/auth", methods=['GET'])
def authentication():
    auth_username = request.headers.get('AUTH_USER')
    auth_password = request.headers.get('AUTH_PASS')

    if auth_username == app.config['AUTHENTICATION_USER'] and auth_password == app.config['AUTHENTICATION_PASS']:
        token = jwt.encode(
            {
                'user': auth_username,
                # Set token valid for certain amount of hour
                'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=1)
            },
            app.config['SECRET_KEY']
        )

        return jsonify({'token': token.decode('UTF-8')})

    return make_response('Authentication failed', 401)


"""
    Route protection
"""
def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.args.get('token')

        if not token:
            return jsonify({'message': 'Token is required'}), 499

        try:
            data = jwt.decode(token, app.config['SECRET_KEY'])
        except:
            return jsonify({'message': 'Token is not valid'}), 498

        return f(*args, **kwargs)

    return decorated


"""
    Retrieve data of user by id
    @param user_id      id of user
"""
@limiter.limit("500/hour")
@app.route(link+"/users/<int:user_id>/", methods=['GET'])
def get_user(user_id):
    return user.get(user_id)


"""
    Retrieve animals of user by id
    @param user_id      id of user
"""
@limiter.limit("500/hour")
@app.route(link+"/users/<int:user_id>/animals", methods=['GET'])
def get_user_animals(user_id):
    if request.args.get('count', default=False, type=bool):
        return user.get_unique_animal_count(user_id)

    return user.get_with_animals(user_id)


"""
    #####   PROTECTED   #####
    Retrieve data from all users
"""
@limiter.limit("500/hour")
@app.route(link+"/users", methods=['GET'])
@token_required
def get_users():
    return user.get()


"""
    #####   PROTECTED   #####
    Create user
"""
@limiter.limit("500/hour")
@app.route(link+"/users", methods=['POST'])
@token_required
def register_user():
    return user.register(request.json['username'], request.json['gender'])


"""
    #####   PROTECTED   #####
    Retrieve data from all employees
"""
@limiter.limit("500/hour")
@app.route(link+"/employees", methods=['GET'])
@token_required
def get_employees():
    return employee.get()


"""
    #####   PROTECTED   #####
    Create employee
"""
@limiter.limit("500/hour")
@app.route(link+"/employees", methods=['POST'])
@token_required
def create_employee():
    return employee.register(request.json['username'], request.json['gender'], request.json['email'])


"""
    #####   PROTECTED   #####
    Retrieve data from all animals
"""
@limiter.limit("500/hour")
@app.route(link+"/animals", methods=['GET'])
@token_required
def get_animals():
    return animal.get()


"""
    #####   PROTECTED   #####
    Retrieve all placed animals
"""
@limiter.limit("200/hour")
@app.route(link+"/maps", defaults={'user_id': None}, methods=['GET'])
@app.route(link+"/maps/<int:user_id>", methods=['GET'])
@token_required
def get_placed_animals(user_id):
    if user_id:
        return animal.retrieve_placed_animals(user_id)

    return animal.retrieve_placed_animals()


"""
    #####   PROTECTED   #####
    Place an animal on the map
"""
@limiter.limit("200/hour")
@app.route(link+"/animals/place", methods=['POST'])
@token_required
def place_animal():
    return animal.place_animal_on_map(request.json['animal'], request.json['employee'], request.json['posX'],
                                      request.json['posY'], request.json['value'])


"""
    #####   PROTECTED   #####
    Catch an animal from the map
"""
@limiter.limit("500/hour")
@app.route(link+"/animals/catch", methods=['POST'])
@token_required
def catch_animal():
    return user.catch_animal(request.json['user'], request.json['animal'], request.json['placed_animal'])


"""
    #####   PROTECTED   #####
    Update user by id
    @param user_id      id of user
"""
@limiter.limit("200/hour")
@app.route(link+"/users/<int:user_id>", methods=['PUT'])
@token_required
def update_user(user_id):
    data = request.get_json()
    key = ''
    value = ''

    if 'username' in data:
        key = 'gebruikersnaam'
        value = data['username']

    elif 'gender' in data:
        key = 'geslacht'
        value = data['gender']

    elif 'playtime' in data:
        return user.logout(user_id, data['playtime'])

    elif 'currency' and 'XP' in data:
        return user.update_xp_currency(user_id, data['currency'], data['XP'])

    elif 'last_seen' in data:
        key = 'laatst_gezien'
        value = data['last_seen']

    elif 'active' in data:
        key = 'actief'
        value = data['active']

    return user.update(user_id, key, value)


"""
    Set user logged in
"""
@limiter.limit("500/hour")
@app.route(link+"/users/login", methods=['GET'])
def login_user():
    return user.login(request.headers.get('username'))


"""
    #####   PROTECTED   #####
    Upload avatar
"""
@limiter.limit("20/hour")
@app.route(link+"/users/<int:user_id>/avatar", methods=['POST'])
def upload_avatar(user_id):
    image_file = request.files['image']
    authentication = request.form['authentication']

    if authentication:
        if authentication == app.config['AUTHENTICATION_USER'] + app.config['AUTHENTICATION_PASS']:
            return user.change_avatar(user_id, image_file)
        else:
            return jsonify({'message': 'Authentication has failed'}), 401
    else:
        return jsonify({'message': 'Authentication is required'}), 403


"""
    Add extra custom header data
    For security reasons
"""
@app.after_request
def after_request(response):
    content_security_policy = ''
    content_security_policy += "default-src 'self'; "
    content_security_policy += "script-src 'self' 'unsafe-inline'; "
    content_security_policy += "style-src 'self' 'unsafe-inline'; "
    content_security_policy += "img-src 'self' data:; "
    content_security_policy += "connect-src 'self';"
    response.headers.add('Content-Security-Policy', content_security_policy)
    response.headers.add('X-Content-Type-Options', 'nosniff')
    response.headers.add('Strict-Transport-Security', 'max-age=86400; includeSubDomains')
    response.headers.add('X-Frame-Options', 'deny')
    response.headers.add('Access-Control-Allow-Methods', 'GET')
    response.headers.add('X-XSS-Protection', '1; mode=block')
    response.headers.set('Server', '')
    response.headers.add('Access-Control-Allow-Origin', '*')

    return response


if __name__ == "__main__":
    # DEVELOPMENT ENVIRONMENT
    # app.run(app.config['DEBUG'])

    # PRODUCTION ENVIRONMENT
    app.run(host=app.config['HOST_IP'], port=app.config['HOST_PORT'])
