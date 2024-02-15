import os
import datetime
import numpy as np

import utils

class CAMERA:

    def __init__(self, name, freq):
        self.name = name
        self.values = []
        self.freq = freq

    @property
    def type(self):
        return "Camera"

    def config(self, path):
        for r, d, f in os.walk(path):
            for file in f:
                if ".txt" in file:
                    with open(os.path.join(r, file), "r") as fd:
                        for line in fd:
                            fall, *rest = line.split()
                            if int(fall) == 1:
                                self.values.append(os.path.join("train/",os.path.dirname(r).split("/")[-1], file[:-3]+"png"))

    def find_value(self, timestamp):
        date = datetime.datetime.fromtimestamp(timestamp/1000)
        count, unit = int(self.freq[:-1]), utils.UNITS[self.freq[-1]]
        if date.__getattribute__(unit[:-1]) % count == 0 and check_missing(date, self.freq[-1]):
            idx = np.random.randint(0, len(self.values)-1)
            return [{"timestamp": timestamp, "file": self.values[idx]}], self.name

        return [], None

def check_missing(date, unit, cmp = 0):
    ret = True
    for tdx in utils.UNITS_MISSING[unit]:
        ret = ret and (True if date.__getattribute__(tdx) == cmp else False)
    return ret
