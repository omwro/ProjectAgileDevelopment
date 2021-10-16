import os
from flask import jsonify, request
from models.Database import *

# @author   Mehmet TETIK
# @version  1.0
# @purpose  Employee class with methods

db = Database()


class Employee:

    # Retrieve all employee details or single employee if id given
    # @param *id        id of employee
    def get(self, *id):
        if id:
            return db.select("SELECT * FROM medewerker WHERE id = %s", id)
        else:
            return db.select("SELECT * FROM medewerker")

    # Create new employee account
    # @param username   username of employee
    # @param gender     gender of employee
    # @param email      email of employee
    def register(self, username, gender, email):
        if username and gender and email:
            check_user = db.select("SELECT * FROM medewerker WHERE gebruikersnaam = %s", (username,))

            json_data = json.loads(check_user)
            count_user = len(json_data)

            # If account with the given username does not exists
            if count_user == 0:
                execute = db.insert("INSERT INTO medewerker (geslacht, gebruikersnaam, email) VALUES (%s, %s, %s)",
                                (gender, username, email))

                if execute:
                    return db.select("SELECT * FROM medewerker ORDER BY id DESC LIMIT 1;")
                else:
                    return jsonify({'message': 'Something went wrong'}), 502
            else:
                return jsonify({'message': 'Employee with the specified username already exists'}), 401
        else:
            return jsonify({'message': 'No data specified'}), 404
