import os
import collections

import discord

import db_reader
import text_processor
from spam_detector import SpamDetector

TOKEN = os.environ.get("DISCORD_BOT_TOKEN")


class SpamFilter(discord.Client):
    def __init__(self):
        super().__init__()
        self._summary_channel = None
        self._discussions_channel = None

        spam_vector = collections.Counter()
        msgs = db_reader.get_all_spams()
        avg_len = 0
        nb_vectors = 0
        for msg in msgs:
            tp = text_processor.TextProcessor(msg.message)
            for vector in tp.vectors.values():
                spam_vector += vector
                avg_len += len(vector)
                nb_vectors += 1
        avg_len /= nb_vectors
        self.detector = SpamDetector(spam_vector, avg_len)

    @property
    def summary_channel(self):
        return self._summary_channel

    @summary_channel.setter
    def summary_channel(self, value):
        if self._summary_channel is not None:
            raise RuntimeError("Value should be set only once")
        self._summary_channel = value

    @property
    def discussions_channel(self):
        return self._discussions_channel

    @discussions_channel.setter
    def discussions_channel(self, value):
        if self._discussions_channel is not None:
            raise RuntimeError("Value should be set only once")
        self._discussions_channel = value


bot = SpamFilter()


@bot.event
async def on_message(message):
    # Bot should not analyze its messages
    # (even though Discord's server settings should not allow this anyway)
    if message.author == bot.user:
        return

    if message.channel == bot.discussions_channel:
        analyzer = text_processor.TextProcessor(message.content)
        for sentence, vector in analyzer.vectors.items():
            if not bot.detector.is_spam(sentence, vector):
                bot_msg = "{0}: {1}".format(message.author, message.content)
                await bot.summary_channel.send(bot_msg)


@bot.event
async def on_ready():
    print('Logged in as')
    print(bot.user.name)
    print(bot.user.id)
    print('------')

    # Scan all channels to get the summary and discussions channels
    for chan in bot.get_all_channels():
        try:
            if chan.name == "summary":
                bot.summary_channel = chan
            elif chan.name == "discussions":
                bot.discussions_channel = chan
        except RuntimeError as e:  # For dev debug
            print(e)
            print("Chan list :")
            print([x for x in bot.get_all_channels()])


if __name__ == '__main__':
    bot.run(TOKEN)
