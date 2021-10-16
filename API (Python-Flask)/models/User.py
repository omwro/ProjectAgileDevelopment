import os
from flask import jsonify, request
from models.Database import *
import datetime

# @author   Mehmet TETIK
# @version  1.0
# @purpose  User class with methods

db = Database()
#   Allowed file extensions
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg'])
#   Upload avatars to this location
UPLOAD_FOLDER = 'static/avatars'
#   Retrieve avatars from URL
AVATARS_URL = ':5000/static/avatars/'


class User:

    # Check whether file extension is allowed
    # @param filename       File fullname
    def allowed_file(self, filename):
        return filename[-3:].lower() in ALLOWED_EXTENSIONS

    # Retrieve all user details or single user if id given
    # @param *id        id of user
    def get(self, *id):
        if id:
            return db.select("SELECT * FROM gebruiker WHERE id = %s", id)
        else:
            return db.select("SELECT * FROM gebruiker")

    # Retrieve user animals by id
    # @param id         id of user
    def get_with_animals(self, id):
        if id:
            return db.select("SELECT * FROM gebruiker_dier "
                             "INNER JOIN dier ON dier.id = gebruiker_dier.dier_id "
                             "WHERE gebruiker_dier.gebruiker_id = %s", (id,))

    # Catch animal
    # @param user_id    id of user
    # @param animal_id  id of animal
    def catch_animal(self, user_id, animal_id, placed_animal_id):
        if user_id:
            if animal_id and placed_animal_id:
                execute = db.insert("INSERT INTO gebruiker_dier (gebruiker_id, dier_id, geplaatste_dier_id) VALUES (%s, %s, %s)",
                    (user_id, animal_id, placed_animal_id))

                if execute:
                    return jsonify({'message': 'Succesfully caught an animal'}), 201
                else:
                    return jsonify({'message': 'Something went wrong'}), 502
            else:
                return jsonify({'message': 'Animal not found'}), 404
        else:
            return jsonify({'message': 'User not found'}), 404

    # Retrieve unique animal count by user
    # @param id         id of user
    def get_unique_animal_count(self, id):
        if id:
            return db.select("SELECT COUNT(DISTINCT(dier_id)) AS aantal_unieke_dieren FROM gebruiker_dier "
                             "WHERE gebruiker_id = %s", (id,))

    # Create account
    # @param username       username of user
    # @param gender         gender of user
    def register(self, username, gender):
        if username and gender:
            check_user = db.select("SELECT * FROM gebruiker WHERE gebruikersnaam = %s", (username,))

            json_data = json.loads(check_user)
            count_user = len(json_data)

            # If account with the given username does not exists
            if count_user == 0:
                execute = db.insert("INSERT INTO gebruiker (gebruikersnaam, geslacht, laatst_gezien) VALUES (%s, %s, NOW())",
                                    (username, gender))

                if execute:
                    return db.select("SELECT * FROM gebruiker ORDER BY id DESC LIMIT 1;")
                else:
                    return jsonify({'message': 'Er ging iets fout'}), 502
            else:
                return jsonify({'message': 'Er bestaat al een account met de opgegeven gebruikersnaam'}), 401

        return jsonify({'message': 'Geen gegevens opgegeven'}), 400

    # User login
    # @param username       username of user
    def login(self, username):
        if username:
            check_user = db.select("SELECT * FROM gebruiker WHERE gebruikersnaam = %s", (username,))

            json_data = json.loads(check_user)
            count_user = len(json_data)

            # If account with given username exists
            if count_user == 1:
                json_response = json_data[0]
                # If account of user is still active
                if json_response['actief'] == 1:
                    # If user is not in use
                    if json_response['online'] == 0:
                        execute = db.update("UPDATE gebruiker SET online = %s WHERE gebruikersnaam = %s", (1, username))

                        if execute:
                            return self.get(json_response['id'])
                        else:
                            return jsonify({'message': 'Er ging iets fout'}), 502

                    if json_response['online'] == 1:
                        return jsonify({'message': 'Deze account is al ingelogd, probeer later opnieuw'}), 401
                else:
                    return jsonify({'message': 'Deze account is geblokkeerd'}), 401
            else:
                return jsonify({'message': 'Er bestaat geen account met de opgegeven gebruikersnaam'}), 404

        return jsonify({'message': 'Geen gebruikersnaam opgegeven'}), 400

    # User logout
    # @param id             id of user
    # @param playtime       new playtime value
    def logout(self, id, playtime):
        if id and playtime:
            # Get current datetime
            now = datetime.datetime.now()

            execute = db.update(
                "UPDATE gebruiker SET speeltijd = %s, online = %s, laatst_gezien = %s WHERE id = %s",
                (playtime, 0, now, id))

            if execute:
                return self.get(id)
            else:
                return jsonify({'message': 'No such user or given data already exists'}), 401

        return jsonify({'message': 'No data given'}), 400

    # User avatar change
    # @param id             id of user
    # @param avatar         avatar to be inserted
    def change_avatar(self, id, avatar):
        if id:
            if avatar:
                # If file has one of the allowed extensions
                if self.allowed_file(avatar.filename):
                    # Save file to the server
                    avatar.save(os.path.join(UPLOAD_FOLDER, avatar.filename))
                    # Update user avatar URL
                    self.update(id, 'avatar', AVATARS_URL + avatar.filename)

                    return jsonify({'message': 'Image succesfully saved'}), 201
                else:
                    return jsonify({'message': 'Image has incorrect extension'}), 403
            else:
                return jsonify({'message': 'Image could not be saved'}), 401
        else:
            return jsonify({'message': 'User not found'}), 404

    # Update account
    # @param id         id of user
    # @param key        key to be updated
    # @param value      new value
    def update(self, id, key, value):
        if key and value:
            execute = db.update("UPDATE gebruiker SET " + key + " = %s WHERE id = %s", (value, id))

            if execute:
                return self.get(id)
            else:
                return jsonify({'message': 'No such user or given data already exists'}), 401

        return jsonify({'message': 'No data given'}), 400

    # Update currency/xp of account
    # @param id         id of user
    # @param currency   currency value
    # @param xp         xp value
    def update_xp_currency(self, id, currency, xp):
        if id:
            execute = db.update("UPDATE gebruiker SET geld = %s, XP = %s WHERE id = %s", (int(currency), int(xp), id))

            if execute:
                return jsonify({'message': 'User succesfully updated'}), 204
            else:
                return jsonify({'message': 'No such user or given data already exists'}), 401
        else:
            return jsonify({'message': 'User not found'}), 404
