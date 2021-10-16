from models.Encoder import Encoder
import json
import mysql.connector

# @author   Mehmet TETIK
# @version  1.0
# @purpose  Database connection with different (query) methods
#           Insert, Update, Select, Delete methods


class Database:

    # Create the database connection
    def __init__(self):
        db_host = ''
        db_table = ''
        db_username = ''
        db_password = ''

        try:
            self.db_connection = mysql.connector.connect(
                user=db_username,
                password=db_password,
                host=db_host,
                database=db_table
            )
            self.db_cursor = self.db_connection.cursor()
        except mysql.connector.Error as error:
            print("Connection couldn't be established: {}".format(error))

    # Database select query
    # @param query      select query
    # @param data       data to select (optional)
    def select(self, query, data=""):
        self.__init__()

        try:
            self.db_cursor.execute(query, data)
            row_headers = [x[0] for x in self.db_cursor.description]
            results = self.db_cursor.fetchall()
            json_data = []

            for result in results:
                json_data.append(dict(zip(row_headers, result)))

            return json.dumps(json_data, indent=2, cls=Encoder)
        except mysql.connector.Error as error:
            print("Data couldn't be retrieved: {}".format(error))
        finally:
            self.close_connection()

    # Database insert query
    # @param query      insert query
    # @param data       data to insert
    def insert(self, query, data):
        self.__init__()

        try:
            self.db_cursor.execute(query, data)
            self.db_connection.commit()

            if self.db_cursor.rowcount > 0:
                print(self.db_cursor.rowcount, "record(s) inserted")
                return True
            else:
                return False
        except mysql.connector.Error as error:
            print("Data couldn't be saved: {}".format(error))
        finally:
            self.close_connection()

    # Database update query
    # @param query      update query
    # @param data       data to insert
    def update(self, query, data):
        self.__init__()

        try:
            self.db_cursor.execute(query, data)
            self.db_connection.commit()

            if self.db_cursor.rowcount > 0:
                print(self.db_cursor.rowcount, "record(s) affected")
                return True
            else:
                return False
        except mysql.connector.Error as error:
            print("Data couldn't be updated: {}".format(error))
        finally:
            self.close_connection()

    # Database delete query
    # @param query      delete query
    # @param data       data to delete (optional)
    def delete(self, query, data=""):
        self.__init__()

        try:
            self.db_cursor.execute(query, data)
            self.db_connection.commit()

            if self.db_cursor.rowcount > 0:
                print(self.db_cursor.rowcount, "record(s) deleted")
                return True
            else:
                return False
        except mysql.connector.Error as error:
            print("Data couldn't be deleted: {}".format(error))
        finally:
            self.close_connection()

    def close_connection(self):
        self.db_cursor.close()
        self.db_connection.close()

    # Close database connection
    # def __del__(self):
    #     self.db_connection.close()
    #     self.db_cursor.close()
