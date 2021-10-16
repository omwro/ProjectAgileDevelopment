import decimal
import json
import datetime

# @author   Mehmet TETIK
# @version  1.0
# @purpose  Encoding various datatypes


class Encoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime.datetime):
            return obj.isoformat()
        if isinstance(obj, decimal.Decimal):
            return float(obj)

        return json.JSONEncoder.default(self, obj)
