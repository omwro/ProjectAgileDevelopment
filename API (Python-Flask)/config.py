# @author   Mehmet TETIK
# @version  1.0
# @purpose  Configuration


# Basic configuration
class BaseConfig(object):
    #   DEBUG mode
    DEBUG = True

    #   API credentials
    SECRET_KEY = ''
    AUTHENTICATION_USER = ''
    AUTHENTICATION_PASS = ''

    #   PRODUCTION ENVIRONMENT
    HOST_IP = ''
    HOST_PORT = 5000
