import os
from flask import jsonify, request
from models.Database import *

# @author   Mehmet TETIK
# @version  1.0
# @purpose  Animal class with methods

db = Database()


class Animal:

    # Retrieve all animal details or single animal if id given
    # @param *id        id of animal
    def get(self, *id):
        if id:
            return db.select("SELECT * FROM dier WHERE id = %s", id)
        else:
            return db.select("SELECT * FROM dier")

    # Retrieve all placed animals on the map, but without user's animals
    # @param *user_id   id of user
    def retrieve_placed_animals(self, *user_id):
        if user_id:
            return db.select("SELECT * FROM geplaatste_dieren WHERE id NOT IN "
                            "(SELECT geplaatste_dier_id FROM gebruiker_dier WHERE gebruiker_id = %s)", user_id)

        return db.select("SELECT * FROM geplaatste_dieren")

    # Place animal on the map
    def place_animal_on_map(self, animal, employee, pos_x, pos_y, value):
        if animal and employee and pos_x and pos_y and value:
            execute = db.insert("INSERT INTO geplaatste_dieren (dier_id, medewerker_id, posX, posY, waarde)"
                                "VALUES (%s, %s, %s, %s, %s)", (animal, employee, pos_x, pos_y, value))

            if execute:
                return jsonify({'message': 'Dier is succesvol geplaatst'}), 201
            else:
                return jsonify({'message': 'Er ging iets fout'}), 502
