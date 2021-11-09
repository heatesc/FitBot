import discord
from discord.ext import commands
import re

class MyClient(discord.Client):
    async def on_ready(self):
        print('Logged on as', self.user)

    async def on_message(self, message):
        # don't respond to ourselves
        if message.author == self.user:
            return

        if re.match(".*\$track.*",message.content):
            await message.channel.send("@{}, your tracking is pending!".format(message.author))
        else: 
            print("hmm")

client = MyClient()
client.run('ODk3MzQ3OTY1MzY1MTMzMzU1.YWUWag.-4JwT_DD6siMFViv0RwHZgX4feE')