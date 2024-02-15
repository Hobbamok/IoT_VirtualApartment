import os
import yaml


class ConfigLoader():
    def __init__(self):
        pass

    def __new__(self, filename: str):
        filepath = os.path.join(os.curdir, filename)
        with open(filepath, "r") as file:
            self.data = yaml.safe_load(file)

        return self.data
