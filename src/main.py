import os

import discord

TOKEN = os.environ.get("DISCORD_BOT_TOKEN")


class SpamFilter(discord.Client):
    def __init__(self):
        super().__init__()
        self._summary_channel = None
        self._discussions_channel = None

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
