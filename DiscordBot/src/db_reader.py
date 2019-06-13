import os

from sqlalchemy import Column, Integer, String, Boolean, create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

pwd = os.environ.get('NLP_DB_ROOT_PASSWORD')

Base = declarative_base()
engine = create_engine('mysql+pymysql://root:{0}@localhost/nlp'.format(pwd),
                       pool_recycle=3600)
Session = sessionmaker(bind=engine)
session = Session()

class DbMessage(Base):
    __tablename__ = "message"
    idNo = Column(Integer, primary_key=True)
    message = Column(String)
    value = Column(Boolean)


class Message():
    def __init__(self, msg, value):
        self.message = msg
        self.is_spam = not bool(value)

    def __str__(self):
        return '<Message: {0} - {1}>'.format(self.message, self.is_spam)


def get_all_spams():
    msgs = list()
    for result in session.query(DbMessage).filter(DbMessage.value == 0).all():
        msgs.append(Message(result.message, result.value))
    return msgs


def get_all_messages():
    msgs = list()
    for result in session.query(DbMessage):
        msgs.append(Message(result.message, result.value))
    return msgs


if __name__ == '__main__':
    for msg in get_all_spams():
        print(msg)
